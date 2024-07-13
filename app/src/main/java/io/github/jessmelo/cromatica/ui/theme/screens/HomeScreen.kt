package io.github.jessmelo.cromatica.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.jessmelo.cromatica.audio.AudioProcessor
import io.github.jessmelo.cromatica.data.ChromaticScalePitch
import io.github.jessmelo.cromatica.data.getPitch
import io.github.jessmelo.cromatica.ui.theme.Ivory
import io.github.jessmelo.cromatica.ui.theme.Lilac
import java.util.Locale

@Composable
fun HomeScreen(
    audioProcessor: AudioProcessor
) {
    val frequency by audioProcessor.pitch.collectAsState()
    val audioRecord by audioProcessor.audioRecord.collectAsState()
    val scrollState = rememberScrollState()
    val detectedPitch = remember { getPitch(frequency.toDouble()) }

    LaunchedEffect(audioRecord) {
        audioProcessor.startRecording()
    }

    DisposableEffect(Unit) {
        onDispose {
            audioProcessor.stopRecording()
        }
    }

    LaunchedEffect(detectedPitch) {
        val index = ChromaticScalePitch.entries.indexOfFirst { it.name == detectedPitch.name }
        if (index != -1) {
            // Scroll to the position of the detected pitch
            scrollState.animateScrollTo(index * 100) // Approximate position calculation
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Lilac),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Ivory),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = CenterHorizontally,
        ) {
            Text(
                text = "${String.format(Locale.getDefault(), "%.3f", frequency)} Hz",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp),
                fontFamily = FontFamily.Serif,
                fontSize = 24.sp,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                ChromaticScalePitch.entries.forEach {
                    Card(
                        modifier = Modifier.padding(8.dp),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text(
                            text = it.noteName,
                            modifier = Modifier.padding(8.dp),
                            fontFamily = FontFamily.Serif,
                            fontSize = 16.sp,
                        )
                    }
                }
            }
        }
    }
}
