package com.example.gymroutine.data.repository

import com.example.gymroutine.data.local.LocalRoutineDataSource
import com.example.gymroutine.data.model.Routine
import com.example.gymroutine.data.remote.FirestoreDataSource
import com.example.gymroutine.domain.repository.RoutineRepository
import javax.inject.Inject

/**
 * Routine repository implementation
 * Uses local storage when not logged in, Firebase when logged in
 */
class RoutineRepositoryImpl @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource,
    private val localRoutineDataSource: LocalRoutineDataSource
) : RoutineRepository {

    override suspend fun getUserRoutines(userId: String): List<Routine> {
        return if (userId.isEmpty()) {
            // Not logged in - use local storage
            localRoutineDataSource.getAllRoutines()
        } else {
            // Logged in - use Firebase
            firestoreDataSource.getUserRoutines(userId)
        }
    }

    override suspend fun getRoutineById(routineId: String): Routine? {
        return null
    }

    override suspend fun createRoutine(routine: Routine): Routine {
        if (routine.userId.isEmpty()) {
            // Not logged in - save locally
            localRoutineDataSource.saveRoutine(routine)
        } else {
            // Logged in - save to Firebase
            firestoreDataSource.createRoutine(routine.userId, routine)
        }
        return routine
    }

    override suspend fun updateRoutine(routine: Routine): Routine {
        if (routine.userId.isEmpty()) {
            // Not logged in - update locally
            localRoutineDataSource.saveRoutine(routine)
        } else {
            // Logged in - update Firebase
            firestoreDataSource.updateRoutine(routine.userId, routine)
        }
        return routine
    }

    override suspend fun deleteRoutine(routineId: String) {
        // This method requires extension - see below
    }

    /**
     * Delete routine with userId
     */
    suspend fun deleteRoutine(userId: String, routineId: String) {
        if (userId.isEmpty()) {
            // Not logged in - delete from local
            localRoutineDataSource.deleteRoutine(routineId)
        } else {
            // Logged in - delete from Firebase
            firestoreDataSource.deleteRoutine(userId, routineId)
        }
    }

    /**
     * Get routine by id with userId
     */
    suspend fun getRoutineById(userId: String, routineId: String): Routine? {
        return if (userId.isEmpty()) {
            localRoutineDataSource.getRoutineById(routineId)
        } else {
            firestoreDataSource.getRoutine(userId, routineId)
        }
    }
}
