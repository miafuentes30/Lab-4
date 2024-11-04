
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

data class CharacterDetailUIState(
    val selectedCharacter: Character? = null,
    val showProgress: Boolean = false,
    val errorMessage: String? = null
)

class CharacterDetailViewModel(
    private val repository: CharacterDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharacterDetailUIState())
    val uiState = _uiState.asStateFlow()

    fun loadCharacterDetails(characterId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(showProgress = true) }

            try {
                delay(2000)
                val character = repository.getCharacterById(characterId)!!.toCharacter()
                _uiState.update { it.copy(
                    selectedCharacter = character,
                    showProgress = false
                )}
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    errorMessage = e.message ?: "Failed to load character",
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
                CharacterDetailViewModel(
                    repository = database.characterDao()
                )
            }
        }
    }
}