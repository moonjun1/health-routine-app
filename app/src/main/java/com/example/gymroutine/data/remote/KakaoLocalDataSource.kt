package com.example.gymroutine.data.remote

import com.example.gymroutine.BuildConfig
import com.example.gymroutine.data.model.Gym
import com.example.gymroutine.data.remote.dto.KakaoPlace
import javax.inject.Inject

// Kakao Local API 데이터 소스
class KakaoLocalDataSource @Inject constructor(
    private val kakaoLocalApi: KakaoLocalApi
) {

// 현재 위치 근처 헬스장 검색
// @param latitude 현재 위도
// @param longitude 현재 경도
// @param radius 검색 반경(미터) (기본값: 5000m = 5km)
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

// 키워드로 헬스장 검색
// @param keyword 검색 키워드 (예: "헬스장", "피트니스", "짐")
// @param latitude 거리 정렬용 현재 위도 (선택사항)
// @param longitude 거리 정렬용 현재 경도 (선택사항)
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

// KakaoPlace를 Gym 모델로 변환
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
