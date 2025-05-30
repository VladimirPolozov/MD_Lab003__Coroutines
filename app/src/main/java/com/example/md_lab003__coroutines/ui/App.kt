package com.example.md_lab003__coroutines.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.md_lab003__coroutines.model.network.RickAndMortyApiService
import com.example.md_lab003__coroutines.ui.screens.MainScreen
import com.example.md_lab003__coroutines.ui.screens.RickAndMortyViewModel
import com.example.md_lab003__coroutines.ui.screens.RickAndMortyViewModelFactory

@Composable
fun RickAndMortyApp(apiService: RickAndMortyApiService) {
    val viewModel: RickAndMortyViewModel = viewModel(factory = RickAndMortyViewModelFactory(apiService))

    Scaffold(
        topBar = { AppTopBar(viewModel) }
    ) {
        MainScreen(
            modifier = Modifier.padding(it)
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppTopBar(viewModel: RickAndMortyViewModel) {
    TopAppBar(
        title = { Text(text = "Rick and Morty") },
        actions = {
            IconButton(
                onClick = {
                    val pageNumber: Int = (0..42).random()
                    viewModel.fetchCharacters(pageNumber)
                }
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Update")
            }
        }
    )
}