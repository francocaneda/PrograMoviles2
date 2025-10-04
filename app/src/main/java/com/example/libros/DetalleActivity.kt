package com.example.libros

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.libros.databinding.ActivityDetalleBinding
import com.example.libros.model.BookDetail
import kotlinx.coroutines.launch

class DetalleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val workId = intent.getStringExtra("WORK_ID")
        if (workId != null) {
            fetchBookDetail(workId)
        } else {
            Log.e("DETALLE", "No se recibió WORK_ID")
        }
    }

    private fun fetchBookDetail(workId: String) {
        lifecycleScope.launch {
            try {
                val detail: BookDetail = RetrofitInstance.api.getBookDetail(workId)

                binding.tvTitleDetail.text = detail.title ?: "Sin título"

                val authorsText = detail.authors?.joinToString(", ") { author ->
                    author.author?.name ?: "Desconocido"
                } ?: "Desconocido"

                binding.tvAuthorDetail.text = "Autor/es: $authorsText"

                val descriptionText = when (detail.description) {
                    is String -> detail.description
                    is Map<*, *> -> detail.description["value"]?.toString() ?: "Sin descripción"
                    else -> "Sin descripción"
                }
                binding.tvDescriptionDetail.text = descriptionText

                val meta = buildString {
                    append("Fecha de publicación: ${detail.first_publish_date ?: "?"}\n")
                    append("Editorial: ${detail.publishers?.joinToString() ?: "?"}\n")
                    append("Páginas: ${detail.number_of_pages ?: "?"}\n")
                    append("Idiomas: ${detail.languages?.joinToString { it.key ?: "" } ?: "?"}\n")
                    append("Temas: ${detail.subjects?.joinToString() ?: "?"}")
                }
                binding.tvMetaDetail.text = meta

                if (!detail.covers.isNullOrEmpty()) {
                    val coverId = detail.covers[0]
                    val url = "https://covers.openlibrary.org/b/id/${coverId}-L.jpg"
                    binding.ivCoverDetail.load(url)
                } else {
                    binding.ivCoverDetail.setImageResource(R.drawable.ic_book_placeholder)
                }

            } catch (e: Exception) {
                Log.e("DETALLE", "Error al cargar detalle: ${e.message}")
            }
        }
    }
}
