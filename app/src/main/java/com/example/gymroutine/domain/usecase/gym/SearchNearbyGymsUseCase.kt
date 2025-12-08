package com.example.gymroutine.domain.usecase.gym

import com.example.gymroutine.data.model.Gym
import com.example.gymroutine.domain.repository.GymRepository
import javax.inject.Inject

/**
 * Use case for searching nearby gyms
 */
class SearchNearbyGymsUseCase @Inject constructor(
    private val gymRepository: GymRepository
) {
    suspend operator fun invoke(
        latitude: Double,
        longitude: Double,
        radius: Int = 5000
    ): Result<List<Gym>> {
        return try {
            // Validate coordinates
            if (latitude !in -90.0..90.0) {
                return Result.failure(Exception("위도는 -90 ~ 90 사이여야 합니다"))
            }
            if (longitude !in -180.0..180.0) {
                return Result.failure(Exception("경도는 -180 ~ 180 사이여야 합니다"))
            }
            if (radius !in 0..20000) {
                return Result.failure(Exception("검색 반경은 0 ~ 20000m 사이여야 합니다"))
            }

            val gyms = gymRepository.searchNearbyGyms(latitude, longitude, radius)
            Result.success(gyms)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
