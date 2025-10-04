package com.example.libros

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://openlibrary.org/"

    // Eliminamos el logger y el okHttpClient personalizado.
    // Retrofit usará la configuración predeterminada de OkHttp (que funciona).

    val api: OpenLibraryApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            // No se especifica .client(okHttpClient), Retrofit usa el cliente predeterminado
            .build()
            .create(OpenLibraryApiService::class.java)
    }
}