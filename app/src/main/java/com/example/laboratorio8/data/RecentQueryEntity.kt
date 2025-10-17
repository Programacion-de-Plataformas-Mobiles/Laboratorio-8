package com.example.laboratorio8.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_queries")
data class RecentQueryEntity(
    @PrimaryKey val query: String,
    val timestamp: Long = System.currentTimeMillis()
)