package com.example.gymroutine.data.repository

import com.example.gymroutine.data.local.ExerciseData
import com.example.gymroutine.data.model.Equipment
import com.example.gymroutine.data.model.Exercise
import com.example.gymroutine.domain.repository.ExerciseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Exercise repository implementation
 */
class ExerciseRepositoryImpl @Inject constructor() : ExerciseRepository {

    override suspend fun getAllExercises(): List<Exercise> = withContext(Dispatchers.IO) {
        ExerciseData.exercises
    }

    override suspend fun getExercisesByCategory(category: String): List<Exercise> =
        withContext(Dispatchers.IO) {
            ExerciseData.getExercisesByCategory(category)
        }

    override suspend fun getExercisesByEquipment(equipmentId: String): List<Exercise> =
        withContext(Dispatchers.IO) {
            ExerciseData.getExercisesByEquipment(equipmentId)
        }

    override suspend fun getExerciseById(id: String): Exercise? = withContext(Dispatchers.IO) {
        ExerciseData.exercises.find { it.id == id }
    }

    override suspend fun getAllEquipment(): List<Equipment> = withContext(Dispatchers.IO) {
        ExerciseData.equipments
    }

    override suspend fun getEquipmentById(id: String): Equipment? = withContext(Dispatchers.IO) {
        ExerciseData.getEquipmentById(id)
    }

    override suspend fun getCategories(): List<String> = withContext(Dispatchers.IO) {
        ExerciseData.getCategories()
    }
}
