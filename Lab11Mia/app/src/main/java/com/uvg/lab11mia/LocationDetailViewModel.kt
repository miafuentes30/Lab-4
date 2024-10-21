
package com.uvg.lab11mia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import LocationDb

class LocationProfileViewModel(
    private val locationDatabase: LocationDb
) : ViewModel() {

    private val _profileState = MutableStateFlow(LocationProfileState())
    val profileState = _profileState.asStateFlow()

    fun loadLocationProfile(locationId: Int) {
        viewModelScope.launch {
            _profileState.update { it.copy(isProfileLoading = true) }
            try {
                delay(2000)
                val locationDetails = locationDatabase.getLocationById(locationId)
                _profileState.update { it.copy(locationInfo = locationDetails, isProfileLoading = false) }
            } catch (exception: Exception) {
                _profileState.update { it.copy(profileErrorMessage = exception.message ?: "Unable to load profile", isProfileLoading = false) }
            }
        }
    }

    companion object {
        val ViewModelFactory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LocationProfileViewModel(LocationDb()) as T
            }
        }
    }
}

data class LocationProfileState(
    val locationInfo: Location? = null,
    val isProfileLoading: Boolean = false,
    val profileErrorMessage: String? = null
)
