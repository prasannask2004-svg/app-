package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_tracks")
data class FavoriteTrackEntity(
    @PrimaryKey val id: String,
    val title: String,
    val artist: String,
    val type: String,
    val mediaUrl: String,
    val coverUrl: String,
    val addedAt: Long = System.currentTimeMillis()
)
