package com.example.gymroutine.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymroutine.data.repository.GymRepositoryImpl
import com.example.gymroutine.data.repository.UserRepositoryImpl
import com.example.gymroutine.domain.repository.AuthRepository
import com.example.gymroutine.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Settings screen ViewModel
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepositoryImpl,
    private val gymRepository: GymRepositoryImpl
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _userEmail = MutableStateFlow("")
    val userEmail: StateFlow<String> = _userEmail.asStateFlow()

    private val _gymName = MutableStateFlow("")
    val gymName: StateFlow<String> = _gymName.asStateFlow()

    private val _logoutState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val logoutState: StateFlow<Resource<Unit>> = _logoutState.asStateFlow()

    init {
        loadUserInfo()
    }

    private fun loadUserInfo() {
        viewModelScope.launch {
            val userId = authRepository.getCurrentUserId()
            _isLoggedIn.value = userId != null

            if (userId != null) {
                val user = userRepository.getUser(userId)
                _userEmail.value = user?.email ?: ""
            }

            // Load gym for both logged-in and guest users
            try {
                val gyms = gymRepository.getUserGyms(userId ?: "")
                _gymName.value = gyms.firstOrNull()?.name ?: ""
            } catch (e: Exception) {
                android.util.Log.e("SettingsViewModel", "Failed to load gym", e)
                _gymName.value = ""
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _logoutState.value = Resource.Loading
            try {
                authRepository.logout()
                _logoutState.value = Resource.Success(Unit)
            } catch (e: Exception) {
                _logoutState.value = Resource.Error(e.message ?: "로그아웃 실패")
            }
        }
    }

    fun resetLogoutState() {
        _logoutState.value = Resource.Idle
    }

    fun refresh() {
        loadUserInfo()
    }
}
