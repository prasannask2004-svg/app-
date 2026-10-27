package com.example.model

data class CustomPlaylist(
    val id: String,
    val name: String,
    val description: String = "User Custom Glass Playlist",
    val trackIds: List<String> = emptyList(),
    val coverUrl: String = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?auto=format&fit=crop&w=600&q=80",
    val createdAt: Long = System.currentTimeMillis()
)
