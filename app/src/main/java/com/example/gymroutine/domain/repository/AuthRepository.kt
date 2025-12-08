package com.example.gymroutine.domain.repository

import com.example.gymroutine.data.model.User

/**
 * Authentication repository interface
 * Defines contract for authentication operations
 */
interface AuthRepository {
    suspend fun signUp(email: String, password: String): User
    suspend fun signIn(email: String, password: String): User
    fun signOut()
    fun getCurrentUserId(): String?
    fun isUserSignedIn(): Boolean
    fun getCurrentUser(): User?
}
