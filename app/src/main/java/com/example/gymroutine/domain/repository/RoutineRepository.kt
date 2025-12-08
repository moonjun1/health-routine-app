package com.example.gymroutine.domain.repository

import com.example.gymroutine.data.model.Routine

/**
 * Routine repository interface
 */
interface RoutineRepository {
    /**
     * Get all routines for current user
     */
    suspend fun getUserRoutines(userId: String): List<Routine>

    /**
     * Get routine by id
     */
    suspend fun getRoutineById(routineId: String): Routine?

    /**
     * Create new routine
     */
    suspend fun createRoutine(routine: Routine): Routine

    /**
     * Update existing routine
     */
    suspend fun updateRoutine(routine: Routine): Routine

    /**
     * Delete routine
     */
    suspend fun deleteRoutine(routineId: String)
}
