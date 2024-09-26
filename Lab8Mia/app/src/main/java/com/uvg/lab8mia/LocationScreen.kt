package com.uvg.lab8mia

import Location
import LocationDb
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private val locationDatabase = LocationDb()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen(onLocationSelected: (Int) -> Unit) {
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
        LazyColumn(
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(locationDatabase.getAllLocations()) { location ->
                LocationListItem(
                    locationData = location,
                    onItemClick = { onLocationSelected(location.id) }
                )
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
        onClick = {onItemClick(locationData.id)}
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

