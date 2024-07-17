package io.github.jessmelo.cromatica.ui.screens.guitartuner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.jessmelo.cromatica.audio.AudioProcessor
import io.github.jessmelo.cromatica.ui.theme.RosewaterLight

@Composable
fun GuitarTuner(
    audioProcessor: AudioProcessor
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RosewaterLight),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

    }
}