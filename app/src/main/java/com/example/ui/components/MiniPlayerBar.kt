package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.RepeatOne
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.MediaTrack
import com.example.model.RepeatMode
import com.example.ui.theme.CosmicDarkBackground
import com.example.ui.theme.GlassBorderColor
import com.example.ui.theme.GlassBorderHighlight
import com.example.ui.theme.PinkAccent
import com.example.ui.theme.PurpleGlow
import com.example.ui.theme.RepeatActiveColor
import com.example.ui.theme.ShuffleActiveColor

@Composable
fun MiniPlayerBar(
    track: MediaTrack?,
    isPlaying: Boolean,
    isShuffleEnabled: Boolean,
    repeatMode: RepeatMode,
    onTogglePlayPause: () -> Unit,
    onPlayNext: () -> Unit,
    onToggleShuffle: () -> Unit,
    onCycleRepeatMode: () -> Unit,
    onExpand: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (track == null) return

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .shadow(20.dp, RoundedCornerShape(24.dp), spotColor = PurpleGlow)
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = Brush.horizontalGradient(
                    listOf(
                        CosmicDarkBackground.copy(alpha = 0.95f),
                        PurpleGlow.copy(alpha = 0.35f),
                        CosmicDarkBackground.copy(alpha = 0.95f)
                    )
                )
            )
            .border(1.dp, GlassBorderHighlight, RoundedCornerShape(24.dp))
            .clickable(onClick = onExpand)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Track Cover Image
            AsyncImage(
                model = track.coverUrl,
                contentDescription = track.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, GlassBorderColor, RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(10.dp))

            // Track Title & Artist
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = track.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1
                )
                Text(
                    text = "${track.language} • ${track.artist}",
                    fontSize = 11.sp,
                    color = Color.White.copy(0.6f),
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Controls: Shuffle, Play/Pause, Next, Repeat
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Shuffle Toggle Button
                val shuffleTint by animateColorAsState(
                    targetValue = if (isShuffleEnabled) ShuffleActiveColor else Color.White.copy(alpha = 0.4f),
                    label = "shuffleTint"
                )
                IconButton(
                    onClick = onToggleShuffle,
                    modifier = Modifier.size(36.dp).testTag("mini_shuffle_button")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Shuffle,
                        contentDescription = "Shuffle",
                        tint = shuffleTint,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Play / Pause Button
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(
                            if (isPlaying) Brush.linearGradient(listOf(PurpleGlow, PinkAccent))
                            else Brush.linearGradient(listOf(Color.White.copy(0.2f), Color.White.copy(0.1f)))
                        )
                        .clickable(onClick = onTogglePlayPause),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Next Button
                IconButton(
                    onClick = onPlayNext,
                    modifier = Modifier.size(36.dp).testTag("mini_next_button")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.SkipNext,
                        contentDescription = "Next Track",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Repeat Toggle Button
                val repeatActive = repeatMode != RepeatMode.OFF
                val repeatTint by animateColorAsState(
                    targetValue = if (repeatActive) RepeatActiveColor else Color.White.copy(alpha = 0.4f),
                    label = "repeatTint"
                )
                IconButton(
                    onClick = onCycleRepeatMode,
                    modifier = Modifier.size(36.dp).testTag("mini_repeat_button")
                ) {
                    Icon(
                        imageVector = if (repeatMode == RepeatMode.ONE) Icons.Rounded.RepeatOne else Icons.Rounded.Repeat,
                        contentDescription = "Repeat",
                        tint = repeatTint,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
