package com.example.gymroutine.data.remote

import com.example.gymroutine.BuildConfig
import com.example.gymroutine.data.model.Gym
import com.example.gymroutine.data.remote.dto.KakaoPlace
import javax.inject.Inject

// Data source for Kakao Local API
class KakaoLocalDataSource @Inject constructor(
    private val kakaoLocalApi: KakaoLocalApi
) {

// Search gyms near current location
// @param latitude Current latitude
// @param longitude Current longitude
// @param radius Search radius in meters (default: 5000m = 5km)
    suspend fun searchNearbyGyms(
        latitude: Double,
        longitude: Double,
        radius: Int = 5000
    ): List<Gym> {
        val response = kakaoLocalApi.searchPlaces(
            authorization = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}",
            query = "헬스장",
            x = longitude.toString(),
            y = latitude.toString(),
            radius = radius,
            page = 1,
            size = 15
        )

        return response.documents.map { it.toGym() }
    }

// Search gyms by keyword
// @param keyword Search keyword (e.g., "헬스장", "피트니스", "짐")
// @param latitude Optional current latitude for distance sorting
// @param longitude Optional current longitude for distance sorting
    suspend fun searchGymsByKeyword(
        keyword: String,
        latitude: Double? = null,
        longitude: Double? = null
    ): List<Gym> {
        val response = kakaoLocalApi.searchPlaces(
            authorization = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}",
            query = keyword,
            x = longitude?.toString(),
            y = latitude?.toString(),
            page = 1,
            size = 15
        )

        return response.documents.map { it.toGym() }
    }

// Convert KakaoPlace to Gym model
    private fun KakaoPlace.toGym(): Gym {
        return Gym(
            placeId = id,
            name = placeName,
            address = roadAddressName.ifEmpty { addressName },
            latitude = latitude.toDoubleOrNull() ?: 0.0,
            longitude = longitude.toDoubleOrNull() ?: 0.0,
            phone = phone,
            registeredBy = "", // Will be set during registration
            hours = emptyMap(), // Will be filled by gym owner
            equipments = emptyList(), // Will be filled by gym owner
            createdAt = System.currentTimeMillis()
        )
    }
}
