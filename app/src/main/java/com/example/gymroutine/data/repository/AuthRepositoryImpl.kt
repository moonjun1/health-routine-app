package com.example.gymroutine.data.repository

import com.example.gymroutine.data.model.User
import com.example.gymroutine.data.remote.FirebaseAuthDataSource
import com.example.gymroutine.data.remote.FirestoreDataSource
import com.example.gymroutine.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Authentication repository implementation
 */
class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: FirebaseAuthDataSource,
    private val firestoreDataSource: FirestoreDataSource
) : AuthRepository {

    override suspend fun signUp(email: String, password: String): User {
        // 1. Create user in Firebase Auth
        val user = authDataSource.signUp(email, password)

        // 2. Create user document in Firestore
        firestoreDataSource.createUser(user)

        return user
    }

    override suspend fun signIn(email: String, password: String): User {
        // 1. Sign in with Firebase Auth
        val authUser = authDataSource.signIn(email, password)

        // 2. Get user data from Firestore
        val firestoreUser = firestoreDataSource.getUser(authUser.id)

        // 3. Return Firestore user data (includes gymId)
        return firestoreUser ?: authUser
    }

    override fun signOut() {
        authDataSource.signOut()
    }

    override fun logout() {
        authDataSource.signOut()
    }

    override fun getCurrentUserId(): String? {
        return authDataSource.getCurrentUserId()
    }

    override fun isUserSignedIn(): Boolean {
        return authDataSource.isUserSignedIn()
    }

    override fun getCurrentUser(): User? {
        val authUser = authDataSource.getCurrentUser() ?: return null
        return authUser
    }
}
