package com.example.gymroutine.domain.usecase.auth

import com.example.gymroutine.domain.repository.AuthRepository
import javax.inject.Inject

// Use case for user logout
class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke() {
        authRepository.signOut()
    }
}
