package com.uvg.lab8mia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.uvg.lab8mia.ui.theme.Lab8MiaTheme
import kotlinx.serialization.Serializable

@Serializable data object LoginDestination
@Serializable data object MainDestination
@Serializable data object CharacterScreenDestination
@Serializable data class CharacterDescriptionDestination(val id: Int)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab8MiaTheme {
                AppContent()
            }
        }
    }
}

@Composable
fun AppContent() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = LoginDestination) {
            composable<LoginDestination> {
                WelcomeScreen(
                    modifier = Modifier.padding(innerPadding),
                    onEnterApp = { navController.navigate(route = CharacterScreenDestination) }
                )
            }
            navigation<MainDestination>(startDestination = CharacterScreenDestination) {
                composable<CharacterScreenDestination> {
                    CharacterListScreen(
                        onCharacterSelected = { id: Int ->
                            navController.navigate(CharacterDescriptionDestination(id = id))
                        }
                    )
                }
                composable<CharacterDescriptionDestination> {
                    val destination = it.toRoute<CharacterDescriptionDestination>()
                    CharacterDetailsScreen(
                        navigateBack = { navController.navigateUp() },
                        characterId = destination.id
                    )
                }
            }
        }
    }
}

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    onEnterApp: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ricky),
                contentDescription = "App Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier.width(280.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onEnterApp,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier.width(200.dp)
            ) {
                Text("Start Exploring")
            }
        }
        Text(
            text = "Mia Fuentes #23775",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        )
    }
}