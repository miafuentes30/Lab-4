package com.uvg.lab12mia

import LocationDb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.uvg.lab12mia.dao.LocationDao
import com.uvg.lab12mia.di.Dependencies
import com.uvg.lab12mia.entity.toLocation
import com.uvg.lab12mia.entity.toEntity
import com.uvg.lab12mia.ktor.ApiKtorRickYMorty
import com.uvg.lab12mia.ktor.ApiRickYMorty
import com.uvg.lab12mia.ktor.KtorDependencies
import com.uvg.lab12mia.ktor.Result
import com.uvg.lab12mia.ktor.mapToLocationModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class LocationCollecionUIState(
    val showProgress: Boolean = false,
    val errorMessage: String? = null
)

class LocationCollectionViewModel(
    private val apiService: ApiRickYMorty,
    private val repository: LocationDao,
    private val backupSource: LocationDb
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocationCollecionUIState())
    val uiState = _uiState.asStateFlow()

    val locations = repository.getAllLocations()
        .map { list -> list.map { it.toLocation() } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    init {
        refreshFromNetwork()
    }

    private fun loadFromBackup() {
        viewModelScope.launch {
            val backupData = backupSource.getAllLocations()
            repository.deleteAllLocations()
            repository.insertLocations(backupData.map { it.toEntity() })
        }
    }

    fun refreshFromNetwork() {
        viewModelScope.launch {
            _uiState.update { it.copy(showProgress = true) }

            when (val result = apiService.getLocations()) {
                is Result.Success -> {
                    val locations = result.data.results.map { it.mapToLocationModel() }
                    saveToRepository(locations.map { it.toEntity().toLocation() })
                    println("Network fetch successful")
                }
                is Result.Error -> {
                    loadFromBackup()
                    println("Falling back to local data: ${result.error}")
                }
            }

            delay(4000)
            _uiState.update { it.copy(showProgress = false) }
        }
    }

    private fun saveToRepository(locations: List<Location>) {
        viewModelScope.launch {
            repository.insertLocations(locations.map { it.toEntity()})
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = checkNotNull(this[APPLICATION_KEY])
                val database = Dependencies.provideDatabase(app)
                LocationCollectionViewModel(
                    apiService = ApiKtorRickYMorty(KtorDependencies.provideHttpClient()),
                    repository = database.locationDao(),
                    backupSource = LocationDb()
                )
            }
        }
    }
}