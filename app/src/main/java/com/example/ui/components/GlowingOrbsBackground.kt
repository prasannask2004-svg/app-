package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.CosmicDarkBackground
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.PinkAccent
import com.example.ui.theme.PurpleGlow

@Composable
fun GlowingOrbsBackground(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "orbs")

    val scale1 by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale1"
    )

    val scale2 by infiniteTransition.animateFloat(
        initialValue = 1.1f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale2"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CosmicDarkBackground)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Orb 1: Purple / Pink Glow (Top Left)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        PurpleGlow.copy(alpha = 0.45f),
                        PinkAccent.copy(alpha = 0.25f),
                        Color.Transparent
                    ),
                    center = Offset(width * 0.25f, height * 0.25f),
                    radius = (width * 0.55f) * scale1
                ),
                center = Offset(width * 0.25f, height * 0.25f),
                radius = (width * 0.55f) * scale1
            )

            // Orb 2: Cyan / Electric Blue Glow (Bottom Right)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        CyanGlow.copy(alpha = 0.45f),
                        PurpleGlow.copy(alpha = 0.2f),
                        Color.Transparent
                    ),
                    center = Offset(width * 0.75f, height * 0.75f),
                    radius = (width * 0.6f) * scale2
                ),
                center = Offset(width * 0.75f, height * 0.75f),
                radius = (width * 0.6f) * scale2
            )
        }
    }
}
