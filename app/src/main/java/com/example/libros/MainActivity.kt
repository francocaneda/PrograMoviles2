package com.example.libros

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
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

        // Configurar la Toolbar para que actúe como la ActionBar (Opcional, pero buena práctica)
        setSupportActionBar(binding.toolbar)

        // 1. Manejo del botón "Favoritos" de la Toolbar
        // Buscamos el botón dentro de la toolbar por su ID.
        binding.toolbar.findViewById<Button>(R.id.btn_favoritos_toolbar)?.setOnClickListener {
            Toast.makeText(this, "Funcionalidad de Favoritos en desarrollo.", Toast.LENGTH_SHORT).show()
        }

        // 2. Configuración del Adaptador con el manejo de clic
        adapter = BookAdapter(emptyList()) { libro: Libro ->
            // Lógica de navegación al detalle (funciona bien)
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

        // 3. Configuración del RecyclerView
        binding.rvBooks.layoutManager = LinearLayoutManager(this)
        binding.rvBooks.adapter = adapter

        // 4. Observación del ViewModel
        viewModel.books.observe(this) { libros ->
            adapter.updateData(libros)
        }

        // 5. Carga inicial de datos
        viewModel.fetchBooks("harry potter")

        // 6. Configuración del buscador
        binding.btnSearch.setOnClickListener {
            // Accedemos al EditText a través del binding (que ahora está dentro del TextInputLayout)
            val query = binding.etSearch.text.toString().trim()
            if (query.isNotEmpty()) {
                viewModel.fetchBooks(query)
            }
        }
    }
}