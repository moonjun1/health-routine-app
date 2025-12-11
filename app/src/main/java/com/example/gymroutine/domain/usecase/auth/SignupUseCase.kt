package com.example.gymroutine.domain.usecase.auth

import com.example.gymroutine.data.model.User
import com.example.gymroutine.domain.repository.AuthRepository
import com.example.gymroutine.util.Constants
import com.example.gymroutine.util.isValidEmail
import com.example.gymroutine.util.isValidPassword
import javax.inject.Inject

// 사용자 회원가입 유스케이스
class SignupUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        confirmPassword: String
    ): Result<User> {
        return try {
            // 이메일 유효성 검증
            if (email.isBlank()) {
                return Result.failure(Exception("이메일을 입력해주세요"))
            }

            if (!email.isValidEmail()) {
                return Result.failure(Exception(Constants.ERROR_INVALID_EMAIL))
            }

            // 비밀번호 유효성 검증
            if (password.isBlank()) {
                return Result.failure(Exception("비밀번호를 입력해주세요"))
            }

            if (!password.isValidPassword()) {
                return Result.failure(Exception(Constants.ERROR_WEAK_PASSWORD))
            }

            // 비밀번호 확인 유효성 검증
            if (password != confirmPassword) {
                return Result.failure(Exception("비밀번호가 일치하지 않습니다"))
            }

            // 회원가입 수행
            val user = authRepository.signUp(email, password)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
