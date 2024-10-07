package com.uvg.lab10mia

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LocationListRoute(
    onLocationSelected: (Int) -> Unit,
    viewModel: LocationOverviewViewModel = viewModel(factory = LocationOverviewViewModel.ViewModelFactory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LocationScreen(
        state = state,
        onLocationSelected = onLocationSelected
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen(
    state: LocationOverviewState,
    onLocationSelected: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Explore Locations") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )
        }
    ) { innerPadding ->
        when {
            state.loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            state.errorMessage != null -> {
                Text(
                    text = "Error: ${state.errorMessage}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
            else -> {
                LazyColumn(
                    contentPadding = innerPadding,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.locationsList) { location ->
                        LocationListItem(
                            locationData = location,
                            onItemClick = { onLocationSelected(location.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LocationListItem(locationData: Location, onItemClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        onClick = { onItemClick(locationData.id) }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = locationData.name,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = locationData.type,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}