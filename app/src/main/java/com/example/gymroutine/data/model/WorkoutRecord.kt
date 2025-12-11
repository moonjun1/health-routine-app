package com.example.gymroutine.data.model

// Workout record data model
// Firestore collection: workout_records/{recordId}
data class WorkoutRecord(
    val id: String = "",
    val userId: String = "",
    val routineId: String = "",
    val routineName: String = "",
    val date: Long = System.currentTimeMillis(),
    val exercises: List<CompletedExercise> = emptyList(),
    val duration: Int = 0, // 운동 시간 (분)
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
) {
    constructor() : this("", "", "", "", 0L, emptyList(), 0, "", 0L)

    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "userId" to userId,
            "routineId" to routineId,
            "routineName" to routineName,
            "date" to date,
            "exercises" to exercises.map { it.toMap() },
            "duration" to duration,
            "notes" to notes,
            "createdAt" to createdAt
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any?>): WorkoutRecord {
            val exercisesList = (map["exercises"] as? List<*>)?.mapNotNull { exerciseMap ->
                (exerciseMap as? Map<*, *>)?.let { m ->
                    @Suppress("UNCHECKED_CAST")
                    CompletedExercise.fromMap(m as Map<String, Any?>)
                }
            } ?: emptyList()

            return WorkoutRecord(
                id = map["id"] as? String ?: "",
                userId = map["userId"] as? String ?: "",
                routineId = map["routineId"] as? String ?: "",
                routineName = map["routineName"] as? String ?: "",
                date = (map["date"] as? Number)?.toLong() ?: 0L,
                exercises = exercisesList,
                duration = (map["duration"] as? Number)?.toInt() ?: 0,
                notes = map["notes"] as? String ?: "",
                createdAt = (map["createdAt"] as? Number)?.toLong() ?: 0L
            )
        }
    }
}

// Completed exercise within a workout record
data class CompletedExercise(
    val exerciseId: String = "",
    val exerciseName: String = "",
    val completedSets: List<CompletedSet> = emptyList()
) {
    constructor() : this("", "", emptyList())

    fun toMap(): Map<String, Any> {
        return mapOf(
            "exerciseId" to exerciseId,
            "exerciseName" to exerciseName,
            "completedSets" to completedSets.map { it.toMap() }
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any?>): CompletedExercise {
            val setsList = (map["completedSets"] as? List<*>)?.mapNotNull { setMap ->
                (setMap as? Map<*, *>)?.let { m ->
                    @Suppress("UNCHECKED_CAST")
                    CompletedSet.fromMap(m as Map<String, Any?>)
                }
            } ?: emptyList()

            return CompletedExercise(
                exerciseId = map["exerciseId"] as? String ?: "",
                exerciseName = map["exerciseName"] as? String ?: "",
                completedSets = setsList
            )
        }
    }
}

// Completed set information
data class CompletedSet(
    val reps: Int = 0,
    val weight: Double = 0.0,
    val completed: Boolean = true
) {
    constructor() : this(0, 0.0, true)

    fun toMap(): Map<String, Any> {
        return mapOf(
            "reps" to reps,
            "weight" to weight,
            "completed" to completed
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any?>): CompletedSet {
            return CompletedSet(
                reps = (map["reps"] as? Number)?.toInt() ?: 0,
                weight = (map["weight"] as? Number)?.toDouble() ?: 0.0,
                completed = map["completed"] as? Boolean ?: true
            )
        }
    }
}
