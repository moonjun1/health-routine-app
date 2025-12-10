package com.example.gymroutine.domain.repository

import com.example.gymroutine.data.model.Gym

/**
 * Repository interface for gym operations
 */
interface GymRepository {

    /**
     * Search gyms near current location using Kakao Local API
     */
    suspend fun searchNearbyGyms(
        latitude: Double,
        longitude: Double,
        radius: Int = 5000
    ): List<Gym>

    /**
     * Search gyms by keyword using Kakao Local API
     */
    suspend fun searchGymsByKeyword(
        keyword: String,
        latitude: Double? = null,
        longitude: Double? = null
    ): List<Gym>

    /**
     * Get gym by ID from Firestore
     */
    suspend fun getGymById(gymId: String): Gym?

    /**
     * Register new gym to Firestore
     */
    suspend fun registerGym(gym: Gym): Gym

    /**
     * Update existing gym in Firestore
     */
    suspend fun updateGym(gym: Gym): Gym

    /**
     * Delete gym from Firestore
     */
    suspend fun deleteGym(gymId: String)

    /**
     * Get all gyms from Firestore
     */
    suspend fun getAllGyms(): List<Gym>

    /**
     * Get user's registered gym
     */
    suspend fun getUserGym(userId: String): Gym?
}
