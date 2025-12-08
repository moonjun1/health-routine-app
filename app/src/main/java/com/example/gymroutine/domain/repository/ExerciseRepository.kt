package com.example.gymroutine.domain.repository

import com.example.gymroutine.data.model.Equipment
import com.example.gymroutine.data.model.Exercise

/**
 * Exercise repository interface
 */
interface ExerciseRepository {
    /**
     * Get all exercises
     */
    suspend fun getAllExercises(): List<Exercise>

    /**
     * Get exercises by category
     */
    suspend fun getExercisesByCategory(category: String): List<Exercise>

    /**
     * Get exercises by equipment
     */
    suspend fun getExercisesByEquipment(equipmentId: String): List<Exercise>

    /**
     * Get exercise by id
     */
    suspend fun getExerciseById(id: String): Exercise?

    /**
     * Get all equipment
     */
    suspend fun getAllEquipment(): List<Equipment>

    /**
     * Get equipment by id
     */
    suspend fun getEquipmentById(id: String): Equipment?

    /**
     * Get all categories
     */
    suspend fun getCategories(): List<String>
}
