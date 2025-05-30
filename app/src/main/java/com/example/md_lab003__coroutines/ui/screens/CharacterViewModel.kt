package com.example.md_lab003__coroutines.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.md_lab003__coroutines.model.data.Character
import com.example.md_lab003__coroutines.model.network.RickAndMortyApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharacterViewModel : ViewModel() {
    private val _characters = MutableStateFlow<List<Character>>(emptyList())
    val characters: StateFlow<List<Character>> = _characters

    fun fetchCharacters() {
        val pageNumber: Int = (0..42).random()
        viewModelScope.launch {
            try {
                Log.d("CharacterViewModel", "Characters loading...")
                val response = RickAndMortyApi.retrofitService.getCharacters(pageNumber)
                _characters.value = response.results
                Log.d("CharacterViewModel", "Characters are loaded: ${response.results.size}")
            } catch (e: Exception) {
                Log.e("CharacterViewModel", "An error occurred during trying trying to load characters", e)
            }
        }
    }

    init {
        fetchCharacters()
    }
}
