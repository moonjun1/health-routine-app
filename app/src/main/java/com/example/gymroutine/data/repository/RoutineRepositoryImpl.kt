package com.example.gymroutine.data.repository

import com.example.gymroutine.data.local.LocalRoutineDataSource
import com.example.gymroutine.data.model.Routine
import com.example.gymroutine.data.remote.FirestoreDataSource
import com.example.gymroutine.domain.repository.RoutineRepository
import javax.inject.Inject

// 루틴 레포지토리 구현
// 비로그인 시 로컬 저장소 사용, 로그인 시 Firebase 사용
class RoutineRepositoryImpl @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource,
    private val localRoutineDataSource: LocalRoutineDataSource
) : RoutineRepository {

    override suspend fun getUserRoutines(userId: String): List<Routine> {
        return if (userId.isEmpty()) {
            // 비로그인 - 로컬 저장소 사용
            localRoutineDataSource.getAllRoutines()
        } else {
            // 로그인 - Firebase 사용
            firestoreDataSource.getUserRoutines(userId)
        }
    }

    override suspend fun getRoutineById(routineId: String): Routine? {
        return null
    }

    override suspend fun createRoutine(routine: Routine): Routine {
        if (routine.userId.isEmpty()) {
            // 비로그인 - 로컬에 저장
            localRoutineDataSource.saveRoutine(routine)
        } else {
            // 로그인 - Firebase에 저장
            firestoreDataSource.createRoutine(routine.userId, routine)
        }
        return routine
    }

    override suspend fun updateRoutine(routine: Routine): Routine {
        if (routine.userId.isEmpty()) {
            // 비로그인 - 로컬에서 업데이트
            localRoutineDataSource.saveRoutine(routine)
        } else {
            // 로그인 - Firebase에서 업데이트
            firestoreDataSource.updateRoutine(routine.userId, routine)
        }
        return routine
    }

    override suspend fun deleteRoutine(routineId: String) {
        // 이 메서드는 확장 필요 - 아래 참조
    }

// userId로 루틴 삭제
    override suspend fun deleteRoutine(userId: String, routineId: String) {
        if (userId.isEmpty()) {
            // 비로그인 - 로컬에서 삭제
            localRoutineDataSource.deleteRoutine(routineId)
        } else {
            // 로그인 - Firebase에서 삭제
            firestoreDataSource.deleteRoutine(userId, routineId)
        }
    }

// userId로 루틴 ID 조회
    suspend fun getRoutineById(userId: String, routineId: String): Routine? {
        return if (userId.isEmpty()) {
            localRoutineDataSource.getRoutineById(routineId)
        } else {
            firestoreDataSource.getRoutine(userId, routineId)
        }
    }
}
