package com.example.homeworkout.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun ExerciseLottieAnimation(
    exerciseId: String,
    modifier: Modifier = Modifier,
    isRestPhase: Boolean = false,
    isPlaying: Boolean = true,
) {
    val assetName = ExerciseAnimationResolver.resolveAsset(exerciseId, isRestPhase)
    val compositionResult = rememberLottieComposition(LottieCompositionSpec.Asset(assetName))
    val composition = compositionResult.value

    if (composition != null) {
        val progress by animateLottieCompositionAsState(
            composition = composition,
            isPlaying = isPlaying,
            iterations = LottieConstants.IterateForever,
        )
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = modifier,
        )
    } else {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.Default.FitnessCenter,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxSize(0.5f),
            )
        }
    }
}
