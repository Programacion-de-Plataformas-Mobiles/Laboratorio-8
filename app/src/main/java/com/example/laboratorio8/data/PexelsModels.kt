package com.example.laboratorio8.data

import com.google.gson.annotations.SerializedName

// Representa la respuesta completa de la búsqueda
data class PexelsSearchResponse(
    val photos: List<PexelsPhoto>
)

// Representa un único objeto de foto de Pexels
data class PexelsPhoto(
    val id: Int, // Pexels usa Int para el ID
    val photographer: String,
    @SerializedName("src") val sources: PexelsPhotoSources
)

// Representa las diferentes URLs de imagen
data class PexelsPhotoSources(
    @SerializedName("large") val large: String, // URL para vista grande/detalle
    @SerializedName("medium") val medium: String,  // URL ideal para la grilla
)