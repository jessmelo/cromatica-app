package io.github.jessmelo.cromatica.audio

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlinx.coroutines.flow.MutableStateFlow
import org.apache.commons.math3.transform.DftNormalization
import org.apache.commons.math3.transform.FastFourierTransformer
import org.apache.commons.math3.transform.TransformType

class AudioProcessor {
    val pitch: MutableStateFlow<Float> = MutableStateFlow(0f)
    private var isRecording = false

    private val sampleRate = 44100 // Sampling rate in Hz
    private val bufferSize = AudioRecord.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    )

    @SuppressLint("MissingPermission")
    val audioRecord = AudioRecord(
        MediaRecorder.AudioSource.MIC,
        sampleRate,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT,
        bufferSize
    )

    fun startRecording() {
        val buffer = ShortArray(bufferSize)
        audioRecord.startRecording()
        isRecording = true

        Thread {
            while (isRecording) {
                val readCount = audioRecord.read(buffer, 0, bufferSize)
                if (readCount > 0) {
                    // Reduce sensitivity by lowering the amplitude
                    for (i in buffer.indices) {
                        buffer[i] = (buffer[i] * 0.5).toInt().toShort()
                    }
                    val fftResult = fft(buffer)
                    val pitch = detectPitchFromFFT(fftResult)
                    this.pitch.value = pitch / 2
                    // Handle detected pitch (e.g., update UI)
                    println("Detected pitch: $pitch Hz")
                }
                Thread.sleep(100)
            }
            audioRecord.stop()
            audioRecord.release()
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
