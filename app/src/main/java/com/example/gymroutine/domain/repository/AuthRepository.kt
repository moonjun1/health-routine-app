package com.example.gymroutine.domain.repository

import com.example.gymroutine.data.model.User

// 인증 저장소 인터페이스
// 인증 작업에 대한 계약 정의
interface AuthRepository {
    suspend fun signUp(email: String, password: String): User
    suspend fun signIn(email: String, password: String): User
    fun signOut()
    fun logout()
    fun getCurrentUserId(): String?
    fun isUserSignedIn(): Boolean
    fun getCurrentUser(): User?
}
