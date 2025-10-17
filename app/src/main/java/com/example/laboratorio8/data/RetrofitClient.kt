package com.example.laboratorio8.data

import com.example.laboratorio8.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://api.pexels.com/"

    const val API_KEY = BuildConfig.PEXELS_API_KEY

    val instance: PexelsApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(PexelsApiService::class.java)
    }
}
