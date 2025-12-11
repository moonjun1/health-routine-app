package com.example.gymroutine.presentation.mypage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymroutine.data.model.User
import com.example.gymroutine.domain.repository.AuthRepository
import com.example.gymroutine.data.repository.UserRepositoryImpl
import com.example.gymroutine.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepositoryImpl
) : ViewModel() {

    companion object {
        private const val TAG = "EditProfileViewModel"
    }

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _updateState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val updateState: StateFlow<Resource<Unit>> = _updateState.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            try {
                val userId = authRepository.getCurrentUserId()
                if (userId != null) {
                    Log.d(TAG, "Loading user data for: $userId")
                    val user = userRepository.getUser(userId)
                    if (user != null) {
                        _currentUser.value = user
                        _email.value = user.email
                        Log.d(TAG, "User data loaded: ${user.email}")
                    } else {
                        Log.e(TAG, "User not found")
                    }
                } else {
                    Log.e(TAG, "No user logged in")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading user data", e)
            }
        }
    }

    fun updateEmail(value: String) {
        _email.value = value
    }

    fun saveProfile() {
        viewModelScope.launch {
            _updateState.value = Resource.Loading
            try {
                val userId = authRepository.getCurrentUserId()
                if (userId == null) {
                    _updateState.value = Resource.Error("로그인이 필요합니다")
                    return@launch
                }

                val currentEmail = _currentUser.value?.email ?: ""
                val newEmail = _email.value.trim()

                if (newEmail.isEmpty()) {
                    _updateState.value = Resource.Error("이메일을 입력해주세요")
                    return@launch
                }

                if (newEmail == currentEmail) {
                    _updateState.value = Resource.Error("변경된 내용이 없습니다")
                    return@launch
                }

                // Firebase Auth email update would go here
                // For now, we'll just show a message that email change requires re-authentication
                _updateState.value = Resource.Error("이메일 변경은 현재 지원하지 않습니다")

                Log.d(TAG, "Profile update attempted: $newEmail")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to update profile", e)
                _updateState.value = Resource.Error(e.message ?: "프로필 업데이트 실패")
            }
        }
    }

    fun resetUpdateState() {
        _updateState.value = Resource.Idle
    }
}
