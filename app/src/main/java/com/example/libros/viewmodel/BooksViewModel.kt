package com.example.libros

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libros.model.Libro
import com.example.libros.model.SearchResponse
import kotlinx.coroutines.launch

class BooksViewModel : ViewModel() {

    private val _books = MutableLiveData<List<Libro>>()
    val books: LiveData<List<Libro>> get() = _books

    fun setBooks(list: List<Libro>) {
        _books.value = list
    }

    fun fetchBooks(query: String) {
        viewModelScope.launch {
            try {
                val response: SearchResponse = RetrofitInstance.api.searchBooks(query)
                val libros = response.docs ?: emptyList()
                Log.d("VIEWMODEL", "Encontrados ${libros.size} libros para '$query'")
                setBooks(libros)
            } catch (e: Exception) {
                Log.e("VIEWMODEL", "Error al buscar libros: ${e.message}")
            }
        }
    }
}
