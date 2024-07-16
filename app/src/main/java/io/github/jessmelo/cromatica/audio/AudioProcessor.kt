package io.github.jessmelo.cromatica.audio

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.apache.commons.math3.transform.DftNormalization
import org.apache.commons.math3.transform.FastFourierTransformer
import org.apache.commons.math3.transform.TransformType

class AudioProcessor(private val context: Context) {
    private val _pitch: MutableStateFlow<Float> = MutableStateFlow(0f)
    val pitch: StateFlow<Float> = _pitch
    private var isRecording = false

    private val sampleRate = 44100 // Sampling rate in Hz
    private val bufferSize = AudioRecord.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    )

    private var _audioRecord: MutableStateFlow<AudioRecord?> = MutableStateFlow(createAudioRecord())
    val audioRecord: StateFlow<AudioRecord?> = _audioRecord

    fun restartAudioRecord() {
        _audioRecord.value = createAudioRecord()
    }

    private fun createAudioRecord(): AudioRecord? {
        return if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            null
        } else {
            AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize
            )
        }
    }

    fun startRecording() {
        if (_audioRecord.value == null) {
            return
        }
        val buffer = ShortArray(bufferSize)
        (_audioRecord.value as AudioRecord).startRecording()
        isRecording = true

        CoroutineScope(Dispatchers.IO).launch {
            while (isRecording) {
                val readCount = _audioRecord.value!!.read(buffer, 0, bufferSize)
                if (readCount > 0) {
                    // Reduce sensitivity by lowering the amplitude
                    for (i in buffer.indices) {
                        buffer[i] = (buffer[i] * 0.0006).toInt().toShort()
                    }
                    val fftResult = fft(buffer)
                    val pitch = detectPitchFromFFT(fftResult)
                    _pitch.value = pitch / 2
                    // Handle detected pitch (e.g., update UI)
                    println("Detected pitch: $pitch Hz")
                }
                delay(150)
            }
            _audioRecord.value!!.stop()
            _audioRecord.value!!.release()
        }.start()
    }

    fun stopRecording() {
        isRecording = false
    }

    private fun fft(buffer: ShortArray): DoubleArray {
        var n = buffer.size
        // Check if n is a power of 2
        if (n and (n - 1) != 0) {
            // If not, increase n to the next power of 2
            var count = 0
            while (n != 0) {
                n = n shr 1
                count += 1
            }
            n = 1 shl count
        }
        // Pad the buffer with zeros
        val paddedBuffer = buffer.copyOf(n)
        val transformer = FastFourierTransformer(DftNormalization.STANDARD)
        val complexData = transformer.transform(
            paddedBuffer.map { it.toDouble() }.toDoubleArray(),
            TransformType.FORWARD
        )
        val magnitudes = DoubleArray(n / 2)
        for (i in magnitudes.indices) {
            magnitudes[i] = complexData[i].abs()
        }
        return magnitudes
    }

    private fun detectPitchFromFFT(fftResult: DoubleArray): Float {
        val peakIndex = fftResult.indices.maxByOrNull { fftResult[it] } ?: return -1f
        return (peakIndex * sampleRate) / fftResult.size.toFloat()
    }
}
