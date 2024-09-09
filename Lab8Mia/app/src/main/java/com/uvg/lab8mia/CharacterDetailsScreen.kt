package com.uvg.lab8mia
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.uvg.lab08.util.CharacterDb

private val characterDatabase = CharacterDb()

@Composable
fun CharacterDetailsScreen(navigateBack: () -> Unit, characterId: Int) {
    Column(Modifier.fillMaxSize()) {
        CharacterAppBar(onNavigateBack = navigateBack)
        Spacer(modifier = Modifier.height(16.dp))
        CharacterDetails(characterId = characterId)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterAppBar(onNavigateBack: () -> Unit) {
    TopAppBar(
        title = { Text("Character Info") },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.KeyboardArrowLeft, "Go back")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    )
}

@Composable
private fun CharacterDetails(characterId: Int) {
    val character = characterDatabase.getCharacterById(characterId)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(character.image)
                .crossfade(true)
                .build(),
            contentDescription = "${character.name} image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(180.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = character.name,
            style = MaterialTheme.typography.headlineMedium,
            fontStyle = FontStyle.Italic
        )
        Spacer(modifier = Modifier.height(32.dp))
        CharacterInfoRow("Species", character.species)
        CharacterInfoRow("Status", character.status)
        CharacterInfoRow("Gender", character.gender)
    }
}

@Composable
private fun CharacterInfoRow(label: String, value: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}