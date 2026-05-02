package com.leomilano.diamond.ui.screens.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onAnimationComplete: () -> Unit
) {
    val scale = remember { Animatable(0.92f) }
    val alpha = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Elegant iOS-style reveal: slow fade and gentle scale
        alpha.animateTo(1f, animationSpec = tween(durationMillis = 600))
        scale.animateTo(1f, animationSpec = tween(durationMillis = 1400))
        textAlpha.animateTo(1f, animationSpec = tween(durationMillis = 1000))
        delay(1500)
        // Hold briefly, then fade out
        alpha.animateTo(0f, animationSpec = tween(durationMillis = 800))
        onAnimationComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .alpha(alpha.value),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = textAlpha.value > 0.01f,
            enter = fadeIn(tween(600)),
            exit = fadeOut(tween(400))
        ) {
            Text(
                text = "Bonjour",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Thin,
                    letterSpacing = 8.sp,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier
                    .scale(scale.value)
                    .alpha(textAlpha.value)
            )
        }
    }
}
