package com.example.md_lab003__coroutines.model.network

import com.example.md_lab003__coroutines.model.data.CharacterResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object RickAndMortyApi {
    private const val BASE_URL = "https://rickandmortyapi.com/api/"

    val retrofitService: RickAndMortyApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RickAndMortyApiService::class.java)
    }
}

interface RickAndMortyApiService {
    @GET("character")
    suspend fun getCharacters(
        @Query("page") pageNumber: Int
    ): CharacterResponse
}