package com.example.gymroutine.data.repository

import com.example.gymroutine.data.model.User
import com.example.gymroutine.data.remote.FirestoreDataSource
import com.example.gymroutine.domain.repository.UserRepository
import javax.inject.Inject

// 사용자 레포지토리 구현
class UserRepositoryImpl @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource
) : UserRepository {

    override suspend fun createUser(user: User) {
        firestoreDataSource.createUser(user)
    }

    override suspend fun getUser(userId: String): User? {
        return firestoreDataSource.getUser(userId)
    }

    override suspend fun updateUserGym(userId: String, gymId: String) {
        firestoreDataSource.updateUserGym(userId, gymId)
    }
}
