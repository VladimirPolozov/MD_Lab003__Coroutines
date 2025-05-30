package com.example.md_lab003__coroutines

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.md_lab003__coroutines.ui.theme.MD_Lab003__CoroutinesTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import com.example.md_lab003__coroutines.ui.screens.CharacterScreen
import com.example.md_lab003__coroutines.ui.screens.CharacterViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MD_Lab003__CoroutinesTheme {
                CharacterScreen()
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppTopBar(viewModel: CharacterViewModel) {
    TopAppBar(
        title = { Text(text = "Rick & Morty") },
        actions = {
            IconButton(
                onClick = {
                    viewModel.fetchCharacters()
                }
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Update")
            }
        }
    )
}