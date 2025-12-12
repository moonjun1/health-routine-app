package com.example.gymroutine.presentation.mypage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymroutine.domain.repository.AuthRepository
import com.example.gymroutine.util.Resource
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    companion object {
        private const val TAG = "ChangePasswordViewModel"
    }

    private val _currentPassword = MutableStateFlow("")
    val currentPassword: StateFlow<String> = _currentPassword.asStateFlow()

    private val _newPassword = MutableStateFlow("")
    val newPassword: StateFlow<String> = _newPassword.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    private val _changePasswordState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val changePasswordState: StateFlow<Resource<Unit>> = _changePasswordState.asStateFlow()

    fun updateCurrentPassword(value: String) {
        _currentPassword.value = value
    }

    fun updateNewPassword(value: String) {
        _newPassword.value = value
    }

    fun updateConfirmPassword(value: String) {
        _confirmPassword.value = value
    }

    fun changePassword() {
        viewModelScope.launch {
            _changePasswordState.value = Resource.Loading

            try {
                val current = _currentPassword.value
                val new = _newPassword.value
                val confirm = _confirmPassword.value

                // 유효성 검사
                if (current.isEmpty() || new.isEmpty() || confirm.isEmpty()) {
                    _changePasswordState.value = Resource.Error("모든 필드를 입력해주세요")
                    return@launch
                }

                if (new.length < 6) {
                    _changePasswordState.value = Resource.Error("새 비밀번호는 6자 이상이어야 합니다")
                    return@launch
                }

                if (new != confirm) {
                    _changePasswordState.value = Resource.Error("새 비밀번호가 일치하지 않습니다")
                    return@launch
                }

                if (current == new) {
                    _changePasswordState.value = Resource.Error("새 비밀번호는 현재 비밀번호와 달라야 합니다")
                    return@launch
                }

                // 현재 사용자 가져오기
                val firebaseUser = FirebaseAuth.getInstance().currentUser
                if (firebaseUser == null) {
                    _changePasswordState.value = Resource.Error("로그인이 필요합니다")
                    return@launch
                }

                val email = firebaseUser.email
                if (email == null) {
                    _changePasswordState.value = Resource.Error("이메일 정보를 찾을 수 없습니다")
                    return@launch
                }

                // 현재 비밀번호로 사용자 재인증
                val credential = EmailAuthProvider.getCredential(email, current)
                firebaseUser.reauthenticate(credential).await()

                // 비밀번호 업데이트
                firebaseUser.updatePassword(new).await()

                Log.d(TAG, "Password changed successfully")
                _changePasswordState.value = Resource.Success(Unit)

                // 필드 초기화
                _currentPassword.value = ""
                _newPassword.value = ""
                _confirmPassword.value = ""

            } catch (e: Exception) {
                Log.e(TAG, "Failed to change password", e)
                val errorMessage = when {
                    e.message?.contains("password is invalid") == true -> "현재 비밀번호가 올바르지 않습니다"
                    e.message?.contains("network") == true -> "네트워크 오류가 발생했습니다"
                    else -> e.message ?: "비밀번호 변경 실패"
                }
                _changePasswordState.value = Resource.Error(errorMessage)
            }
        }
    }

    fun resetState() {
        _changePasswordState.value = Resource.Idle
    }
}
