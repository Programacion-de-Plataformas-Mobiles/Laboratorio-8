package com.example.laboratorio8.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecentQueryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuery(query: RecentQueryEntity)

    @Query("SELECT * FROM recent_queries ORDER BY timestamp DESC LIMIT 10")
    suspend fun getRecentQueries(): List<RecentQueryEntity>

    @Query("DELETE FROM recent_queries WHERE `query` NOT IN (SELECT `query` FROM recent_queries ORDER BY timestamp DESC LIMIT 10)")
    suspend fun deleteOldQueries()
}
