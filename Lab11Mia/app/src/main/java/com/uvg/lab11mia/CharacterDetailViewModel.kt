
package com.uvg.lab11mia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class CharacterProfileViewModel(
    private val characterDatabase: CharacterDb
) : ViewModel() {

    private val _profileState = MutableStateFlow(CharacterProfileState())
    val profileState = _profileState.asStateFlow()

    fun loadCharacterProfile(characterId: Int) {
        viewModelScope.launch {
            _profileState.update { it.copy(isLoadingProfile = true) }
            try {
                delay(2000)
                val characterDetails = characterDatabase.getCharacterById(characterId)
                _profileState.update { it.copy(characterInfo = characterDetails, isLoadingProfile = false) }
            } catch (exception: Exception) {
                _profileState.update { it.copy(profileError = exception.message ?: "Error occurred", isLoadingProfile = false) }
            }
        }
    }

    companion object {
        val ViewModelFactory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CharacterProfileViewModel(CharacterDb()) as T
            }
        }
    }
}

data class CharacterProfileState(
    val characterInfo: Character? = null,
    val isLoadingProfile: Boolean = false,
    val profileError: String? = null
)
