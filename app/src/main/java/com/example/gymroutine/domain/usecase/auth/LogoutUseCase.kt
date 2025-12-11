package com.example.gymroutine.domain.usecase.auth

import com.example.gymroutine.domain.repository.AuthRepository
import javax.inject.Inject

// 사용자 로그아웃 유스케이스
class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke() {
        authRepository.signOut()
    }
}
