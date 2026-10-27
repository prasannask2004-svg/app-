package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.PlaylistPlay
import androidx.compose.material.icons.rounded.QueueMusic
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Videocam
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.model.GeminiAiTab
import com.example.model.MediaAlbum
import com.example.model.MediaType
import com.example.model.MediaTrack
import com.example.ui.components.AlbumCard
import com.example.ui.components.ArtistCard
import com.example.ui.components.AuthModal
import com.example.ui.components.CreatePlaylistModal
import com.example.ui.components.DolbyBadge
import com.example.ui.components.GeminiModal
import com.example.ui.components.GlassCard
import com.example.ui.components.GlowingOrbsBackground
import com.example.ui.components.MiniPlayerBar
import com.example.ui.components.SleepTimerModal
import com.example.ui.theme.CosmicDarkSurface
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.GlassBorderColor
import com.example.ui.theme.GlassBorderHighlight
import com.example.ui.theme.PinkAccent
import com.example.ui.theme.PurpleGlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: PlayerViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val userProfile = uiState.userProfile
    val context = LocalContext.current
    val safeInsets = WindowInsets.safeDrawing.asPaddingValues()

    val authSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val sleepSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val playlistSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val geminiSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
    ) {
        GlowingOrbsBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = safeInsets.calculateTopPadding())
        ) {
            // Top Bar & Search Header
            HomeTopHeader(
                userName = userProfile.name,
                isLoggedIn = userProfile.isLoggedIn,
                avatarUrl = userProfile.avatarUrl,
                searchQuery = uiState.searchQuery,
                selectedLanguage = uiState.selectedHomeLanguage,
                isDolbyEnabled = uiState.isDolbyAtmosEnabled,
                onProfileClick = { viewModel.toggleProfileScreen(true) },
                onSearchChange = { viewModel.setSearchQuery(it) },
                onLanguageSelect = { viewModel.setSelectedHomeLanguage(it) },
                onToggleDolby = { viewModel.toggleDolbyAtmos() },
                onOpenDrawer = { viewModel.togglePlaylistDrawer(true) },
                onOpenGemini = { viewModel.openGeminiModal() }
            )

            // Scrollable Content
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 90.dp)
            ) {
                // Section 1: Top Composers & Artists Carousel
                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    SectionHeader(title = "Legendary Composers & Artists 🎤", subtitle = "A.R. Rahman, Anirudh, Deva, Sirpy, Imman & more")
                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp)
                    ) {
                        items(uiState.artists) { artist ->
                            val isSelected = uiState.selectedArtistId == artist.id
                            ArtistCard(
                                artist = artist,
                                isSelected = isSelected,
                                onClick = { viewModel.selectArtistFilter(artist.id) }
                            )
                        }
                    }
                }

                // Gemini AI Music Assistant Banner
                item {
                    Spacer(modifier = Modifier.height(18.dp))
                    GeminiFeatureBanner(
                        onOpenGeminiStory = { viewModel.openGeminiModal(GeminiAiTab.SONG_STORY) },
                        onOpenGeminiMood = { viewModel.openGeminiModal(GeminiAiTab.MOOD_DJ) },
                        currentTrackTitle = uiState.currentTrack?.title
                    )
                }

                // Section 2: User Custom Playlists
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "My Custom Playlists 🎵",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Create and organize your favorite songs",
                                fontSize = 12.sp,
                                color = Color.White.copy(0.5f)
                            )
                        }

                        IconButton(
                            onClick = { viewModel.toggleCreatePlaylistModal(true) },
                            modifier = Modifier
                                .testTag("create_playlist_button")
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(PurpleGlow.copy(0.4f))
                                .border(1.dp, PinkAccent, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Add,
                                contentDescription = "New Playlist",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp)
                    ) {
                        items(uiState.customPlaylists) { playlist ->
                            GlassCard(
                                modifier = Modifier
                                    .width(170.dp)
                                    .height(95.dp)
                                    .clickable { viewModel.togglePlaylistDrawer(true) }
                                    .testTag("custom_playlist_${playlist.id}")
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Rounded.PlaylistPlay,
                                            contentDescription = null,
                                            tint = PinkAccent,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = playlist.name,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            maxLines = 1
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = playlist.description,
                                        fontSize = 11.sp,
                                        color = Color.White.copy(0.6f),
                                        maxLines = 2
                                    )
                                }
                            }
                        }
                    }
                }

                // Section 3: Language-Wise Playlists Carousel
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    SectionHeader(title = "Language-Wise Playlists 🎧", subtitle = "Select your preferred South & Global language")
                    Spacer(modifier = Modifier.height(10.dp))

                    val languages = listOf(
                        Triple("Tamil", "Tamil Hit Vocal Songs", "https://images.unsplash.com/photo-1614613535308-eb5fbd3d2c17?auto=format&fit=crop&w=600&q=80"),
                        Triple("Telugu", "Telugu Mass & Melodies", "https://images.unsplash.com/photo-1508739773434-c26b3d09e071?auto=format&fit=crop&w=600&q=80"),
                        Triple("Kannada", "Kannada Chartbusters", "https://images.unsplash.com/photo-1518609878373-06d740f60d8b?auto=format&fit=crop&w=600&q=80"),
                        Triple("Malayalam", "Malayalam Soul & Rap", "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?auto=format&fit=crop&w=600&q=80"),
                        Triple("Hindi", "Bollywood Top Grooves", "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?auto=format&fit=crop&w=600&q=80"),
                        Triple("English", "International Pop", "https://images.unsplash.com/photo-1506703719100-a0f3a48c0f86?auto=format&fit=crop&w=600&q=80")
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp)
                    ) {
                        items(languages) { (lang, desc, cover) ->
                            LanguagePlaylistCard(
                                language = lang,
                                description = desc,
                                coverUrl = cover,
                                isSelected = uiState.selectedHomeLanguage.equals(lang, ignoreCase = true),
                                onClick = { viewModel.setSelectedHomeLanguage(lang) }
                            )
                        }
                    }
                }

                // Section 4: New Added Albums (All Languages)
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    SectionHeader(title = "New Added Albums 📀", subtitle = "Latest releases with Share & Play options")
                    Spacer(modifier = Modifier.height(12.dp))

                    val filteredAlbums = if (uiState.selectedHomeLanguage == "ALL") {
                        uiState.albums
                    } else {
                        uiState.albums.filter { it.language.equals(uiState.selectedHomeLanguage, ignoreCase = true) }
                    }

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp)
                    ) {
                        items(filteredAlbums) { album ->
                            Column {
                                AlbumCard(
                                    album = album,
                                    onClick = { viewModel.playAlbum(album) }
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.width(140.dp),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    IconButton(
                                        onClick = { viewModel.shareAlbum(context, album) },
                                        modifier = Modifier.size(28.dp).testTag("share_album_${album.id}")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Share,
                                            contentDescription = "Share Album",
                                            tint = Color.White.copy(0.6f),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Section 5: Featured Vocal Songs List
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    SectionHeader(title = "Full Vocal Songs 🔥", subtitle = "No instrumentals • Dolby Atmos Spatial Audio")
                    Spacer(modifier = Modifier.height(12.dp))
                }

                val songsList = uiState.tracks.filter { track ->
                    val matchesQuery = track.title.contains(uiState.searchQuery, ignoreCase = true) ||
                            track.artist.contains(uiState.searchQuery, ignoreCase = true) ||
                            track.album.contains(uiState.searchQuery, ignoreCase = true)

                    val matchesLang = if (uiState.selectedHomeLanguage == "ALL") true
                    else track.language.equals(uiState.selectedHomeLanguage, ignoreCase = true)

                    val matchesArtist = if (uiState.selectedArtistId == null) true
                    else track.artistId == uiState.selectedArtistId

                    matchesQuery && matchesLang && matchesArtist
                }

                items(songsList) { track ->
                    val isCurrent = uiState.currentTrack?.id == track.id
                    val isFavorite = uiState.favoriteTrackIds.contains(track.id)

                    SongRowItem(
                        track = track,
                        isCurrent = isCurrent,
                        isFavorite = isFavorite,
                        onPlay = { viewModel.prepareAndPlay(track, expandFullPlayer = false) },
                        onFavoriteToggle = { viewModel.toggleFavorite(track) },
                        onShare = { viewModel.shareTrack(context, track) },
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)
                    )
                }
            }

            // Bottom Floating Glass Mini Player
            if (uiState.currentTrack != null) {
                MiniPlayerBar(
                    track = uiState.currentTrack,
                    isPlaying = uiState.isPlaying,
                    isShuffleEnabled = uiState.isShuffleEnabled,
                    repeatMode = uiState.repeatMode,
                    onTogglePlayPause = { viewModel.togglePlayPause() },
                    onPlayNext = { viewModel.playNext() },
                    onToggleShuffle = { viewModel.toggleShuffle() },
                    onCycleRepeatMode = { viewModel.cycleRepeatMode() },
                    onExpand = { viewModel.toggleFullPlayer(true) },
                    modifier = Modifier.padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
                )
            }
        }

        // Modals
        if (uiState.isAuthModalOpen) {
            AuthModal(
                sheetState = authSheetState,
                onMobileLogin = { phone, name -> viewModel.loginWithMobile(phone, name) },
                onGoogleLogin = { viewModel.loginWithGoogle() },
                onDismiss = { viewModel.toggleAuthModal(false) }
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

        if (uiState.isCreatePlaylistModalOpen) {
            CreatePlaylistModal(
                sheetState = playlistSheetState,
                onCreatePlaylist = { name, desc -> viewModel.createCustomPlaylist(name, desc) },
                onDismiss = { viewModel.toggleCreatePlaylistModal(false) }
            )
        }

        if (uiState.isGeminiModalOpen) {
            GeminiModal(
                sheetState = geminiSheetState,
                activeTab = uiState.geminiActiveTab,
                aiState = uiState.geminiAiState,
                currentTrack = uiState.currentTrack,
                promptText = uiState.geminiPromptText,
                onTabSelect = { viewModel.setGeminiActiveTab(it) },
                onPromptChange = { viewModel.setGeminiPromptText(it) },
                onSendPrompt = { viewModel.generateMoodDj(it) },
                onRefreshStory = { viewModel.generateTrackStory() },
                onRetry = { viewModel.retryLastGeminiRequest() },
                onDismiss = { viewModel.closeGeminiModal() }
            )
        }
    }
}

@Composable
private fun HomeTopHeader(
    userName: String,
    isLoggedIn: Boolean,
    avatarUrl: String,
    searchQuery: String,
    selectedLanguage: String,
    isDolbyEnabled: Boolean,
    onProfileClick: () -> Unit,
    onSearchChange: (String) -> Unit,
    onLanguageSelect: (String) -> Unit,
    onToggleDolby: () -> Unit,
    onOpenDrawer: () -> Unit,
    onOpenGemini: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(onClick = onProfileClick)
                    .testTag("home_profile_avatar")
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .shadow(8.dp, CircleShape, spotColor = PurpleGlow)
                        .clip(CircleShape)
                        .border(1.5.dp, Brush.horizontalGradient(listOf(PurpleGlow, PinkAccent)), CircleShape)
                ) {
                    AsyncImage(
                        model = avatarUrl,
                        contentDescription = "Profile Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "Hello, $userName",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = if (isLoggedIn) "VIP Member" else "Tap to Create Account",
                        fontSize = 11.sp,
                        color = if (isLoggedIn) PinkAccent else Color.White.copy(0.6f)
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Gemini AI Top Action Button
                IconButton(
                    onClick = onOpenGemini,
                    modifier = Modifier
                        .testTag("home_gemini_button")
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(PurpleGlow, PinkAccent))
                        )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AutoAwesome,
                        contentDescription = "Gemini AI",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                DolbyBadge(
                    isDolbyEnabled = isDolbyEnabled,
                    onToggleDolby = onToggleDolby
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = onOpenDrawer,
                    modifier = Modifier
                        .testTag("home_open_queue_button")
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(CosmicDarkSurface)
                        .border(1.dp, GlassBorderColor, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.QueueMusic,
                        contentDescription = "Queue Drawer",
                        tint = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("home_search_input"),
            placeholder = { Text("Search songs, artists (AR Rahman, Anirudh...)...", color = Color.White.copy(0.4f)) },
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

        Spacer(modifier = Modifier.height(10.dp))

        val langFilters = listOf("ALL", "Tamil", "Telugu", "Kannada", "Malayalam", "Hindi", "English")
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(langFilters) { lang ->
                val isSelected = selectedLanguage.equals(lang, ignoreCase = true)
                FilterChip(
                    selected = isSelected,
                    onClick = { onLanguageSelect(lang) },
                    label = { Text(if (lang == "ALL") "All Languages" else lang, color = if (isSelected) Color.White else Color.White.copy(0.7f)) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = CosmicDarkSurface,
                        selectedContainerColor = PurpleGlow.copy(0.8f)
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
    }
}

@Composable
private fun SectionHeader(title: String, subtitle: String) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = subtitle,
            fontSize = 12.sp,
            color = Color.White.copy(0.5f)
        )
    }
}

@Composable
private fun LanguagePlaylistCard(
    language: String,
    description: String,
    coverUrl: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .width(160.dp)
            .height(140.dp)
            .clickable(onClick = onClick)
            .testTag("lang_card_$language"),
        backgroundColor = if (isSelected) PurpleGlow.copy(0.4f) else CosmicDarkSurface
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = coverUrl,
                contentDescription = language,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(0.85f))
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Text(
                    text = "$language Hits",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = description,
                    fontSize = 10.sp,
                    color = Color.White.copy(0.7f),
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun SongRowItem(
    track: MediaTrack,
    isCurrent: Boolean,
    isFavorite: Boolean,
    onPlay: () -> Unit,
    onFavoriteToggle: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(if (isCurrent) PurpleGlow.copy(0.3f) else CosmicDarkSurface)
            .border(1.dp, if (isCurrent) GlassBorderHighlight else GlassBorderColor, RoundedCornerShape(18.dp))
            .clickable(onClick = onPlay)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = track.coverUrl,
                contentDescription = track.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (track.type == MediaType.VIDEO) Icons.Rounded.Videocam else Icons.Rounded.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = track.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = if (isCurrent) PurpleGlow else Color.White,
                maxLines = 1
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(PurpleGlow.copy(0.35f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(text = track.language, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${track.artist} • ${track.album}",
                    fontSize = 12.sp,
                    color = Color.White.copy(0.6f),
                    maxLines = 1
                )
            }
        }

        // Like / Favorite Button
        IconButton(
            onClick = onFavoriteToggle,
            modifier = Modifier.size(36.dp).testTag("like_song_${track.id}")
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                contentDescription = "Like Song",
                tint = if (isFavorite) PinkAccent else Color.White.copy(0.4f),
                modifier = Modifier.size(20.dp)
            )
        }

        // Share Button
        IconButton(
            onClick = onShare,
            modifier = Modifier.size(36.dp).testTag("share_song_${track.id}")
        ) {
            Icon(
                imageVector = Icons.Rounded.Share,
                contentDescription = "Share Song",
                tint = Color.White.copy(0.5f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun GeminiFeatureBanner(
    onOpenGeminiStory: () -> Unit,
    onOpenGeminiMood: () -> Unit,
    currentTrackTitle: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFF2C1654).copy(alpha = 0.8f),
                        Color(0xFF14244B).copy(alpha = 0.85f),
                        Color(0xFF0F3B4E).copy(alpha = 0.7f)
                    )
                )
            )
            .border(
                1.dp,
                Brush.horizontalGradient(listOf(PurpleGlow, CyanAccent)),
                RoundedCornerShape(20.dp)
            )
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(PinkAccent, PurpleGlow))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AutoAwesome,
                            contentDescription = "Gemini AI",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Gemini AI Music Assistant",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Song stories, ragas, trivia & custom mood playlists",
                            fontSize = 11.sp,
                            color = CyanAccent
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.12f))
                        .clickable(onClick = onOpenGeminiStory)
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                        .testTag("home_gemini_story_chip"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (currentTrackTitle != null) "✨ Analyze \"$currentTrackTitle\"" else "✨ Analyze Current Song",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        maxLines = 1
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(PurpleGlow.copy(alpha = 0.6f), PinkAccent.copy(alpha = 0.6f))
                            )
                        )
                        .clickable(onClick = onOpenGeminiMood)
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                        .testTag("home_gemini_mood_chip"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🎧 Ask Mood DJ",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

