package com.example.libros.model

// Cada libro individual
data class Libro(
    val title: String?,
    val author_name: List<String>?,
    val first_publish_year: Int?,
    val cover_i: Int?
)

// La respuesta completa de la búsqueda
data class SearchResponse(
    val numFound: Int?,
    val start: Int?,
    val docs: List<Libro>?
)
