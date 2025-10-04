package com.example.libros.model

import com.google.gson.annotations.SerializedName

data class Libro(
    val title: String?,
    val author_name: List<String>?,
    val first_publish_year: Int?,
    val cover_i: Int?,
    @SerializedName("key") val key: String? // Esto es crucial para abrir DetalleActivity
)

// La respuesta completa de la búsqueda
data class SearchResponse(
    val numFound: Int?,
    val start: Int?,
    val docs: List<Libro>?
)
