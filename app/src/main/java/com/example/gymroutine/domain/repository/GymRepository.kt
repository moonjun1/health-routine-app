package com.example.gymroutine.domain.repository

import com.example.gymroutine.data.model.Gym

// 헬스장 작업용 레포지토리 인터페이스
interface GymRepository {

// Kakao Local API를 사용하여 현재 위치 근처 헬스장 검색
    suspend fun searchNearbyGyms(
        latitude: Double,
        longitude: Double,
        radius: Int = 5000
    ): List<Gym>

// Kakao Local API를 사용하여 키워드로 헬스장 검색
    suspend fun searchGymsByKeyword(
        keyword: String,
        latitude: Double? = null,
        longitude: Double? = null
    ): List<Gym>

// Firestore에서 ID로 헬스장 조회
    suspend fun getGymById(gymId: String): Gym?

// Firestore에 새 헬스장 등록
    suspend fun registerGym(gym: Gym): Gym

// Firestore에서 기존 헬스장 업데이트
    suspend fun updateGym(gym: Gym): Gym

// Firestore에서 헬스장 삭제
    suspend fun deleteGym(gymId: String)

// Firestore에서 모든 헬스장 조회
    suspend fun getAllGyms(): List<Gym>

// 사용자가 등록한 헬스장 조회 (첫 번째 헬스장)
    suspend fun getUserGym(userId: String): Gym?

// 사용자가 등록한 모든 헬스장 조회
    suspend fun getUserGyms(userId: String): List<Gym>

// 사용자의 내 헬스장 설정
    suspend fun setMyGym(userId: String, gymId: String)
}
