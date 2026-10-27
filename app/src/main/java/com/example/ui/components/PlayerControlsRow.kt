package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.RepeatOne
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.RepeatMode
import com.example.ui.theme.GlassBorderHighlight
import com.example.ui.theme.PinkAccent
import com.example.ui.theme.PurpleGlow
import com.example.ui.theme.RepeatActiveColor
import com.example.ui.theme.ShuffleActiveColor

@Composable
fun PlayerControlsRow(
    isPlaying: Boolean,
    isBuffering: Boolean,
    isShuffleEnabled: Boolean,
    repeatMode: RepeatMode,
    isSleepTimerActive: Boolean,
    onTogglePlayPause: () -> Unit,
    onPlayNext: () -> Unit,
    onPlayPrevious: () -> Unit,
    onToggleShuffle: () -> Unit,
    onCycleRepeatMode: () -> Unit,
    onOpenSleepTimer: () -> Unit,
    onShareTrack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Shuffle Button
            val shuffleTint by animateColorAsState(
                targetValue = if (isShuffleEnabled) ShuffleActiveColor else Color.White.copy(alpha = 0.5f),
                animationSpec = spring(),
                label = "shuffleTint"
            )
            IconButton(
                onClick = onToggleShuffle,
                modifier = Modifier
                    .testTag("shuffle_button")
                    .size(42.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Rounded.Shuffle,
                        contentDescription = "Toggle Shuffle Mode",
                        tint = shuffleTint,
                        modifier = Modifier.size(22.dp)
                    )
                    if (isShuffleEnabled) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(ShuffleActiveColor)
                        )
                    }
                }
            }

            // Previous Track Button
            IconButton(
                onClick = onPlayPrevious,
                modifier = Modifier
                    .testTag("prev_button")
                    .size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.SkipPrevious,
                    contentDescription = "Play Previous Track",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }

            // Main Play / Pause Glass Circle
            val playButtonBrush = if (isPlaying) {
                Brush.linearGradient(listOf(PurpleGlow, PinkAccent))
            } else {
                Brush.linearGradient(listOf(Color.White.copy(alpha = 0.25f), Color.White.copy(alpha = 0.12f)))
            }

            Box(
                modifier = Modifier
                    .testTag("play_pause_button")
                    .size(62.dp)
                    .shadow(elevation = 16.dp, shape = CircleShape, spotColor = PurpleGlow)
                    .clip(CircleShape)
                    .background(playButtonBrush)
                    .border(width = 1.dp, brush = Brush.verticalGradient(listOf(GlassBorderHighlight, Color.White.copy(0.2f))), shape = CircleShape)
                    .clickable(onClick = onTogglePlayPause),
                contentAlignment = Alignment.Center
            ) {
                if (isBuffering) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(28.dp),
                        color = Color.White,
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Icon(
                        imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = Color.White,
                        modifier = Modifier.size(34.dp)
                    )
                }
            }

            // Next Track Button
            IconButton(
                onClick = onPlayNext,
                modifier = Modifier
                    .testTag("next_button")
                    .size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.SkipNext,
                    contentDescription = "Play Next Track",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }

            // Repeat Mode Button
            val repeatActive = repeatMode != RepeatMode.OFF
            val repeatTint by animateColorAsState(
                targetValue = if (repeatActive) RepeatActiveColor else Color.White.copy(alpha = 0.5f),
                animationSpec = spring(),
                label = "repeatTint"
            )

            IconButton(
                onClick = onCycleRepeatMode,
                modifier = Modifier
                    .testTag("repeat_button")
                    .size(42.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (repeatMode == RepeatMode.ONE) Icons.Rounded.RepeatOne else Icons.Rounded.Repeat,
                        contentDescription = "Cycle Repeat Mode",
                        tint = repeatTint,
                        modifier = Modifier.size(22.dp)
                    )
                    if (repeatActive) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(RepeatActiveColor)
                        )
                    }
                }
            }

            // Sleep Timer Action Button
            IconButton(
                onClick = onOpenSleepTimer,
                modifier = Modifier
                    .testTag("sleep_timer_button")
                    .size(42.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Timer,
                    contentDescription = "Sleep Timer",
                    tint = if (isSleepTimerActive) PinkAccent else Color.White.copy(0.5f),
                    modifier = Modifier.size(22.dp)
                )
            }

            // Share Track Button
            IconButton(
                onClick = onShareTrack,
                modifier = Modifier
                    .testTag("share_track_button")
                    .size(42.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Share,
                    contentDescription = "Share Track",
                    tint = Color.White.copy(0.7f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
