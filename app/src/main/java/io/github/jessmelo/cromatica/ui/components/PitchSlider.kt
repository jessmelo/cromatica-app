package io.github.jessmelo.cromatica.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import io.github.jessmelo.cromatica.ui.theme.Lilac

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PitchSlider(
    pitchDifference: Double,
    pitchRange: ClosedFloatingPointRange<Float>
) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    Slider(
        value = pitchDifference.toFloat(),
        onValueChange = {
            sliderPosition = it
        },
        track = {
            val sliderTrackHeight = 2.dp
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(sliderTrackHeight)
                    .background(
                        color = Color.DarkGray,
                        shape = RectangleShape
                    )
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        valueRange = pitchRange,
        colors = SliderDefaults.colors(
            thumbColor = Lilac,
            activeTrackColor = Color.DarkGray,
            inactiveTrackColor = Color.DarkGray,
        ),
    )
}