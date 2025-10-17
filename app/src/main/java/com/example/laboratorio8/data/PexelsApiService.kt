package com.example.laboratorio8.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface PexelsApiService {
    @GET("v1/search")
    suspend fun searchPhotos(
        @Header("Authorization") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20
    ): Response<PexelsSearchResponse>

    @GET("v1/photos/{id}")
    suspend fun getPhoto(
        @Header("Authorization") apiKey: String,
        @Path("id") photoId: String
    ): Response<PexelsPhoto>
}