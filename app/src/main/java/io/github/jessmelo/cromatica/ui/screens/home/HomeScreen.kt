package io.github.jessmelo.cromatica.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.jessmelo.cromatica.audio.AudioProcessor
import io.github.jessmelo.cromatica.data.ChromaticScalePitch
import io.github.jessmelo.cromatica.data.getPitch
import io.github.jessmelo.cromatica.ui.theme.RosewaterLight
import io.github.jessmelo.cromatica.ui.components.PitchSlider
import java.util.Locale

@Composable
fun HomeScreen(
    audioProcessor: AudioProcessor
) {
    val frequency by audioProcessor.pitch.collectAsState()
    val audioRecord by audioProcessor.audioRecord.collectAsState()
    val detectedPitch = getPitch(frequency.toDouble())
    val pitchDifference = if (detectedPitch == ChromaticScalePitch.UNDEFINED) {
        (ChromaticScalePitch.C1.frequency + ChromaticScalePitch.C8.frequency) / 2
    } else {
        detectedPitch.frequency - frequency
    }

    val leftNote = if (detectedPitch == ChromaticScalePitch.UNDEFINED) {
        ChromaticScalePitch.C1
    } else {
        ChromaticScalePitch.entries[detectedPitch.ordinal - 1]
    }
    val rightNote = if (detectedPitch == ChromaticScalePitch.UNDEFINED) {
        ChromaticScalePitch.C8
    } else {
        ChromaticScalePitch.entries[detectedPitch.ordinal + 1]
    }

    val minFrequency = (leftNote.frequency - detectedPitch.frequency).toFloat()
    val maxFrequency = (rightNote.frequency - detectedPitch.frequency).toFloat()
    val pitchRange = minFrequency..maxFrequency

    LaunchedEffect(audioRecord) {
        audioProcessor.startRecording()
    }

    DisposableEffect(Unit) {
        onDispose {
            audioProcessor.stopRecording()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RosewaterLight),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .align(CenterHorizontally),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${String.format(Locale.getDefault(), "%.3f", frequency)} Hz",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp),
                fontSize = 24.sp,
            )

            Text(
                text = detectedPitch.noteName,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp),
                fontSize = 24.sp,
            )
            Text(
                text = if (pitchDifference > 5 && detectedPitch != ChromaticScalePitch.UNDEFINED) {
                    "Muito grave"
                } else if (pitchDifference < -5) {
                    "Muito agudo"
                } else {
                    "Afinado"
                },
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp),
                fontSize = 14.sp,
            )
            PitchSlider(pitchDifference = pitchDifference, pitchRange = pitchRange)
        }
    }
}

