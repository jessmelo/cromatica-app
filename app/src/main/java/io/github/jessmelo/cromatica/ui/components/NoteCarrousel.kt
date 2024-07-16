package io.github.jessmelo.cromatica.ui.components

import android.content.res.Resources
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.jessmelo.cromatica.data.ChromaticScalePitch
import io.github.jessmelo.cromatica.data.getPitch
import kotlinx.coroutines.launch

@Composable
fun NoteCarrousel(
    frequency: Float
) {
    val scope = rememberCoroutineScope()
    val detectedPitch = getPitch(frequency.toDouble())
    val noteWidth = 64.dp
    val noteIndices = remember {
        ChromaticScalePitch.entries.withIndex().associate { it.value.noteName to it.index }
    }
    val firstNoteIndex = noteIndices[ChromaticScalePitch.entries.first().noteName] ?: 0
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val initialScrollPosition =
        (firstNoteIndex * noteWidth.value - screenWidth.value / 2) * Resources.getSystem().displayMetrics.density

    val scrollState = rememberScrollState(initial = initialScrollPosition.toInt())

    LaunchedEffect(detectedPitch) {
        val index = noteIndices[detectedPitch.noteName] ?: return@LaunchedEffect
        scope.launch {
            scrollState.animateScrollTo((index * ((noteWidth.value - 4) * Resources.getSystem().displayMetrics.density)).toInt())
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ChromaticScalePitch.entries.forEach {
            Card(
                modifier = Modifier.width(noteWidth),
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(
                    text = it.noteName,
                    modifier = Modifier.padding(8.dp),
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.Serif,
                    fontSize = 16.sp,
                )
            }
        }
    }
}
