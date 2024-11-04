package com.uvg.lab12mia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.uvg.lab12mia.dao.CharacterDao
import com.uvg.lab12mia.di.Dependencies
import com.uvg.lab12mia.entity.toCharacter
import com.uvg.lab12mia.entity.toEntity
import com.uvg.lab12mia.ktor.ApiKtorRickYMorty
import com.uvg.lab12mia.ktor.ApiRickYMorty
import com.uvg.lab12mia.ktor.KtorDependencies
import com.uvg.lab12mia.ktor.Result
import com.uvg.lab12mia.ktor.mapToCharacterModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class CharacterCollecionUIState(
    val showProgress: Boolean = false,
    val errorMessage: String? = null
)

class CharacterCollectionViewModel(
    private val apiService: ApiRickYMorty,
    private val repository: CharacterDao,
    private val backupSource: CharacterDb
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharacterCollecionUIState())
    val uiState = _uiState.asStateFlow()

    val characters = repository.getAllCharacters()
        .map { list -> list.map { it.toCharacter() } }
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
            val backupData = backupSource.getAllCharacters()
            repository.deleteAllCharacters()
            repository.insertCharacters(backupData.map { it.toEntity() })
        }
    }

    fun refreshFromNetwork() {
        viewModelScope.launch {
            _uiState.update { it.copy(showProgress = true) }

            when (val result = apiService.getCharacters()) {
                is Result.Success -> {
                    val characters = result.data.results.map { it.mapToCharacterModel() }
                    saveToRepository(characters.map { it.toEntity().toCharacter() })
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

    private fun saveToRepository(characters: List<Character>) {
        viewModelScope.launch {
            repository.insertCharacters(characters.map { it.toEntity()})
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = checkNotNull(this[APPLICATION_KEY])
                val database = Dependencies.provideDatabase(app)
                CharacterCollectionViewModel(
                    apiService = ApiKtorRickYMorty(KtorDependencies.provideHttpClient()),
                    repository = database.characterDao(),
                    backupSource = CharacterDb()
                )
            }
        }
    }
}