package com.uvg.lab12mia

import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.uvg.lab12mia.data.UserDataStorePrefs
import com.uvg.lab12mia.dao.CharacterDao
import com.uvg.lab12mia.dao.LocationDao
import com.uvg.lab12mia.di.Dependencies
import com.uvg.lab12mia.entity.toCharacter
import com.uvg.lab12mia.entity.toLocation
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoginViewModel(
    private val characterDao: CharacterDao,
    private val locationDao: LocationDao,
    private val dataStoreUserPrefs: UserDataStorePrefs
) : ViewModel() {

    data class LoginUiState(
        val loginState: LoginState = LoginState.Initial,
        val username: String = "",
        val characters: List<Character> = emptyList(),
        val locations: List<Location> = emptyList()
    )

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        initializeData()
    }

    private fun initializeData() {
        viewModelScope.launch {
            combine(
                dataStoreUserPrefs.getUsername(),
                characterDao.getAllCharacters(),
                locationDao.getAllLocations()
            ) { username, characters, locations ->
                LoginUiState(
                    loginState = if (!username.isNullOrBlank()) {
                        LoginState.Success(username)
                    } else {
                        LoginState.LoggedOut
                    },
                    characters = characters.map { it.toCharacter() },
                    locations = locations.map { it.toLocation() }
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    fun updateUsername(name: String) {
        _uiState.value = _uiState.value.copy(username = name)
    }

    fun login() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(loginState = LoginState.Loading)
                dataStoreUserPrefs.setUsername(_uiState.value.username)
                _uiState.value = _uiState.value.copy(
                    loginState = LoginState.Success(_uiState.value.username)
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loginState = LoginState.Error("Error al sincronizar: ${e.message}")
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            dataStoreUserPrefs.clearUsername()
            _uiState.value = _uiState.value.copy(loginState = LoginState.LoggedOut)
        }
    }

    sealed class LoginState {
        object Initial : LoginState()
        object Loading : LoginState()
        object LoggedOut : LoginState()
        data class Success(val username: String) : LoginState()
        data class Error(val message: String) : LoginState()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = checkNotNull(this[APPLICATION_KEY])
                val db = Dependencies.provideDatabase(application)
                LoginViewModel(
                    characterDao = db.characterDao(),
                    locationDao = db.locationDao(),
                    dataStoreUserPrefs = UserDataStorePrefs(application.dataStore)
                )
            }
        }
    }
}