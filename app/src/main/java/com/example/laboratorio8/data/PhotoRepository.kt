package com.example.laboratorio8.data

import android.util.Log

class PhotoRepository(private val photoDao: PhotoDao, private val recentQueryDao: RecentQueryDao) {

    private val apiService = RetrofitClient.instance

    suspend fun getPhotos(query: String, isNetworkAvailable: Boolean): List<PhotoEntity> {
        if (isNetworkAvailable) {
            try {
                val response = apiService.searchPhotos(RetrofitClient.API_KEY, query)
                if (response.isSuccessful && response.body() != null) {
                    val photosFromApi = response.body()!!.photos
                    val photoEntities = photosFromApi.map { it.toPhotoEntity(query) }
                    photoDao.insertPhotos(photoEntities)
                }
            } catch (e: Exception) {
                Log.e("PhotoRepository", "Error fetching photos from network", e)
            }
        }
        return photoDao.getPhotosByQuery(query)
    }

    suspend fun getPhotoById(photoId: String, isNetworkAvailable: Boolean): PhotoEntity? {
        var photo = photoDao.getPhotoById(photoId)
        if (photo == null && isNetworkAvailable) {
            try {
                val response = apiService.getPhoto(RetrofitClient.API_KEY, photoId)
                if (response.isSuccessful && response.body() != null) {
                    val photoFromApi = response.body()!!
                    val photoEntity = photoFromApi.toPhotoEntity("details") // Assign a default query key
                    photoDao.insertPhotos(listOf(photoEntity))
                    photo = photoEntity
                }
            } catch (e: Exception) {
                Log.e("PhotoRepository", "Error fetching single photo from network", e)
            }
        }
        return photo
    }

    suspend fun toggleFavorite(photoId: String, isFavorite: Boolean) {
        photoDao.setFavorite(photoId, isFavorite)
    }

    suspend fun getRecentQueries(): List<String> {
        return recentQueryDao.getRecentQueries().map { it.query }
    }

    suspend fun addRecentQuery(query: String) {
        recentQueryDao.insertQuery(RecentQueryEntity(query = query))
        recentQueryDao.deleteOldQueries()
    }
}

fun PexelsPhoto.toPhotoEntity(query: String): PhotoEntity {
    return PhotoEntity(
        id = this.id.toString(),
        query = query,
        imageUrlSmall = this.sources.medium,
        imageUrlLarge = this.sources.large,
        photographer = this.photographer,
        isFavorite = false
    )
}
