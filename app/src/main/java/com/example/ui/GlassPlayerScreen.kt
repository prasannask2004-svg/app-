package com.example.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode as AnimRepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.QueueMusic
import androidx.compose.material.icons.rounded.VolumeDown
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.MediaType
import com.example.model.MediaTrack
import com.example.ui.components.DolbyBadge
import com.example.ui.components.GlassCard
import com.example.ui.components.GlowingOrbsBackground
import com.example.ui.components.PlayerControlsRow
import com.example.ui.components.PlaylistDrawer
import com.example.ui.components.SleepTimerModal
import com.example.ui.components.VideoPlayerView
import com.example.ui.theme.CosmicDarkSurface
import com.example.ui.theme.GlassBorderColor
import com.example.ui.theme.PinkAccent
import com.example.ui.theme.PurpleGlow
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlassPlayerScreen(
    viewModel: PlayerViewModel,
    onBackToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val safeInsets = WindowInsets.safeDrawing.asPaddingValues()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val sleepSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
    ) {
        GlowingOrbsBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(safeInsets)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                elevation = 24.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Top Bar
                    TopActionBar(
                        activeMediaType = uiState.activeMediaType,
                        onBackToHome = onBackToHome,
                        onModeSelect = { type -> viewModel.setMediaType(type) },
                        onOpenDrawer = { viewModel.togglePlaylistDrawer(true) }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Dolby Atmos Spatial Badge
                    DolbyBadge(
                        isDolbyEnabled = uiState.isDolbyAtmosEnabled,
                        onToggleDolby = { viewModel.toggleDolbyAtmos() }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Media Stage (Album Disc / Video View)
                    MediaStageView(
                        track = uiState.currentTrack,
                        activeMediaType = uiState.activeMediaType,
                        isPlaying = uiState.isPlaying,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Track Title & Like
                    TrackHeader(
                        track = uiState.currentTrack,
                        isFavorite = uiState.favoriteTrackIds.contains(uiState.currentTrack?.id),
                        onFavoriteToggle = { track -> viewModel.toggleFavorite(track) }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Seek Progress Bar
                    SeekProgressSection(
                        currentPositionMs = uiState.currentPositionMs,
                        durationMs = uiState.durationMs,
                        onSeekTo = { pos -> viewModel.seekTo(pos) }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Complete Controls Row (Previous, Play/Pause, Next, Shuffle, Repeat, Sleep Timer, Share)
                    PlayerControlsRow(
                        isPlaying = uiState.isPlaying,
                        isBuffering = uiState.isBuffering,
                        isShuffleEnabled = uiState.isShuffleEnabled,
                        repeatMode = uiState.repeatMode,
                        isSleepTimerActive = uiState.sleepTimerMinutes > 0,
                        onTogglePlayPause = { viewModel.togglePlayPause() },
                        onPlayNext = { viewModel.playNext() },
                        onPlayPrevious = { viewModel.playPrevious() },
                        onToggleShuffle = { viewModel.toggleShuffle() },
                        onCycleRepeatMode = { viewModel.cycleRepeatMode() },
                        onOpenSleepTimer = { viewModel.toggleSleepModal(true) },
                        onShareTrack = {
                            uiState.currentTrack?.let { track -> viewModel.shareTrack(context, track) }
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Volume Control
                    VolumeSection(
                        volume = uiState.volume,
                        onVolumeChange = { vol -> viewModel.setVolume(vol) }
                    )
                }
            }
        }

        // Modals
        if (uiState.isPlaylistDrawerOpen) {
            PlaylistDrawer(
                sheetState = sheetState,
                tracks = uiState.tracks,
                currentTrack = uiState.currentTrack,
                favoriteTrackIds = uiState.favoriteTrackIds,
                searchQuery = uiState.searchQuery,
                selectedFilter = uiState.selectedFilter,
                onSearchQueryChange = { q -> viewModel.setSearchQuery(q) },
                onFilterSelect = { filter -> viewModel.setFilter(filter) },
                onTrackSelect = { track ->
                    viewModel.prepareAndPlay(track)
                    viewModel.togglePlaylistDrawer(false)
                },
                onFavoriteToggle = { track -> viewModel.toggleFavorite(track) },
                onDismiss = { viewModel.togglePlaylistDrawer(false) }
            )
        }

        if (uiState.isSleepModalOpen) {
            SleepTimerModal(
                sheetState = sleepSheetState,
                currentMinutes = uiState.sleepTimerMinutes,
                remainingSeconds = uiState.sleepTimerRemainingSeconds,
                onSetTimerMinutes = { mins -> viewModel.setSleepTimer(mins) },
                onDismiss = { viewModel.toggleSleepModal(false) }
            )
        }
    }
}

@Composable
private fun TopActionBar(
    activeMediaType: MediaType,
    onBackToHome: () -> Unit,
    onModeSelect: (MediaType) -> Unit,
    onOpenDrawer: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackToHome,
            modifier = Modifier
                .testTag("back_to_home_button")
                .size(40.dp)
                .clip(CircleShape)
                .background(CosmicDarkSurface)
                .border(1.dp, GlassBorderColor, CircleShape)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back to Home",
                tint = Color.White
            )
        }

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.35f))
                .border(width = 1.dp, color = GlassBorderColor, shape = CircleShape)
                .padding(3.dp)
        ) {
            Row {
                ModeButton(
                    label = "Songs",
                    isSelected = activeMediaType == MediaType.AUDIO,
                    onClick = { onModeSelect(MediaType.AUDIO) },
                    testTag = "mode_songs_button"
                )
                ModeButton(
                    label = "Videos",
                    isSelected = activeMediaType == MediaType.VIDEO,
                    onClick = { onModeSelect(MediaType.VIDEO) },
                    testTag = "mode_videos_button"
                )
            }
        }

        IconButton(
            onClick = onOpenDrawer,
            modifier = Modifier
                .testTag("open_drawer_button")
                .size(40.dp)
                .clip(CircleShape)
                .background(CosmicDarkSurface)
                .border(1.dp, GlassBorderColor, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Rounded.QueueMusic,
                contentDescription = "Open Playlist Queue",
                tint = Color.White
            )
        }
    }
}

@Composable
private fun ModeButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val backgroundBrush = if (isSelected) {
        Brush.horizontalGradient(listOf(PurpleGlow.copy(alpha = 0.8f), PinkAccent.copy(alpha = 0.7f)))
    } else {
        Brush.horizontalGradient(listOf(Color.Transparent, Color.Transparent))
    }

    Box(
        modifier = Modifier
            .testTag(testTag)
            .clip(CircleShape)
            .background(backgroundBrush)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f)
        )
    }
}

@Composable
private fun MediaStageView(
    track: MediaTrack?,
    activeMediaType: MediaType,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    val rotationTransition = rememberInfiniteTransition(label = "vinyl")
    val rotation by rotationTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = AnimRepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(Color.Black.copy(alpha = 0.4f))
            .border(1.dp, GlassBorderColor, RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (activeMediaType == MediaType.VIDEO && track?.type == MediaType.VIDEO) {
            VideoPlayerView(
                videoUrl = track.mediaUrl,
                isPlaying = isPlaying,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(
                modifier = Modifier
                    .size(165.dp)
                    .shadow(16.dp, CircleShape, spotColor = PurpleGlow)
                    .clip(CircleShape)
                    .rotate(if (isPlaying) rotation else 0f)
                    .border(4.dp, Color.White.copy(0.2f), CircleShape)
            ) {
                AsyncImage(
                    model = track?.coverUrl ?: "https://images.unsplash.com/photo-1614613535308-eb5fbd3d2c17?auto=format&fit=crop&w=600&q=80",
                    contentDescription = "Album Cover",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .align(Alignment.Center)
                        .clip(CircleShape)
                        .background(Color.Black.copy(0.7f))
                        .border(1.5.dp, Color.White.copy(0.4f), CircleShape)
                )
            }
        }
    }
}

@Composable
private fun TrackHeader(
    track: MediaTrack?,
    isFavorite: Boolean,
    onFavoriteToggle: (MediaTrack) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = track?.title ?: "Select Track",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "${track?.language ?: ""} • ${track?.artist ?: "Unknown Artist"} • ${track?.album ?: "Single"}",
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.6f),
                maxLines = 1
            )
        }

        if (track != null) {
            IconButton(
                onClick = { onFavoriteToggle(track) },
                modifier = Modifier.testTag("favorite_toggle_button")
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                    contentDescription = "Toggle Favorite",
                    tint = if (isFavorite) PinkAccent else Color.White.copy(0.5f),
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}

@Composable
private fun SeekProgressSection(
    currentPositionMs: Long,
    durationMs: Long,
    onSeekTo: (Long) -> Unit
) {
    val progress = if (durationMs > 0) {
        (currentPositionMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f)
    } else 0f

    Column(modifier = Modifier.fillMaxWidth()) {
        Slider(
            value = progress,
            onValueChange = { frac ->
                val newPos = (frac * durationMs).toLong()
                onSeekTo(newPos)
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("seek_bar_slider"),
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = PurpleGlow,
                inactiveTrackColor = Color.White.copy(alpha = 0.2f)
            )
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatMs(currentPositionMs),
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.5f)
            )
            Text(
                text = formatMs(durationMs),
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
private fun VolumeSection(
    volume: Float,
    onVolumeChange: (Float) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Rounded.VolumeDown,
            contentDescription = "Volume Low",
            tint = Color.White.copy(alpha = 0.5f),
            modifier = Modifier.size(18.dp)
        )
        Slider(
            value = volume,
            onValueChange = onVolumeChange,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
                .testTag("volume_bar_slider"),
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = PinkAccent.copy(alpha = 0.8f),
                inactiveTrackColor = Color.White.copy(alpha = 0.2f)
            )
        )
        Icon(
            imageVector = Icons.Rounded.VolumeUp,
            contentDescription = "Volume High",
            tint = Color.White.copy(alpha = 0.5f),
            modifier = Modifier.size(18.dp)
        )
    }
}

private fun formatMs(ms: Long): String {
    val totalSeconds = (ms / 1000).toInt()
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format(Locale.US, "%d:%02d", minutes, seconds)
}
