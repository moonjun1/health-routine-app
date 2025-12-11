package com.example.gymroutine.domain.repository

import com.example.gymroutine.data.model.User

// 사용자 레포지토리 인터페이스
interface UserRepository {
    suspend fun createUser(user: User)
    suspend fun getUser(userId: String): User?
    suspend fun updateUserGym(userId: String, gymId: String)
}
