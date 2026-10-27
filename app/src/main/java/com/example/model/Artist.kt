package com.example.model

data class Artist(
    val id: String,
    val name: String,
    val language: String, // Tamil, Telugu, Kannada, Malayalam, etc.
    val photoUrl: String,
    val role: String = "Composer & Singer",
    val songCount: Int = 12
)
