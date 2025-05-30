package com.example.md_lab003__coroutines.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.md_lab003__coroutines.R
import com.example.md_lab003__coroutines.model.data.Character

@Composable
@SuppressLint("ModifierParameter")
fun MainScreen (
    viewModel: RickAndMortyViewModel = viewModel(),
    modifier: Modifier = Modifier,
) {
    val rickAndMortyUiState by viewModel.rickAndMortyUiState.collectAsState()
    val characters by viewModel.characters.collectAsState()

    when (rickAndMortyUiState) {
        is RickAndMortyUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is RickAndMortyUiState.Success -> ResultScreen(characters, modifier)
        is RickAndMortyUiState.Error -> ErrorScreen(modifier = modifier.fillMaxSize())
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error),
            contentDescription = ""
        )
        Text(text = "Loading error", modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun ResultScreen(
    characters: List<Character>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(characters) {
            CharacterCard(it)
        }
    }
}

@Composable
fun CharacterCard(character: Character) {
    val modifier = when (character.species) {
        "Human" -> Modifier
        "Alien" -> Modifier.background(Color.Green.copy(0.25f))
        else -> Modifier.background(Color.Blue.copy(0.25f))
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                modifier = Modifier
                    .size(64.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = character.name, fontWeight = FontWeight.Bold)
                Text(text = "Species: ${character.species}")
            }
        }
    }
}
