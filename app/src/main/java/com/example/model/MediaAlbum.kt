package com.example.model

data class MediaAlbum(
    val id: String,
    val title: String,
    val artist: String,
    val language: String, // Tamil, Telugu, Kannada, Malayalam, etc.
    val coverUrl: String,
    val releaseYear: String = "2024",
    val trackCount: Int = 6,
    val genre: String = "Filmi Pop"
)
