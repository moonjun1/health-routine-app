package com.example.gymroutine.domain.repository

import com.example.gymroutine.data.model.WorkoutRecord

// 운동 기록 레포지토리 인터페이스
interface WorkoutRecordRepository {
// 특정 월의 운동 기록 조회
// @param userId 사용자 ID
// @param year 연도 (예: 2024)
// @param month 월 (1-12)
    suspend fun getWorkoutRecordsByMonth(userId: String, year: Int, month: Int): List<WorkoutRecord>

// 특정 날짜의 운동 기록 조회
// @param userId 사용자 ID
// @param date 밀리초 단위 날짜
    suspend fun getWorkoutRecordsByDate(userId: String, date: Long): List<WorkoutRecord>

// 사용자의 모든 운동 기록 조회
    suspend fun getUserWorkoutRecords(userId: String): List<WorkoutRecord>

// id로 운동 기록 조회
    suspend fun getWorkoutRecordById(recordId: String): WorkoutRecord?

// 새 운동 기록 생성
    suspend fun createWorkoutRecord(record: WorkoutRecord): WorkoutRecord

// 기존 운동 기록 업데이트
    suspend fun updateWorkoutRecord(record: WorkoutRecord): WorkoutRecord

// 운동 기록 삭제
    suspend fun deleteWorkoutRecord(userId: String, recordId: String)
}
