package io.github.jessmelo.cromatica.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import io.github.jessmelo.cromatica.audio.AudioProcessor
import java.util.Locale

@Composable
fun HomeScreen() {
    val audioProcessor = AudioProcessor()
    val frequency by audioProcessor.pitch.collectAsState()

    LaunchedEffect(PERMISSION_GRANTED) {
        audioProcessor.startRecording()
    }

    DisposableEffect(Unit) {
        onDispose {
            audioProcessor.stopRecording()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(100.dp)
            ) {
                Text(
                    text = "Frequency: ${String.format(Locale.getDefault(), "%.3f", frequency)} Hz"
                )
            }
        }
    }
}