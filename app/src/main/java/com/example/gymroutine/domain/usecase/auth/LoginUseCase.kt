package com.example.gymroutine.domain.usecase.auth

import com.example.gymroutine.data.model.User
import com.example.gymroutine.domain.repository.AuthRepository
import com.example.gymroutine.util.Constants
import javax.inject.Inject

/**
 * Use case for user login
 */
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        return try {
            // Validate input
            if (email.isBlank()) {
                return Result.failure(Exception("이메일을 입력해주세요"))
            }

            if (password.isBlank()) {
                return Result.failure(Exception("비밀번호를 입력해주세요"))
            }

            if (password.length < Constants.MIN_PASSWORD_LENGTH) {
                return Result.failure(Exception(Constants.ERROR_WEAK_PASSWORD))
            }

            // Perform login
            val user = authRepository.signIn(email, password)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
