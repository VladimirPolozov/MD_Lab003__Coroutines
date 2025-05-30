package com.example.md_lab003__coroutines

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.md_lab003__coroutines.model.network.RickAndMortyApiService
import com.example.md_lab003__coroutines.ui.theme.MD_Lab003__CoroutinesTheme
import com.example.md_lab003__coroutines.ui.RickAndMortyApp
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    private val apiService: RickAndMortyApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RickAndMortyApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MD_Lab003__CoroutinesTheme {
                RickAndMortyApp(apiService = apiService)
            }
        }
    }
}