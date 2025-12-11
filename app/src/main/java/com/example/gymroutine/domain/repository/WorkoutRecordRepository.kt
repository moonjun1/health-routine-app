package com.example.gymroutine.domain.repository

import com.example.gymroutine.data.model.WorkoutRecord

/**
 * Workout record repository interface
 */
interface WorkoutRecordRepository {
    /**
     * Get workout records for a specific month
     * @param userId User ID
     * @param year Year (e.g., 2024)
     * @param month Month (1-12)
     */
    suspend fun getWorkoutRecordsByMonth(userId: String, year: Int, month: Int): List<WorkoutRecord>

    /**
     * Get workout records for a specific date
     * @param userId User ID
     * @param date Date in milliseconds
     */
    suspend fun getWorkoutRecordsByDate(userId: String, date: Long): List<WorkoutRecord>

    /**
     * Get all workout records for user
     */
    suspend fun getUserWorkoutRecords(userId: String): List<WorkoutRecord>

    /**
     * Get workout record by id
     */
    suspend fun getWorkoutRecordById(recordId: String): WorkoutRecord?

    /**
     * Create new workout record
     */
    suspend fun createWorkoutRecord(record: WorkoutRecord): WorkoutRecord

    /**
     * Update existing workout record
     */
    suspend fun updateWorkoutRecord(record: WorkoutRecord): WorkoutRecord

    /**
     * Delete workout record
     */
    suspend fun deleteWorkoutRecord(userId: String, recordId: String)
}
