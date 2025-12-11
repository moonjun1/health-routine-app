package com.example.gymroutine.domain.repository

import com.example.gymroutine.data.model.Routine

// 루틴 레포지토리 인터페이스
interface RoutineRepository {
// 현재 사용자의 모든 루틴 조회
    suspend fun getUserRoutines(userId: String): List<Routine>

// id로 루틴 조회
    suspend fun getRoutineById(routineId: String): Routine?

// 새 루틴 생성
    suspend fun createRoutine(routine: Routine): Routine

// 기존 루틴 업데이트
    suspend fun updateRoutine(routine: Routine): Routine

// 루틴 삭제
    suspend fun deleteRoutine(routineId: String)

// userId로 루틴 삭제
    suspend fun deleteRoutine(userId: String, routineId: String)
}
