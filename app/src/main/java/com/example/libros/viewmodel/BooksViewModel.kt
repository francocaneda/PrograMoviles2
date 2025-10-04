package com.example.libros

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.libros.model.Libro

class BooksViewModel : ViewModel() {

    private val _books = MutableLiveData<List<Libro>>()
    val books: LiveData<List<Libro>> get() = _books

    fun setBooks(list: List<Libro>) {
        _books.value = list
    }
}
