package com.example.laboratorio8.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoEntity(
    @PrimaryKey val id: String,
    val query: String,
    val imageUrlSmall: String,
    val imageUrlLarge: String,
    val photographer: String,
    val authorUrl: String, // New field
    var isFavorite: Boolean = false,
    val savedAt: Long = System.currentTimeMillis()
)
