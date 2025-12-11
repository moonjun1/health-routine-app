package com.example.gymroutine.data.repository

import com.example.gymroutine.data.model.User
import com.example.gymroutine.data.remote.FirebaseAuthDataSource
import com.example.gymroutine.data.remote.FirestoreDataSource
import com.example.gymroutine.domain.repository.AuthRepository
import javax.inject.Inject

// 인증 레포지토리 구현
class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: FirebaseAuthDataSource,
    private val firestoreDataSource: FirestoreDataSource
) : AuthRepository {

    override suspend fun signUp(email: String, password: String): User {
        // 1. Firebase Auth에서 사용자 생성
        val user = authDataSource.signUp(email, password)

        // 2. Firestore에 사용자 문서 생성
        firestoreDataSource.createUser(user)

        return user
    }

    override suspend fun signIn(email: String, password: String): User {
        // 1. Firebase Auth로 로그인
        val authUser = authDataSource.signIn(email, password)

        // 2. Firestore에서 사용자 데이터 조회
        val firestoreUser = firestoreDataSource.getUser(authUser.id)

        // 3. Firestore 사용자 데이터 반환 (gymId 포함)
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
