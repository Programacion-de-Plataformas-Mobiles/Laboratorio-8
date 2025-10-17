package com.example.laboratorio8.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://api.pexels.com/"

    // REEMPLAZA "TU_API_KEY_DE_PEXELS" con tu clave real
    const val API_KEY = "TU_API_KEY_DE_PEXELS"

    val instance: PexelsApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(PexelsApiService::class.java)
    }
}