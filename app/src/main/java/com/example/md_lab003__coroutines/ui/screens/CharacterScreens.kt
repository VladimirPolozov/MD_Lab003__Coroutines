package com.example.md_lab003__coroutines.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.md_lab003__coroutines.AppTopBar
import com.example.md_lab003__coroutines.model.data.Character

@Composable
fun CharacterScreen(viewModel: CharacterViewModel = viewModel()) {
    val characters by viewModel.characters.collectAsState()

    Scaffold(
        topBar = { AppTopBar(viewModel) }
    ) { innerPadding ->
        if (characters.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            CharacterList(characters, modifier = Modifier.padding(innerPadding))
        }
    }
}

@Composable
fun CharacterList(
    characters: List<Character>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(characters) {
            CharacterItem(it)
        }
    }
}

@Composable
fun CharacterItem(character: Character) {
    CharacterCard(
        character,
        when (character.species) {
            "Human" -> Modifier
            "Alien" -> Modifier.background(Color.Green.copy(0.25f))
            else -> Modifier.background(Color.Blue.copy(0.25f))
        }
    )
}

@Composable
fun CharacterCard(
    character: Character,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = character.name, fontWeight = FontWeight.Bold)
                Text(text = "Species: ${character.species}")
            }
        }
    }
}