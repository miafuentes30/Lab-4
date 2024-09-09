package com.uvg.lab8mia

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.uvg.lab08.util.CharacterDb

private val characterDatabase = CharacterDb()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(onCharacterSelected: (Int) -> Unit) {
    Column(Modifier.fillMaxSize()) {
        CharacterListAppBar()
        CharacterList(onCharacterSelected = onCharacterSelected)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterListAppBar() {
    TopAppBar(
        title = { Text("Character Gallery") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

@Composable
private fun CharacterList(onCharacterSelected: (Int) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        items(characterDatabase.getAllCharacters()){personaje : Character ->
            CharacterListItem(character = personaje, onCharacterSelected = onCharacterSelected)
            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}

@Composable
private fun CharacterListItem(character: Character, onCharacterSelected: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCharacterSelected(character.id) }
            .padding(8.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(character.image)
                .crossfade(true)
                .build(),
            contentDescription = "${character.name} thumbnail",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(4.dp))
        )
        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(
                text = character.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = "${character.species} - ${character.status}",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}