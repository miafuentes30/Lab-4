package com.uvg.lab11mia

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LocationDetailRoute(
    locationId: Int,
    navigateBack: () -> Unit,
    viewModel: LocationProfileViewModel = viewModel(factory = LocationProfileViewModel.ViewModelFactory)
) {
    val state by viewModel.profileState.collectAsStateWithLifecycle()

    LaunchedEffect(locationId) {
        viewModel.loadLocationProfile(locationId)
    }

    LocationDetails(
        state = state,
        navigateBack = navigateBack,
        locationId = locationId
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDetails(
    state: LocationProfileState,
    navigateBack: () -> Unit,
    locationId: Int
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Location Details") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Go back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.inverseOnSurface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                state.isProfileLoading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
                }
                state.profileErrorMessage != null -> {
                    Text(
                        text = "Error: ${state.profileErrorMessage}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
                state.locationInfo != null -> {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = state.locationInfo.name,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    LocationDetailCard(location = state.locationInfo)
                }
            }
        }
    }
}

@Composable
private fun LocationDetailCard(location: Location) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DetailRow("Id", location.id.toString())
            DetailRow("Type", location.type)
            DetailRow("Dimension", location.dimension)
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.titleMedium)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}