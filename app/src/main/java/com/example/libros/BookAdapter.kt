package com.example.libros

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.libros.databinding.ItemBookBinding
import com.example.libros.model.Libro

class BookAdapter(
    private var books: List<Libro>,
    private val onItemClick: (Libro) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    inner class BookViewHolder(private val binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(libro: Libro) {
            binding.tvTitle.text = libro.title ?: "Sin título"
            binding.tvAuthor.text = libro.author_name?.joinToString(", ") ?: "Desconocido"

            if (libro.cover_i != null) {
                val url = "https://covers.openlibrary.org/b/id/${libro.cover_i}-M.jpg"
                binding.ivCover.load(url)
            } else {
                binding.ivCover.setImageResource(R.drawable.ic_book_placeholder)
            }

            binding.root.setOnClickListener {
                onItemClick(libro)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount(): Int = books.size

    fun updateData(newBooks: List<Libro>) {
        books = newBooks
        notifyDataSetChanged()
    }
}
