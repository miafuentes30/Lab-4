
package com.uvg.lab10mia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import LocationDb

class LocationOverviewViewModel(
    private val locationDatabase: LocationDb
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocationOverviewState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchLocations()
    }

    private fun fetchLocations() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            try {
                delay(4000)
                val allLocations = locationDatabase.getAllLocations()
                _uiState.update { it.copy(locationsList = allLocations, loading = false) }
            } catch (exception: Exception) {
                _uiState.update { it.copy(errorMessage = exception.message ?: "Unexpected error", loading = false) }
            }
        }
    }

    companion object {
        val ViewModelFactory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LocationOverviewViewModel(LocationDb()) as T
            }
        }
    }
}

data class LocationOverviewState(
    val locationsList: List<Location> = emptyList(),
    val loading: Boolean = false,
    val errorMessage: String? = null
)
