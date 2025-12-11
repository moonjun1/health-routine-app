package com.example.gymroutine.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymroutine.data.model.User
import com.example.gymroutine.domain.usecase.auth.SignupUseCase
import com.example.gymroutine.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 회원가입 화면 뷰모델
@HiltViewModel
class SignupViewModel @Inject constructor(
    private val signupUseCase: SignupUseCase
) : ViewModel() {

    private val _signupState = MutableStateFlow<Resource<User>>(Resource.Idle)
    val signupState: StateFlow<Resource<User>> = _signupState.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _confirmPassword.value = newConfirmPassword
    }

    fun signup() {
        viewModelScope.launch {
            _signupState.value = Resource.Loading

            val result = signupUseCase(
                email = _email.value,
                password = _password.value,
                confirmPassword = _confirmPassword.value
            )

            _signupState.value = if (result.isSuccess) {
                Resource.Success(result.getOrThrow())
            } else {
                Resource.Error(result.exceptionOrNull()?.message ?: "회원가입 실패")
            }
        }
    }

    fun resetState() {
        _signupState.value = Resource.Idle
    }
}
