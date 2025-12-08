package com.example.gymroutine.data.repository

import com.example.gymroutine.data.model.Gym
import com.example.gymroutine.data.remote.FirestoreDataSource
import com.example.gymroutine.data.remote.KakaoLocalDataSource
import com.example.gymroutine.domain.repository.GymRepository
import javax.inject.Inject

/**
 * Implementation of GymRepository
 * Combines Kakao Local API for search and Firestore for gym data management
 */
class GymRepositoryImpl @Inject constructor(
    private val kakaoLocalDataSource: KakaoLocalDataSource,
    private val firestoreDataSource: FirestoreDataSource
) : GymRepository {

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
        return firestoreDataSource.getGym(gymId)
    }

    override suspend fun registerGym(gym: Gym): Gym {
        firestoreDataSource.createGym(gym)
        return gym
    }

    override suspend fun updateGym(gym: Gym): Gym {
        firestoreDataSource.updateGym(gym)
        return gym
    }

    override suspend fun deleteGym(gymId: String) {
        firestoreDataSource.deleteGym(gymId)
    }

    override suspend fun getAllGyms(): List<Gym> {
        return firestoreDataSource.getGyms()
    }
}
