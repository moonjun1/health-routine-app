package com.example.gymroutine.data.repository

import com.example.gymroutine.data.model.WorkoutRecord
import com.example.gymroutine.domain.repository.WorkoutRecordRepository
import java.util.Calendar
import javax.inject.Inject

/**
 * Workout record repository implementation
 * Currently uses in-memory storage for demonstration
 * TODO: Add local database and Firebase integration
 */
class WorkoutRecordRepositoryImpl @Inject constructor() : WorkoutRecordRepository {

    // In-memory storage for demonstration
    private val workoutRecords = mutableListOf<WorkoutRecord>()

    override suspend fun getWorkoutRecordsByMonth(
        userId: String,
        year: Int,
        month: Int
    ): List<WorkoutRecord> {
        val calendar = Calendar.getInstance()

        return workoutRecords.filter { record ->
            calendar.timeInMillis = record.date
            val recordYear = calendar.get(Calendar.YEAR)
            val recordMonth = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH is 0-based

            record.userId == userId && recordYear == year && recordMonth == month
        }
    }

    override suspend fun getWorkoutRecordsByDate(userId: String, date: Long): List<WorkoutRecord> {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        val targetYear = calendar.get(Calendar.YEAR)
        val targetMonth = calendar.get(Calendar.MONTH)
        val targetDay = calendar.get(Calendar.DAY_OF_MONTH)

        return workoutRecords.filter { record ->
            calendar.timeInMillis = record.date
            val recordYear = calendar.get(Calendar.YEAR)
            val recordMonth = calendar.get(Calendar.MONTH)
            val recordDay = calendar.get(Calendar.DAY_OF_MONTH)

            record.userId == userId &&
            recordYear == targetYear &&
            recordMonth == targetMonth &&
            recordDay == targetDay
        }
    }

    override suspend fun getUserWorkoutRecords(userId: String): List<WorkoutRecord> {
        return workoutRecords.filter { it.userId == userId }
    }

    override suspend fun getWorkoutRecordById(recordId: String): WorkoutRecord? {
        return workoutRecords.find { it.id == recordId }
    }

    override suspend fun createWorkoutRecord(record: WorkoutRecord): WorkoutRecord {
        workoutRecords.add(record)
        return record
    }

    override suspend fun updateWorkoutRecord(record: WorkoutRecord): WorkoutRecord {
        val index = workoutRecords.indexOfFirst { it.id == record.id }
        if (index != -1) {
            workoutRecords[index] = record
        }
        return record
    }

    override suspend fun deleteWorkoutRecord(userId: String, recordId: String) {
        workoutRecords.removeAll { it.id == recordId && it.userId == userId }
    }
}
