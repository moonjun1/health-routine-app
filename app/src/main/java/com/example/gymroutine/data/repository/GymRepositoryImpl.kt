package com.example.gymroutine.data.repository

import android.util.Log
import com.example.gymroutine.data.local.GymLocalDataSource
import com.example.gymroutine.data.model.Gym
import com.example.gymroutine.data.remote.FirestoreDataSource
import com.example.gymroutine.data.remote.KakaoLocalDataSource
import com.example.gymroutine.domain.repository.AuthRepository
import com.example.gymroutine.domain.repository.GymRepository
import javax.inject.Inject

/**
 * Implementation of GymRepository
 * Combines Kakao Local API for search and Firestore/Local storage for gym data management
 * Automatically uses local storage when user is not logged in
 */
class GymRepositoryImpl @Inject constructor(
    private val kakaoLocalDataSource: KakaoLocalDataSource,
    private val firestoreDataSource: FirestoreDataSource,
    private val gymLocalDataSource: GymLocalDataSource,
    private val authRepository: AuthRepository
) : GymRepository {

    companion object {
        private const val TAG = "GymRepositoryImpl"
    }

    private fun isLoggedIn(): Boolean {
        return authRepository.getCurrentUserId() != null
    }

    override suspend fun searchNearbyGyms(
        latitude: Double,
        longitude: Double,
        radius: Int
    ): List<Gym> {
        return kakaoLocalDataSource.searchNearbyGyms(latitude, longitude, radius)
    }

    override suspend fun searchGymsByKeyword(
        keyword: String,
        latitude: Double?,
        longitude: Double?
    ): List<Gym> {
        return kakaoLocalDataSource.searchGymsByKeyword(keyword, latitude, longitude)
    }

    override suspend fun getGymById(gymId: String): Gym? {
        return if (isLoggedIn()) {
            Log.d(TAG, "getGymById: Using Firebase")
            firestoreDataSource.getGym(gymId)
        } else {
            Log.d(TAG, "getGymById: Using local storage")
            gymLocalDataSource.getGym(gymId)
        }
    }

    override suspend fun registerGym(gym: Gym): Gym {
        return if (isLoggedIn()) {
            Log.d(TAG, "registerGym: Saving to Firebase")
            firestoreDataSource.createGym(gym)
            gym
        } else {
            Log.d(TAG, "registerGym: Saving to local storage")
            // For non-logged in users, use "local" as registeredBy
            val localGym = gym.copy(registeredBy = "local")
            gymLocalDataSource.saveGym(localGym)
            localGym
        }
    }

    override suspend fun updateGym(gym: Gym): Gym {
        return if (isLoggedIn()) {
            Log.d(TAG, "updateGym: Updating in Firebase")
            firestoreDataSource.updateGym(gym)
            gym
        } else {
            Log.d(TAG, "updateGym: Updating in local storage")
            gymLocalDataSource.saveGym(gym)
            gym
        }
    }

    override suspend fun deleteGym(gymId: String) {
        if (isLoggedIn()) {
            Log.d(TAG, "deleteGym: Deleting from Firebase")
            firestoreDataSource.deleteGym(gymId)
        } else {
            Log.d(TAG, "deleteGym: Deleting from local storage")
            gymLocalDataSource.deleteGym(gymId)
        }
    }

    override suspend fun getAllGyms(): List<Gym> {
        return if (isLoggedIn()) {
            Log.d(TAG, "getAllGyms: Loading from Firebase")
            firestoreDataSource.getGyms()
        } else {
            Log.d(TAG, "getAllGyms: Loading from local storage")
            gymLocalDataSource.getGyms()
        }
    }

    override suspend fun getUserGym(userId: String): Gym? {
        return if (isLoggedIn()) {
            Log.d(TAG, "getUserGym: Loading from Firebase for user $userId")
            firestoreDataSource.getUserGym(userId)
        } else {
            Log.d(TAG, "getUserGym: Loading selected gym from local storage")
            gymLocalDataSource.getSelectedGym()
        }
    }

    override suspend fun getUserGyms(userId: String): List<Gym> {
        return if (isLoggedIn()) {
            Log.d(TAG, "getUserGyms: Loading from Firebase for user $userId")
            firestoreDataSource.getUserGyms(userId)
        } else {
            Log.d(TAG, "getUserGyms: Loading all from local storage")
            gymLocalDataSource.getGyms()
        }
    }

    override suspend fun setMyGym(userId: String, gymId: String) {
        if (isLoggedIn()) {
            Log.d(TAG, "setMyGym: Setting in Firebase")
            firestoreDataSource.setMyGym(userId, gymId)
        } else {
            Log.d(TAG, "setMyGym: Setting selected gym in local storage")
            gymLocalDataSource.setSelectedGymId(gymId)
        }
    }
}
