package com.example.libros.model

import com.google.gson.annotations.SerializedName

data class BookDetail(
    val title: String?,
    val description: Any?,

    @SerializedName("first_publish_date")
    val firstPublishDate: String?,

    val publishers: Any?, // Ya era Any?

    @SerializedName("number_of_pages")
    val numberOfPages: Int?,

    val subjects: List<String>?,
    val covers: List<Int>?,

    // ⭐ CAMBIO CLAVE: Autores también a Any?
    val authors: Any?,

    val languages: Any? // Ya era Any?
)

// !!! ELIMINA las clases AuthorContainer y AuthorInfo de este archivo !!!