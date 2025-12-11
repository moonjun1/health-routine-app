package com.example.gymroutine.domain.repository

import com.example.gymroutine.data.model.Equipment
import com.example.gymroutine.data.model.Exercise

// 운동 레포지토리 인터페이스
interface ExerciseRepository {
// 모든 운동 조회
    suspend fun getAllExercises(): List<Exercise>

// 카테고리별 운동 조회
    suspend fun getExercisesByCategory(category: String): List<Exercise>

// 기구별 운동 조회
    suspend fun getExercisesByEquipment(equipmentId: String): List<Exercise>

// id로 운동 조회
    suspend fun getExerciseById(id: String): Exercise?

// 모든 기구 조회
    suspend fun getAllEquipment(): List<Equipment>

// id로 기구 조회
    suspend fun getEquipmentById(id: String): Equipment?

// 모든 카테고리 조회
    suspend fun getCategories(): List<String>
}
