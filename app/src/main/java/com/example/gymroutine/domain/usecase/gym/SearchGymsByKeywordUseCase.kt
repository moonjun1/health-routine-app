package com.example.gymroutine.domain.usecase.gym

import com.example.gymroutine.data.model.Gym
import com.example.gymroutine.domain.repository.GymRepository
import javax.inject.Inject

// 키워드로 헬스장 검색 유스케이스
class SearchGymsByKeywordUseCase @Inject constructor(
    private val gymRepository: GymRepository
) {
    suspend operator fun invoke(
        keyword: String,
        latitude: Double? = null,
        longitude: Double? = null
    ): Result<List<Gym>> {
        return try {
            // 키워드 유효성 검증
            if (keyword.isBlank()) {
                return Result.failure(Exception("검색어를 입력해주세요"))
            }
            if (keyword.length < 2) {
                return Result.failure(Exception("검색어는 2자 이상이어야 합니다"))
            }

            // 좌표 유효성 검증 if provided
            if (latitude != null && latitude !in -90.0..90.0) {
                return Result.failure(Exception("위도는 -90 ~ 90 사이여야 합니다"))
            }
            if (longitude != null && longitude !in -180.0..180.0) {
                return Result.failure(Exception("경도는 -180 ~ 180 사이여야 합니다"))
            }

            val gyms = gymRepository.searchGymsByKeyword(keyword, latitude, longitude)
            Result.success(gyms)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
