package com.example.homeworkout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.homeworkout.di.ProvideAppContainer
import com.example.homeworkout.presentation.navigation.HomeWorkoutNavHost
import com.example.homeworkout.ui.theme.HomeWorkoutTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProvideAppContainer {
                HomeWorkoutTheme {
                    HomeWorkoutNavHost(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}
