package com.example.md_lab003__coroutines.model.network

import com.example.md_lab003__coroutines.model.data.CharacterResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RickAndMortyApiService {
    @GET("character")
    suspend fun getCharacters(
        @Query("page") pageNumber: Int
    ): CharacterResponse
}