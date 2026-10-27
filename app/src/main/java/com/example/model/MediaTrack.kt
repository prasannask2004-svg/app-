package com.example.model

enum class MediaType {
    AUDIO,
    VIDEO
}

enum class RepeatMode {
    OFF,    // Advance to next; stop at end of playlist
    ALL,    // Repeat entire playlist continuously
    ONE     // Loop current track endlessly
}

data class MediaTrack(
    val id: String,
    val title: String,
    val artist: String,
    val artistId: String = "art_ar_rahman",
    val album: String = "Single",
    val language: String = "Tamil", // Tamil, Telugu, Kannada, Malayalam, English
    val type: MediaType,
    val mediaUrl: String,
    val coverUrl: String,
    val durationMs: Long = 180000L,
    val genre: String = "Film Pop",
    val isDolbyAtmosSupported: Boolean = true,
    val isFavorite: Boolean = false
)
