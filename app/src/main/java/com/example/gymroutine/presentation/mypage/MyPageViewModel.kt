package com.example.gymroutine.presentation.mypage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymroutine.data.model.User
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
 * MyPage ViewModel
 * Handles user profile, settings, and logout
 */
@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepositoryImpl
) : ViewModel() {

    companion object {
        private const val TAG = "MyPageViewModel"
    }

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _userState = MutableStateFlow<Resource<User>>(Resource.Idle)
    val userState: StateFlow<Resource<User>> = _userState.asStateFlow()

    private val _logoutState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val logoutState: StateFlow<Resource<Unit>> = _logoutState.asStateFlow()

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        val firebaseUser = authRepository.getCurrentUser()
        val userId = authRepository.getCurrentUserId()
        _isLoggedIn.value = firebaseUser != null

        if (userId != null) {
            Log.d(TAG, "checkLoginStatus: User logged in - $userId")
            loadUserData()
        } else {
            Log.d(TAG, "checkLoginStatus: Not logged in (guest mode)")
            _currentUser.value = null
        }
    }

    private fun loadUserData() {
        viewModelScope.launch {
            val userId = authRepository.getCurrentUserId() ?: return@launch

            _userState.value = Resource.Loading
            try {
                val user = userRepository.getUser(userId)
                if (user != null) {
                    _currentUser.value = user
                    _userState.value = Resource.Success(user)
                    Log.d(TAG, "loadUserData: Success - ${user.email}")
                } else {
                    _userState.value = Resource.Error("사용자 정보를 찾을 수 없습니다")
                    Log.w(TAG, "loadUserData: User not found")
                }
            } catch (e: Exception) {
                _userState.value = Resource.Error(e.message ?: "사용자 정보 로드 실패")
                Log.e(TAG, "loadUserData: Failed", e)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _logoutState.value = Resource.Loading
            try {
                authRepository.signOut()
                _isLoggedIn.value = false
                _currentUser.value = null
                _logoutState.value = Resource.Success(Unit)
                Log.d(TAG, "logout: Success")
            } catch (e: Exception) {
                _logoutState.value = Resource.Error(e.message ?: "로그아웃 실패")
                Log.e(TAG, "logout: Failed", e)
            }
        }
    }

    fun resetLogoutState() {
        _logoutState.value = Resource.Idle
    }

    fun refresh() {
        checkLoginStatus()
    }
}
