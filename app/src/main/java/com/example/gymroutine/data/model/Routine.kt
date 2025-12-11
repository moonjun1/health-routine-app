package com.example.gymroutine.data.model

// Routine data model
// Firestore collection: routines/{routineId}
data class Routine(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val description: String = "",
    val category: String = "",
    val exercises: List<ExerciseSet> = emptyList(),
    val color: String = "#2196F3", // Default blue color
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    constructor() : this("", "", "", "", "", emptyList(), "#2196F3", 0L, 0L)

    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "userId" to userId,
            "name" to name,
            "description" to description,
            "category" to category,
            "exercises" to exercises.map { it.toMap() },
            "color" to color,
            "createdAt" to createdAt,
            "updatedAt" to updatedAt
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any?>): Routine {
            val exercisesList = (map["exercises"] as? List<*>)?.mapNotNull { exerciseMap ->
                (exerciseMap as? Map<*, *>)?.let { m ->
                    @Suppress("UNCHECKED_CAST")
                    ExerciseSet.fromMap(m as Map<String, Any?>)
                }
            } ?: emptyList()

            return Routine(
                id = map["id"] as? String ?: "",
                userId = map["userId"] as? String ?: "",
                name = map["name"] as? String ?: "",
                description = map["description"] as? String ?: "",
                category = map["category"] as? String ?: "",
                exercises = exercisesList,
                color = map["color"] as? String ?: "#2196F3",
                createdAt = (map["createdAt"] as? Number)?.toLong() ?: 0L,
                updatedAt = (map["updatedAt"] as? Number)?.toLong() ?: 0L
            )
        }
    }
}

// Exercise set information within a routine
data class ExerciseSet(
    val exerciseId: String = "",
    val exerciseName: String = "",
    val sets: Int = 3,
    val reps: Int = 10,
    val weight: Double = 0.0,
    val restTime: Int = 60, // seconds
    val order: Int = 0
) {
    constructor() : this("", "", 3, 10, 0.0, 60, 0)

    fun toMap(): Map<String, Any> {
        return mapOf(
            "exerciseId" to exerciseId,
            "exerciseName" to exerciseName,
            "sets" to sets,
            "reps" to reps,
            "weight" to weight,
            "restTime" to restTime,
            "order" to order
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any?>): ExerciseSet {
            return ExerciseSet(
                exerciseId = map["exerciseId"] as? String ?: "",
                exerciseName = map["exerciseName"] as? String ?: "",
                sets = (map["sets"] as? Number)?.toInt() ?: 3,
                reps = (map["reps"] as? Number)?.toInt() ?: 10,
                weight = (map["weight"] as? Number)?.toDouble() ?: 0.0,
                restTime = (map["restTime"] as? Number)?.toInt() ?: 60,
                order = (map["order"] as? Number)?.toInt() ?: 0
            )
        }
    }
}
