package com.uvg.lab10mia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.AccountBox
import androidx.compose.material.icons.sharp.Person
import androidx.compose.material.icons.sharp.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.uvg.lab10mia.ui.theme.Lab10MiaTheme
import kotlinx.serialization.Serializable

@Serializable data object LoginDestination
@Serializable data object MainDestination
@Serializable data object AltDestination
@Serializable data object ProfileDestination
@Serializable data object CharacterScreenDestination
@Serializable data object LocationScreenDestination
@Serializable data class CharacterDescriptionDestination(val id: Int)
@Serializable data class LocationDetailsDestination(val id: Int)

data class NavItem(
    val title: String,
    val destination: Any,
    val Icon: ImageVector
)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab10MiaTheme {
                AppContent()
            }
        }
    }
}

@Composable
fun AppContent() {
    val navController = rememberNavController()
    val items = listOf(
        NavItem("Characters", CharacterScreenDestination, Icons.Sharp.Person),
        NavItem("Places", LocationScreenDestination, Icons.Sharp.Place),
        NavItem("Profile", ProfileDestination, Icons.Sharp.AccountBox)

    )

    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    var logged by rememberSaveable {
        mutableStateOf(false)
    }
    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        when (logged){
            true -> NavigationBar {
                items.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        label = {Text(text = navItem.title)},
                        onClick = {
                            selectedItemIndex = index
                            navController.navigate(navItem.destination)
                        },
                        icon = {
                            Icon(navItem.Icon, contentDescription = "Icon")
                        })
                }
            }
            else -> {}
        }
    }

        ) { innerPadding ->
        NavHost(navController = navController, startDestination = LoginDestination) {
            composable<LoginDestination> {
                WelcomeScreen(
                    modifier = Modifier.padding(innerPadding),
                    onEnterApp = {
                        navController.navigate(route = CharacterScreenDestination)
                        logged = true
                    }
                )
            }
            composable<ProfileDestination> {
                ProfileScreen(
                    onSignOut = {

                        navController.navigate(route = LoginDestination)
                        navController.popBackStack(route = MainDestination, inclusive = false)
                        logged = false
                    }
                )
            }
            navigation<MainDestination>(startDestination = CharacterScreenDestination) {
                composable<CharacterScreenDestination> {
                    CharacterListRoute(
                        onCharacterSelected = { id: Int ->
                            navController.navigate(CharacterDescriptionDestination(id = id))
                        }
                    )
                }
                composable<CharacterDescriptionDestination> {
                    val destination = it.toRoute<CharacterDescriptionDestination>()
                    CharacterDetailRoute(
                        navigateBack = { navController.navigateUp() },
                        characterId = destination.id
                    )
                }
            }
            navigation<AltDestination>(startDestination = LocationScreenDestination) {
                composable<LocationScreenDestination> {
                    LocationListRoute(
                        onLocationSelected = { id: Int ->
                            navController.navigate(LocationDetailsDestination(id = id))
                        }
                    )
                }
                composable<LocationDetailsDestination> {
                    val destination = it.toRoute<LocationDetailsDestination>()
                    LocationDetailRoute(
                        navigateBack = { navController.navigateUp() },
                        locationId = destination.id
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