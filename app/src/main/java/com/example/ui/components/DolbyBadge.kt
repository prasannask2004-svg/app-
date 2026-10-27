package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SurroundSound
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GlassBorderColor
import com.example.ui.theme.PinkAccent
import com.example.ui.theme.PurpleGlow

@Composable
fun DolbyBadge(
    isDolbyEnabled: Boolean,
    onToggleDolby: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isDolbyEnabled) PurpleGlow.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.4f),
        label = "dolbyBg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isDolbyEnabled) PinkAccent else GlassBorderColor,
        label = "dolbyBorder"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(onClick = onToggleDolby)
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .testTag("dolby_atmos_toggle_badge")
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Rounded.SurroundSound,
                contentDescription = "Dolby Atmos",
                tint = if (isDolbyEnabled) PinkAccent else Color.White.copy(0.6f),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = if (isDolbyEnabled) "Dolby Atmos Spatial ON" else "Dolby Atmos OFF",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDolbyEnabled) Color.White else Color.White.copy(0.6f)
            )
        }
    }
}
