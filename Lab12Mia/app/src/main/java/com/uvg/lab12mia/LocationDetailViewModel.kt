
package com.uvg.lab12mia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.uvg.lab12mia.dao.LocationDao
import com.uvg.lab12mia.di.Dependencies
import com.uvg.lab12mia.entity.toLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

data class LocationDetailUIState(
    val selectedLocation: Location? = null,
    val showProgress: Boolean = false,
    val errorMessage: String? = null
)

class LocationDetailViewModel(
    private val repository: LocationDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocationDetailUIState())
    val uiState = _uiState.asStateFlow()

    fun loadLocationDetails(locationId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(showProgress = true) }

            try {
                delay(2000)
                val location = repository.getLocationById(locationId)!!.toLocation()
                _uiState.update { it.copy(
                    selectedLocation = location,
                    showProgress = false
                )}
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    errorMessage = e.message ?: "Failed to load location",
                    showProgress = false
                )}
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = checkNotNull(this[APPLICATION_KEY])
                val database = Dependencies.provideDatabase(app)
                LocationDetailViewModel(
                    repository = database.locationDao()
                )
            }
        }
    }
}