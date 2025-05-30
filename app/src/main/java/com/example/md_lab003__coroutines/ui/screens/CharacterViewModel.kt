package com.example.md_lab003__coroutines.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.md_lab003__coroutines.model.data.Character
import com.example.md_lab003__coroutines.model.network.RickAndMortyApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface RickAndMortyUiState {
    data object Success : RickAndMortyUiState
    data object Error : RickAndMortyUiState
    data object Loading : RickAndMortyUiState
}

@Suppress("UNCHECKED_CAST")
class RickAndMortyViewModelFactory(
    private val api: RickAndMortyApiService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RickAndMortyViewModel::class.java)) {
            return RickAndMortyViewModel(api) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class RickAndMortyViewModel(
    private val api: RickAndMortyApiService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val _rickAndMortyUiState =
        MutableStateFlow<RickAndMortyUiState>(RickAndMortyUiState.Loading)
    var rickAndMortyUiState: StateFlow<RickAndMortyUiState> = _rickAndMortyUiState
    private val _characters = MutableStateFlow<List<Character>>(emptyList())
    val characters: StateFlow<List<Character>> = _characters

    init {
        fetchCharacters()
    }

    fun fetchCharacters(pageNumber: Int = 1) {
        viewModelScope.launch(dispatcher) {
            _rickAndMortyUiState.value = RickAndMortyUiState.Loading
            try {
                val response = api.getCharacters(pageNumber)
                _characters.value = response.results
                _rickAndMortyUiState.value = RickAndMortyUiState.Success
            } catch (e: HttpException) {
                _characters.value = emptyList()
                _rickAndMortyUiState.value = RickAndMortyUiState.Error
            } catch (e: IOException) {
                _characters.value = emptyList()
                _rickAndMortyUiState.value = RickAndMortyUiState.Error
            }
        }
    }

    fun clearForTest() {
        viewModelScope.cancel()
    }
}