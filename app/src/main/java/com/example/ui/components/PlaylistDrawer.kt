package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Videocam
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.MediaType
import com.example.model.MediaTrack
import com.example.ui.theme.CosmicDarkBackground
import com.example.ui.theme.CosmicDarkSurface
import com.example.ui.theme.GlassBorderColor
import com.example.ui.theme.GlassBorderHighlight
import com.example.ui.theme.PinkAccent
import com.example.ui.theme.PurpleGlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistDrawer(
    sheetState: SheetState,
    tracks: List<MediaTrack>,
    currentTrack: MediaTrack?,
    favoriteTrackIds: Set<String>,
    searchQuery: String,
    selectedFilter: String,
    onSearchQueryChange: (String) -> Unit,
    onFilterSelect: (String) -> Unit,
    onTrackSelect: (MediaTrack) -> Unit,
    onFavoriteToggle: (MediaTrack) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = CosmicDarkBackground.copy(alpha = 0.94f),
        scrimColor = Color.Black.copy(alpha = 0.65f),
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .width(44.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.3f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "South Songs Playlist",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                IconButton(onClick = onDismiss, modifier = Modifier.testTag("close_drawer_button")) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Close Playlist Queue",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("playlist_search_input"),
                placeholder = { Text("Search song, artist, album, language...", color = Color.White.copy(0.4f)) },
                leadingIcon = {
                    Icon(imageVector = Icons.Rounded.Search, contentDescription = null, tint = Color.White.copy(0.6f))
                },
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CosmicDarkSurface,
                    unfocusedContainerColor = CosmicDarkSurface,
                    focusedBorderColor = PurpleGlow,
                    unfocusedBorderColor = GlassBorderColor,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Language & Category Filter Chips
            val filters = listOf(
                "ALL" to "All Songs",
                "TAMIL" to "Tamil 🎵",
                "TELUGU" to "Telugu 🎶",
                "KANNADA" to "Kannada 🎸",
                "MALAYALAM" to "Malayalam 🎙️",
                "VIDEO" to "Videos 🎬",
                "FAVORITES" to "Favorites ❤️"
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filters) { (key, label) ->
                    val isSelected = selectedFilter == key
                    FilterChip(
                        selected = isSelected,
                        onClick = { onFilterSelect(key) },
                        label = { Text(label, color = if (isSelected) Color.White else Color.White.copy(0.7f)) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = CosmicDarkSurface,
                            selectedContainerColor = PurpleGlow.copy(alpha = 0.7f)
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = GlassBorderColor,
                            selectedBorderColor = GlassBorderHighlight,
                            enabled = true,
                            selected = isSelected
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Filtered Track List
            val filteredTracks = tracks.filter { track ->
                val matchesQuery = track.title.contains(searchQuery, ignoreCase = true) ||
                        track.artist.contains(searchQuery, ignoreCase = true) ||
                        track.album.contains(searchQuery, ignoreCase = true) ||
                        track.language.contains(searchQuery, ignoreCase = true) ||
                        track.genre.contains(searchQuery, ignoreCase = true)

                val matchesCategory = when (selectedFilter) {
                    "TAMIL" -> track.language.equals("Tamil", ignoreCase = true)
                    "TELUGU" -> track.language.equals("Telugu", ignoreCase = true)
                    "KANNADA" -> track.language.equals("Kannada", ignoreCase = true)
                    "MALAYALAM" -> track.language.equals("Malayalam", ignoreCase = true)
                    "VIDEO" -> track.type == MediaType.VIDEO
                    "FAVORITES" -> favoriteTrackIds.contains(track.id)
                    else -> true
                }

                matchesQuery && matchesCategory
            }

            if (filteredTracks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No songs found for selected filter.",
                        color = Color.White.copy(0.5f),
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(filteredTracks) { track ->
                        val isCurrent = currentTrack?.id == track.id
                        val isFav = favoriteTrackIds.contains(track.id)

                        TrackListItem(
                            track = track,
                            isCurrent = isCurrent,
                            isFavorite = isFav,
                            onSelect = { onTrackSelect(track) },
                            onFavoriteToggle = { onFavoriteToggle(track) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TrackListItem(
    track: MediaTrack,
    isCurrent: Boolean,
    isFavorite: Boolean,
    onSelect: () -> Unit,
    onFavoriteToggle: () -> Unit
) {
    val backgroundBrush = if (isCurrent) {
        Brush.horizontalGradient(listOf(PurpleGlow.copy(alpha = 0.35f), CosmicDarkSurface))
    } else {
        Brush.horizontalGradient(listOf(CosmicDarkSurface, CosmicDarkSurface))
    }

    val borderBrush = if (isCurrent) {
        Brush.horizontalGradient(listOf(GlassBorderHighlight, PurpleGlow))
    } else {
        Brush.horizontalGradient(listOf(GlassBorderColor, GlassBorderColor))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundBrush)
            .border(width = 1.dp, brush = borderBrush, shape = RoundedCornerShape(20.dp))
            .clickable(onClick = onSelect)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Thumbnail Image
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.Black.copy(0.4f)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = track.coverUrl,
                contentDescription = track.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
            if (track.type == MediaType.VIDEO) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(2.dp)
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(PinkAccent),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Videocam,
                        contentDescription = "Video",
                        tint = Color.White,
                        modifier = Modifier.size(10.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Title & Artist & Language Tag
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = track.title,
                fontSize = 15.sp,
                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                color = if (isCurrent) PurpleGlow else Color.White,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Language Tag Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(PurpleGlow.copy(0.3f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = track.language,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${track.artist} • ${track.album}",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.6f),
                    maxLines = 1
                )
            }
        }

        // Heart Favorite Toggle
        IconButton(onClick = onFavoriteToggle) {
            Icon(
                imageVector = if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                contentDescription = if (isFavorite) "Remove Favorite" else "Add Favorite",
                tint = if (isFavorite) PinkAccent else Color.White.copy(alpha = 0.4f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
