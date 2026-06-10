package com.example.homeworkout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.homeworkout.presentation.root.AppRoot
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * Flipped to true the moment the Compose splash image has been laid out.
     * setKeepOnScreenCondition polls this on the main thread before every frame,
     * so the (invisible) system splash exits on the very next frame after Compose
     * draws — revealing the full-screen branded Compose splash with no blank gap.
     */
    @Volatile
    private var composeSplashReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition { !composeSplashReady }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppRoot(
                modifier = Modifier.fillMaxSize(),
                onComposeSplashReady = { composeSplashReady = true },
            )
        }
    }
}
