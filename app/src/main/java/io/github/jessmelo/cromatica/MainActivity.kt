package io.github.jessmelo.cromatica

import android.Manifest.permission.RECORD_AUDIO
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.checkSelfPermission
import io.github.jessmelo.cromatica.audio.AudioProcessor
import io.github.jessmelo.cromatica.ui.CromaticaApp
import io.github.jessmelo.cromatica.ui.theme.CromaticaTheme

class MainActivity : ComponentActivity() {
    private lateinit var audioProcessor: AudioProcessor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        checkPermissions()

        audioProcessor = AudioProcessor(this)

        setContent {
            CromaticaTheme {
                CromaticaApp(audioProcessor)
            }
        }
    }

    private fun checkPermissions() {
        val permission = checkSelfPermission(this, RECORD_AUDIO)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            requestPermission()
        }
    }

    private fun requestPermission() {
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (!isGranted) {
                Toast.makeText(
                    this,
                    "Permission to record audio is required",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // restart the activity to start the audio processor
                audioProcessor.restartAudioRecord()
            }
        }
        requestPermissionLauncher.launch(RECORD_AUDIO)
    }
}
