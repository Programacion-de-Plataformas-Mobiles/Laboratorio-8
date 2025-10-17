package com.example.laboratorio8.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotos(photos: List<PhotoEntity>)

    @Query("SELECT * FROM photos WHERE `query` = :query ORDER BY savedAt ASC")
    suspend fun getPhotosByQuery(query: String): List<PhotoEntity>

    @Query("SELECT * FROM photos WHERE id = :photoId")
    suspend fun getPhotoById(photoId: String): PhotoEntity?

    @Query("UPDATE photos SET isFavorite = :isFavorite WHERE id = :photoId")
    suspend fun setFavorite(photoId: String, isFavorite: Boolean)

    @Query("SELECT * FROM photos WHERE isFavorite = 1")
    suspend fun getFavoritePhotos(): List<PhotoEntity>
}
