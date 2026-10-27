package com.example.ui

import android.app.Application
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MediaDatabase
import com.example.data.MediaRepository
import com.example.model.Artist
import com.example.model.AuthMethod
import com.example.model.CustomPlaylist
import com.example.model.MediaAlbum
import com.example.model.MediaType
import com.example.model.MediaTrack
import com.example.model.RepeatMode
import com.example.model.UserProfile
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

data class PlayerUiState(
    val tracks: List<MediaTrack> = emptyList(),
    val albums: List<MediaAlbum> = emptyList(),
    val artists: List<Artist> = emptyList(),
    val customPlaylists: List<CustomPlaylist> = emptyList(),
    val currentTrack: MediaTrack? = null,
    val currentIndex: Int = 0,
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val currentPositionMs: Long = 0L,
    val durationMs: Long = 180000L,
    val volume: Float = 0.8f,
    val isShuffleEnabled: Boolean = false,
    val repeatMode: RepeatMode = RepeatMode.OFF,
    val activeMediaType: MediaType = MediaType.AUDIO,
    val favoriteTrackIds: Set<String> = emptySet(),
    val isPlaylistDrawerOpen: Boolean = false,
    val isFullPlayerVisible: Boolean = false,
    val isProfileScreenVisible: Boolean = false,
    val isAuthModalOpen: Boolean = false,
    val isSleepModalOpen: Boolean = false,
    val isCreatePlaylistModalOpen: Boolean = false,
    val isDolbyAtmosEnabled: Boolean = true,
    val sleepTimerMinutes: Int = 0,
    val sleepTimerRemainingSeconds: Int = 0,
    val userProfile: UserProfile = UserProfile(),
    val searchQuery: String = "",
    val selectedFilter: String = "ALL",
    val selectedHomeLanguage: String = "ALL",
    val selectedArtistId: String? = null
)

class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val db = MediaDatabase.getDatabase(application)
    private val repository = MediaRepository(db.favoritesDao())

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    private var mediaPlayer: MediaPlayer? = null
    private var isMediaPlayerPrepared: Boolean = false
    private var progressJob: Job? = null
    private var sleepTimerJob: Job? = null

    init {
        val initialTracks = repository.sampleTracks
        val initialAlbums = repository.newAlbums
        val initialArtists = repository.artists
        val defaultPlaylists = listOf(
            CustomPlaylist(id = "pl_1", name = "A.R. Rahman Hits", description = "Oscar Maestro Vocal Melodies"),
            CustomPlaylist(id = "pl_2", name = "Anirudh Energy Jams", description = "High Power Tamil Pop Beats")
        )
        val firstTrack = initialTracks.firstOrNull()

        _uiState.update {
            it.copy(
                tracks = initialTracks,
                albums = initialAlbums,
                artists = initialArtists,
                customPlaylists = defaultPlaylists,
                currentTrack = firstTrack,
                currentIndex = 0,
                activeMediaType = firstTrack?.type ?: MediaType.AUDIO,
                durationMs = firstTrack?.durationMs ?: 180000L
            )
        }

        viewModelScope.launch {
            repository.getFavoriteIdsFlow().collect { favIds ->
                _uiState.update { it.copy(favoriteTrackIds = favIds) }
            }
        }
    }

    fun setSleepTimer(minutes: Int) {
        sleepTimerJob?.cancel()
        if (minutes <= 0) {
            _uiState.update {
                it.copy(sleepTimerMinutes = 0, sleepTimerRemainingSeconds = 0)
            }
            return
        }

        val totalSeconds = minutes * 60
        _uiState.update {
            it.copy(sleepTimerMinutes = minutes, sleepTimerRemainingSeconds = totalSeconds)
        }

        sleepTimerJob = viewModelScope.launch {
            var remaining = totalSeconds
            while (isActive && remaining > 0) {
                delay(1000)
                remaining -= 1
                _uiState.update { it.copy(sleepTimerRemainingSeconds = remaining) }
            }
            if (remaining <= 0) {
                pause()
                _uiState.update {
                    it.copy(sleepTimerMinutes = 0, sleepTimerRemainingSeconds = 0)
                }
            }
        }
    }

    fun toggleDolbyAtmos() {
        _uiState.update { it.copy(isDolbyAtmosEnabled = !it.isDolbyAtmosEnabled) }
    }

    fun createCustomPlaylist(name: String, description: String) {
        val newPl = CustomPlaylist(
            id = "pl_${System.currentTimeMillis()}",
            name = name,
            description = description
        )
        _uiState.update {
            it.copy(customPlaylists = it.customPlaylists + newPl)
        }
    }

    fun selectArtistFilter(artistId: String?) {
        _uiState.update {
            if (it.selectedArtistId == artistId) {
                it.copy(selectedArtistId = null)
            } else {
                it.copy(selectedArtistId = artistId)
            }
        }
    }

    fun shareTrack(context: Context, track: MediaTrack) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Check out this song on Glass Media Player: ${track.title}")
            putExtra(
                Intent.EXTRA_TEXT,
                "🎵 Listening to '${track.title}' by ${track.artist} (${track.language}) on Glass Media Player! Stream now: ${track.mediaUrl}"
            )
        }
        context.startActivity(Intent.createChooser(intent, "Share Track via"))
    }

    fun shareAlbum(context: Context, album: MediaAlbum) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Check out the album '${album.title}' on Glass Media Player!")
            putExtra(
                Intent.EXTRA_TEXT,
                "📀 Check out the new album '${album.title}' by ${album.artist} (${album.language}) on Glass Media Player!"
            )
        }
        context.startActivity(Intent.createChooser(intent, "Share Album via"))
    }

    fun loginWithMobile(mobile: String, name: String) {
        _uiState.update {
            it.copy(
                userProfile = UserProfile(
                    userId = "user_${System.currentTimeMillis()}",
                    name = name,
                    mobileNumber = mobile,
                    authMethod = AuthMethod.INDIA_MOBILE,
                    isLoggedIn = true,
                    memberStatus = "Verified +91 Mobile Listener",
                    avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&q=80"
                ),
                isAuthModalOpen = false
            )
        }
    }

    fun loginWithGoogle() {
        _uiState.update {
            it.copy(
                userProfile = UserProfile(
                    userId = "google_${System.currentTimeMillis()}",
                    name = "Google Glass Listener",
                    email = "listener@gmail.com",
                    authMethod = AuthMethod.GOOGLE,
                    isLoggedIn = true,
                    memberStatus = "Google Verified VIP",
                    avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=400&q=80"
                ),
                isAuthModalOpen = false
            )
        }
    }

    fun updateProfileName(newName: String) {
        if (newName.trim().isEmpty()) return
        _uiState.update {
            it.copy(userProfile = it.userProfile.copy(name = newName.trim()))
        }
    }

    fun logout() {
        _uiState.update {
            it.copy(
                userProfile = UserProfile(),
                isProfileScreenVisible = false
            )
        }
    }

    fun toggleAuthModal(open: Boolean? = null) {
        _uiState.update {
            it.copy(isAuthModalOpen = open ?: !it.isAuthModalOpen)
        }
    }

    fun toggleSleepModal(open: Boolean? = null) {
        _uiState.update {
            it.copy(isSleepModalOpen = open ?: !it.isSleepModalOpen)
        }
    }

    fun toggleCreatePlaylistModal(open: Boolean? = null) {
        _uiState.update {
            it.copy(isCreatePlaylistModalOpen = open ?: !it.isCreatePlaylistModalOpen)
        }
    }

    fun toggleProfileScreen(visible: Boolean? = null) {
        _uiState.update {
            it.copy(isProfileScreenVisible = visible ?: !it.isProfileScreenVisible)
        }
    }

    fun prepareAndPlay(track: MediaTrack, expandFullPlayer: Boolean = false) {
        val index = _uiState.value.tracks.indexOfFirst { it.id == track.id }
        if (index != -1) {
            _uiState.update {
                it.copy(
                    currentTrack = track,
                    currentIndex = index,
                    activeMediaType = track.type,
                    currentPositionMs = 0L,
                    durationMs = track.durationMs,
                    isFullPlayerVisible = if (expandFullPlayer) true else it.isFullPlayerVisible
                )
            }
        }

        if (track.type == MediaType.AUDIO) {
            startAudioPlayback(track.mediaUrl)
        } else {
            releaseMediaPlayer()
            _uiState.update { it.copy(isPlaying = true, isBuffering = false) }
            startProgressTimer()
        }
    }

    fun playAlbum(album: MediaAlbum) {
        val albumTracks = _uiState.value.tracks.filter {
            it.album.contains(album.title, ignoreCase = true) || it.language.equals(album.language, ignoreCase = true)
        }
        val first = albumTracks.firstOrNull() ?: _uiState.value.tracks.firstOrNull()
        if (first != null) {
            prepareAndPlay(first, expandFullPlayer = true)
        }
    }

    fun toggleFullPlayer(visible: Boolean? = null) {
        _uiState.update {
            it.copy(isFullPlayerVisible = visible ?: !it.isFullPlayerVisible)
        }
    }

    private fun startAudioPlayback(url: String) {
        try {
            releaseMediaPlayer()
            _uiState.update { it.copy(isBuffering = true, isPlaying = false) }
            isMediaPlayerPrepared = false

            val headers = mapOf("User-Agent" to "Mozilla/5.0 (Android; Mobile)")

            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(getApplication(), Uri.parse(url), headers)
                setVolume(_uiState.value.volume, _uiState.value.volume)
                setOnPreparedListener { mp ->
                    isMediaPlayerPrepared = true
                    _uiState.update {
                        it.copy(
                            isPlaying = true,
                            isBuffering = false,
                            durationMs = if (mp.duration > 0) mp.duration.toLong() else it.durationMs
                        )
                    }
                    try {
                        mp.start()
                        startProgressTimer()
                    } catch (e: Exception) {
                        Log.e("PlayerViewModel", "Error starting player in setOnPreparedListener", e)
                    }
                }
                setOnCompletionListener {
                    handleTrackCompletion()
                }
                setOnErrorListener { _, what, extra ->
                    Log.w("PlayerViewModel", "MediaPlayer error what=$what extra=$extra")
                    isMediaPlayerPrepared = false
                    _uiState.update { it.copy(isPlaying = false, isBuffering = false) }
                    true
                }
                prepareAsync()
            }
        } catch (e: Exception) {
            Log.e("PlayerViewModel", "Error initializing audio player", e)
            isMediaPlayerPrepared = false
            _uiState.update { it.copy(isPlaying = false, isBuffering = false) }
        }
    }

    fun togglePlayPause() {
        val current = _uiState.value.currentTrack ?: return
        if (_uiState.value.isPlaying) {
            pause()
        } else {
            resume(current)
        }
    }

    private fun pause() {
        if (isMediaPlayerPrepared && mediaPlayer != null) {
            try {
                if (mediaPlayer?.isPlaying == true) {
                    mediaPlayer?.pause()
                }
            } catch (e: Exception) {
                Log.e("PlayerViewModel", "Error pausing player", e)
            }
        }
        _uiState.update { it.copy(isPlaying = false) }
        stopProgressTimer()
    }

    private fun resume(track: MediaTrack) {
        if (track.type == MediaType.AUDIO) {
            if (mediaPlayer != null && isMediaPlayerPrepared) {
                try {
                    mediaPlayer?.start()
                    _uiState.update { it.copy(isPlaying = true) }
                    startProgressTimer()
                } catch (e: Exception) {
                    Log.e("PlayerViewModel", "Error resuming prepared player", e)
                    startAudioPlayback(track.mediaUrl)
                }
            } else {
                startAudioPlayback(track.mediaUrl)
            }
        } else {
            _uiState.update { it.copy(isPlaying = true) }
            startProgressTimer()
        }
    }

    fun playNext() {
        val state = _uiState.value
        if (state.tracks.isEmpty()) return

        val nextIndex = if (state.isShuffleEnabled) {
            if (state.tracks.size > 1) {
                var randomIdx: Int
                do {
                    randomIdx = Random.nextInt(state.tracks.size)
                } while (randomIdx == state.currentIndex)
                randomIdx
            } else 0
        } else {
            (state.currentIndex + 1) % state.tracks.size
        }

        val nextTrack = state.tracks[nextIndex]
        prepareAndPlay(nextTrack)
    }

    fun playPrevious() {
        val state = _uiState.value
        if (state.tracks.isEmpty()) return

        val prevIndex = if (state.currentIndex > 0) {
            state.currentIndex - 1
        } else {
            state.tracks.size - 1
        }

        val prevTrack = state.tracks[prevIndex]
        prepareAndPlay(prevTrack)
    }

    fun toggleShuffle() {
        _uiState.update { it.copy(isShuffleEnabled = !it.isShuffleEnabled) }
    }

    fun cycleRepeatMode() {
        _uiState.update {
            val nextRepeatMode = when (it.repeatMode) {
                RepeatMode.OFF -> RepeatMode.ALL
                RepeatMode.ALL -> RepeatMode.ONE
                RepeatMode.ONE -> RepeatMode.OFF
            }
            it.copy(repeatMode = nextRepeatMode)
        }
    }

    fun setVolume(newVolume: Float) {
        _uiState.update { it.copy(volume = newVolume.coerceIn(0f, 1f)) }
        if (isMediaPlayerPrepared && mediaPlayer != null) {
            try {
                mediaPlayer?.setVolume(newVolume, newVolume)
            } catch (e: Exception) {
                Log.e("PlayerViewModel", "Error setting volume", e)
            }
        }
    }

    fun seekTo(positionMs: Long) {
        _uiState.update { it.copy(currentPositionMs = positionMs) }
        if (isMediaPlayerPrepared && mediaPlayer != null) {
            try {
                mediaPlayer?.seekTo(positionMs.toInt())
            } catch (e: Exception) {
                Log.e("PlayerViewModel", "Error seeking player", e)
            }
        }
    }

    fun setMediaType(type: MediaType) {
        val state = _uiState.value
        if (state.activeMediaType == type) return

        val matchingTrack = state.tracks.firstOrNull { it.type == type }
            ?: state.tracks.firstOrNull()

        if (matchingTrack != null) {
            prepareAndPlay(matchingTrack)
        }
    }

    fun toggleFavorite(track: MediaTrack) {
        viewModelScope.launch {
            repository.toggleFavorite(track)
        }
    }

    fun togglePlaylistDrawer(open: Boolean? = null) {
        _uiState.update {
            it.copy(isPlaylistDrawerOpen = open ?: !it.isPlaylistDrawerOpen)
        }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setFilter(filter: String) {
        _uiState.update { it.copy(selectedFilter = filter) }
    }

    fun setSelectedHomeLanguage(language: String) {
        _uiState.update { it.copy(selectedHomeLanguage = language, selectedFilter = language) }
    }

    private fun handleTrackCompletion() {
        val state = _uiState.value
        when (state.repeatMode) {
            RepeatMode.ONE -> {
                state.currentTrack?.let { prepareAndPlay(it) }
            }
            RepeatMode.ALL -> {
                playNext()
            }
            RepeatMode.OFF -> {
                if (state.currentIndex < state.tracks.size - 1) {
                    playNext()
                } else {
                    _uiState.update { it.copy(isPlaying = false, currentPositionMs = 0L) }
                    stopProgressTimer()
                }
            }
        }
    }

    private fun startProgressTimer() {
        stopProgressTimer()
        progressJob = viewModelScope.launch {
            while (isActive && _uiState.value.isPlaying) {
                if (isMediaPlayerPrepared) {
                    mediaPlayer?.let { mp ->
                        try {
                            if (mp.isPlaying) {
                                _uiState.update { it.copy(currentPositionMs = mp.currentPosition.toLong()) }
                            }
                        } catch (e: Exception) {
                            Log.e("PlayerViewModel", "Error querying position", e)
                        }
                    }
                }
                delay(500)
            }
        }
    }

    private fun stopProgressTimer() {
        progressJob?.cancel()
        progressJob = null
    }

    private fun releaseMediaPlayer() {
        stopProgressTimer()
        sleepTimerJob?.cancel()
        isMediaPlayerPrepared = false
        try {
            mediaPlayer?.let { mp ->
                if (mp.isPlaying) {
                    mp.stop()
                }
                mp.reset()
                mp.release()
            }
        } catch (e: Exception) {
            Log.e("PlayerViewModel", "Error releasing MediaPlayer", e)
        }
        mediaPlayer = null
    }

    override fun onCleared() {
        super.onCleared()
        releaseMediaPlayer()
    }
}
