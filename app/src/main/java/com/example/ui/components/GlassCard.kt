package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.CosmicDarkSurface
import com.example.ui.theme.GlassBorderColor
import com.example.ui.theme.GlassBorderHighlight

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(32.dp),
    backgroundColor: Color = CosmicDarkSurface,
    borderColor: Color = GlassBorderColor,
    elevation: Dp = 16.dp,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .shadow(elevation = elevation, shape = shape, spotColor = Color.Black.copy(alpha = 0.5f))
            .clip(shape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        backgroundColor.copy(alpha = 0.15f),
                        backgroundColor.copy(alpha = 0.05f)
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        GlassBorderHighlight,
                        borderColor
                    )
                ),
                shape = shape
            ),
        color = Color.Transparent,
        content = content
    )
}
