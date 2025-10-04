package com.example.libros

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.libros.databinding.ActivityMainBinding
import com.example.libros.model.Libro
import com.example.libros.model.SearchResponse
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: BookAdapter
    private val viewModel: BooksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializamos adapter con click
        adapter = BookAdapter(emptyList()) { libro ->
            Log.d("CLICK", "Clickeaste: ${libro.title}")
            // acá podrías abrir una nueva Activity con detalle
        }

        binding.rvBooks.layoutManager = LinearLayoutManager(this)
        binding.rvBooks.adapter = adapter

        // Observamos cambios del ViewModel
        viewModel.books.observe(this) { libros: List<Libro> ->
            adapter.updateData(libros)
        }

        // Búsqueda inicial
        fetchBooks("harry potter")

        // Botón de búsqueda
        binding.btnSearch.setOnClickListener {
            val query = binding.etSearch.text.toString().trim()
            if (query.isNotEmpty()) {
                fetchBooks(query)
            }
        }
    }

    private fun fetchBooks(query: String) {
        lifecycleScope.launch {
            try {
                val response: SearchResponse = RetrofitInstance.api.searchBooks(query)
                val libros = response.docs ?: emptyList()

                Log.d("API_TEST", "Encontrados: ${libros.size} libros")

                viewModel.setBooks(libros)

            } catch (e: Exception) {
                Log.e("API_TEST", "Error al buscar libros: ${e.message}")
            }
        }
    }
}
