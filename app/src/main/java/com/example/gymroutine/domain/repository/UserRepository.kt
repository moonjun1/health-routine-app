package com.example.gymroutine.domain.repository

import com.example.gymroutine.data.model.User

// User repository interface
interface UserRepository {
    suspend fun createUser(user: User)
    suspend fun getUser(userId: String): User?
    suspend fun updateUserGym(userId: String, gymId: String)
}
