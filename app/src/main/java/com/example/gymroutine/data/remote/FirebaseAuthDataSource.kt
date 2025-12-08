package com.example.gymroutine.data.remote

import com.example.gymroutine.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Firebase Authentication data source
 * Handles authentication operations with Firebase Auth
 */
class FirebaseAuthDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    /**
     * Sign up with email and password
     * @throws FirebaseAuthException on failure
     */
    suspend fun signUp(email: String, password: String): User {
        try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: throw Exception("사용자 생성 실패")

            return User(
                id = firebaseUser.uid,
                email = firebaseUser.email ?: email,
                gymId = null,
                createdAt = System.currentTimeMillis()
            )
        } catch (e: FirebaseAuthWeakPasswordException) {
            throw Exception("비밀번호는 6자 이상이어야 합니다")
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            throw Exception("올바른 이메일 형식이 아닙니다")
        } catch (e: FirebaseAuthUserCollisionException) {
            throw Exception("이미 사용 중인 이메일입니다")
        } catch (e: Exception) {
            throw Exception("회원가입 실패: ${e.message}")
        }
    }

    /**
     * Sign in with email and password
     * @throws FirebaseAuthException on failure
     */
    suspend fun signIn(email: String, password: String): User {
        try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: throw Exception("로그인 실패")

            return User(
                id = firebaseUser.uid,
                email = firebaseUser.email ?: email,
                gymId = null,
                createdAt = System.currentTimeMillis()
            )
        } catch (e: FirebaseAuthInvalidUserException) {
            throw Exception("사용자를 찾을 수 없습니다")
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            throw Exception("이메일 또는 비밀번호가 일치하지 않습니다")
        } catch (e: Exception) {
            throw Exception("로그인 실패: ${e.message}")
        }
    }

    /**
     * Sign out current user
     */
    fun signOut() {
        firebaseAuth.signOut()
    }

    /**
     * Get current user ID
     */
    fun getCurrentUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }

    /**
     * Check if user is signed in
     */
    fun isUserSignedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    /**
     * Get current user
     */
    fun getCurrentUser(): User? {
        val firebaseUser = firebaseAuth.currentUser ?: return null
        return User(
            id = firebaseUser.uid,
            email = firebaseUser.email ?: "",
            gymId = null,
            createdAt = System.currentTimeMillis()
        )
    }
}
