
package com.uvg.lab10mia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class CharacterOverviewViewModel(
    private val characterDatabase: CharacterDb
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharacterOverviewState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchCharacters()
    }

    private fun fetchCharacters() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingList = true) }
            try {
                delay(4000)
                val allCharacters = characterDatabase.getAllCharacters()
                _uiState.update { it.copy(charactersList = allCharacters, isLoadingList = false) }
            } catch (exception: Exception) {
                _uiState.update { it.copy(listError = exception.message ?: "Failed to load", isLoadingList = false) }
            }
        }
    }

    companion object {
        val ViewModelFactory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CharacterOverviewViewModel(CharacterDb()) as T
            }
        }
    }
}

data class CharacterOverviewState(
    val charactersList: List<Character> = emptyList(),
    val isLoadingList: Boolean = false,
    val listError: String? = null
)
