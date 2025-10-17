package com.example.laboratorio8.data

import com.google.gson.annotations.SerializedName

// Representa la respuesta completa de la búsqueda
data class PexelsSearchResponse(
    val photos: List<PexelsPhoto>
)

// Representa un único objeto de foto de Pexels
data class PexelsPhoto(
    val id: Int,
    val photographer: String,
    @SerializedName("photographer_url") val photographerUrl: String,
    @SerializedName("src") val sources: PexelsPhotoSources
)

// Representa las diferentes URLs de imagen
data class PexelsPhotoSources(
    @SerializedName("large") val large: String,
    @SerializedName("medium") val medium: String
)
