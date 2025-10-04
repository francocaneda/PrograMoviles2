package com.example.libros.model

data class BookDetail(
    val title: String?,
    val description: Any?, // Puede ser String o Map
    val first_publish_date: String?,
    val publishers: List<String>?,
    val number_of_pages: Int?,
    val subjects: List<String>?,
    val covers: List<Int>?,
    val authors: List<Author>?,
    val languages: List<Language>?
)

data class Author(
    val author: InnerAuthor?
)

data class InnerAuthor(
    val key: String?,
    val name: String?
)

data class Language(
    val key: String?
)
