package com.example.gymroutine.data.repository

import android.util.Log
import com.example.gymroutine.data.local.GymLocalDataSource
import com.example.gymroutine.data.model.Gym
import com.example.gymroutine.data.remote.FirestoreDataSource
import com.example.gymroutine.data.remote.KakaoLocalDataSource
import com.example.gymroutine.domain.repository.AuthRepository
import com.example.gymroutine.domain.repository.GymRepository
import javax.inject.Inject

// GymRepository 구현체
// 검색을 위한 Kakao Local API와 헬스장 데이터 관리를 위한 Firestore/로컬 저장소 결합
// 사용자가 로그인하지 않은 경우 자동으로 로컬 저장소 사용
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
            Log.d(TAG, "getGymById: Firebase 사용")
            firestoreDataSource.getGym(gymId)
        } else {
            Log.d(TAG, "getGymById: 로컬 저장소 사용")
            gymLocalDataSource.getGym(gymId)
        }
    }

    override suspend fun registerGym(gym: Gym): Gym {
        return if (isLoggedIn()) {
            Log.d(TAG, "registerGym: Firebase에 저장")
            firestoreDataSource.createGym(gym)
            gym
        } else {
            Log.d(TAG, "registerGym: 로컬 저장소에 저장")
            // 비로그인 사용자는 registeredBy를 "local"로 설정
            val localGym = gym.copy(registeredBy = "local")
            gymLocalDataSource.saveGym(localGym)
            localGym
        }
    }

    override suspend fun updateGym(gym: Gym): Gym {
        return if (isLoggedIn()) {
            Log.d(TAG, "updateGym: Firebase에서 업데이트")
            firestoreDataSource.updateGym(gym)
            gym
        } else {
            Log.d(TAG, "updateGym: 로컬 저장소에서 업데이트")
            gymLocalDataSource.saveGym(gym)
            gym
        }
    }

    override suspend fun deleteGym(gymId: String) {
        if (isLoggedIn()) {
            Log.d(TAG, "deleteGym: Firebase에서 삭제")
            firestoreDataSource.deleteGym(gymId)
        } else {
            Log.d(TAG, "deleteGym: 로컬 저장소에서 삭제")
            gymLocalDataSource.deleteGym(gymId)
        }
    }

    override suspend fun getAllGyms(): List<Gym> {
        return if (isLoggedIn()) {
            Log.d(TAG, "getAllGyms: Firebase에서 로드")
            firestoreDataSource.getGyms()
        } else {
            Log.d(TAG, "getAllGyms: 로컬 저장소에서 로드")
            gymLocalDataSource.getGyms()
        }
    }

    override suspend fun getUserGym(userId: String): Gym? {
        return if (isLoggedIn()) {
            Log.d(TAG, "getUserGym: 사용자 $userId의 Firebase에서 로드")
            firestoreDataSource.getUserGym(userId)
        } else {
            Log.d(TAG, "getUserGym: 로컬 저장소에서 선택된 헬스장 로드")
            gymLocalDataSource.getSelectedGym()
        }
    }

    override suspend fun getUserGyms(userId: String): List<Gym> {
        return if (isLoggedIn()) {
            Log.d(TAG, "getUserGyms: 사용자 $userId의 Firebase에서 로드")
            firestoreDataSource.getUserGyms(userId)
        } else {
            Log.d(TAG, "getUserGyms: 로컬 저장소에서 전체 로드")
            gymLocalDataSource.getGyms()
        }
    }

    override suspend fun setMyGym(userId: String, gymId: String) {
        if (isLoggedIn()) {
            Log.d(TAG, "setMyGym: Firebase에 설정")
            firestoreDataSource.setMyGym(userId, gymId)
        } else {
            Log.d(TAG, "setMyGym: 로컬 저장소에 선택된 헬스장 설정")
            gymLocalDataSource.setSelectedGymId(gymId)
        }
    }
}
