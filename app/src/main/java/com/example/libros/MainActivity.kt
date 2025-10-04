package com.example.libros

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.libros.databinding.ActivityMainBinding
import com.example.libros.model.Libro

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: BookAdapter
    private val viewModel: BooksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = BookAdapter(emptyList()) { libro: Libro ->
            val key = libro.key
            if (key != null) {
                val workId = key.substringAfter("/works/")
                val intent = Intent(this, DetalleActivity::class.java)
                intent.putExtra("WORK_ID", workId)
                startActivity(intent)
            } else {
                Log.e("MAIN_ACTIVITY", "No se puede abrir detalle: libro.key es null para '${libro.title}'")
                Toast.makeText(this, "No se puede abrir el detalle de este libro", Toast.LENGTH_SHORT).show()
            }
        }

        binding.rvBooks.layoutManager = LinearLayoutManager(this)
        binding.rvBooks.adapter = adapter

        viewModel.books.observe(this) { libros ->
            adapter.updateData(libros)
        }

        viewModel.fetchBooks("harry potter")

        binding.btnSearch.setOnClickListener {
            val query = binding.etSearch.text.toString().trim()
            if (query.isNotEmpty()) {
                viewModel.fetchBooks(query)
            }
        }
    }
}
