package com.example.libros

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.libros.databinding.ActivityDetalleBinding
import com.example.libros.model.BookDetail
import kotlinx.coroutines.launch
import kotlin.collections.Map

class DetalleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVolver.setOnClickListener {
            // Usa el despachador de retroceso, que es la forma moderna de volver a la actividad anterior
            onBackPressedDispatcher.onBackPressed()
        }

        // Configuración de la Toolbar
        setSupportActionBar(binding.toolbarDetalle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Log.d("DETALLE", "DetalleActivity iniciada con éxito.")

        val workId = intent.getStringExtra("WORK_ID")
        if (workId != null) {
            fetchBookDetail(workId)
        } else {
            Log.e("DETALLE", "No se recibió WORK_ID")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun fetchBookDetail(workId: String) {
        lifecycleScope.launch {
            try {
                // Asumimos que BookDetail ahora tiene authors, publishers, languages como Any?
                val detail: BookDetail = RetrofitInstance.api.getBookDetail(workId)
                Log.d("DETALLE", "Detalles recibidos para: ${detail.title}")

                binding.tvTitleDetail.text = detail.title ?: "Sin título"

                // 1. Manejo de Autor: Manejo manual de Any? para List<Map<"author", Map<"name", String>>>
                val authorsText = when (val auths = detail.authors) {
                    is List<*> -> {
                        auths.joinToString(", ") {
                            // Accede a la clave "author" del primer mapa, y luego a la clave "name" del segundo mapa.
                            val authorMap = (it as? Map<*, *>)?.get("author") as? Map<*, *>
                            authorMap?.get("name")?.toString() ?: ""
                        }
                    }
                    else -> "Desconocido"
                }.takeIf { it.isNotBlank() } ?: "Desconocido"
                binding.tvAuthorDetail.text = "Autor/es: $authorsText"

                // 2. Manejo de la Descripción (String o Map)
                val descriptionText = when (detail.description) {
                    is String -> detail.description
                    is Map<*, *> -> detail.description["value"]?.toString() ?: "Sin descripción"
                    else -> "Sin descripción"
                }
                binding.tvDescriptionDetail.text = descriptionText

                // ⭐ CONSTRUCCIÓN DE METADATOS: Usa mapeo manual para Any? ⭐
                val meta = buildString {

                    // 3. Editoriales (Publishers): Manejo manual de List<Map<"name", String>>
                    val publishersText = when (val pubs = detail.publishers) {
                        is List<*> -> {
                            // Intenta extraer el campo 'name' de cada objeto en la lista
                            pubs.joinToString(", ") { (it as? Map<*, *>)?.get("name")?.toString() ?: "" }
                        }
                        else -> "No disponible"
                    }.takeIf { it.isNotBlank() } ?: "No disponible"
                    append("Editorial: $publishersText\n")

                    // 4. Páginas (Number of Pages)
                    val pages = detail.numberOfPages?.toString() ?: "No disponible"
                    append("Páginas: $pages\n")

                    // 5. Idiomas (Languages): Manejo manual de List<Map<"key", String>>
                    val languagesText = when (val langs = detail.languages) {
                        is List<*> -> {
                            // Intenta extraer el campo 'key' y limpiar la URL
                            langs.joinToString(", ") {
                                val key = (it as? Map<*, *>)?.get("key")?.toString()
                                key?.substringAfter("/languages/") ?: ""
                            }
                        }
                        else -> "Desconocido"
                    }.takeIf { it.isNotBlank() } ?: "Desconocido"
                    append("Idiomas: $languagesText\n")

                    // 6. Fecha de publicación
                    append("Fecha de publicación: ${detail.firstPublishDate ?: "No disponible"}\n")

                    // 7. Temas (Subjects)
                    append("Temas: ${detail.subjects?.joinToString(", ") ?: "No listados"}")
                }
                binding.tvMetaDetail.text = meta

                // 8. Carga de la portada
                if (!detail.covers.isNullOrEmpty()) {
                    val coverId = detail.covers[0]
                    val url = "https://covers.openlibrary.org/b/id/${coverId}-L.jpg"
                    binding.ivCoverDetail.load(url)
                } else {
                    binding.ivCoverDetail.setImageResource(R.drawable.ic_book_placeholder)
                }

            } catch (e: Exception) {
                Log.e("DETALLE", "Error al cargar detalle: ${e.message}", e)
                binding.tvTitleDetail.text = "Error al cargar el detalle"
                binding.tvDescriptionDetail.text = "Error de conexión o de formato de datos. Intente con otro libro."
            }
        }
    }
}