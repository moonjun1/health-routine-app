package com.example.gymroutine.data.repository

import com.example.gymroutine.data.model.WorkoutRecord
import com.example.gymroutine.data.remote.FirestoreDataSource
import com.example.gymroutine.domain.repository.WorkoutRecordRepository
import java.util.Calendar
import javax.inject.Inject

// Workout record repository implementation
// Uses Firebase for logged-in users
class WorkoutRecordRepositoryImpl @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource
) : WorkoutRecordRepository {

    override suspend fun getWorkoutRecordsByMonth(
        userId: String,
        year: Int,
        month: Int
    ): List<WorkoutRecord> {
        if (userId.isEmpty()) {
            return emptyList() // For now, no local storage for guest users
        }

        val calendar = Calendar.getInstance()
        val allRecords = firestoreDataSource.getUserWorkoutRecords(userId)

        return allRecords.filter { record ->
            calendar.timeInMillis = record.date
            val recordYear = calendar.get(Calendar.YEAR)
            val recordMonth = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH is 0-based

            recordYear == year && recordMonth == month
        }
    }

    override suspend fun getWorkoutRecordsByDate(userId: String, date: Long): List<WorkoutRecord> {
        if (userId.isEmpty()) {
            return emptyList()
        }

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        val targetYear = calendar.get(Calendar.YEAR)
        val targetMonth = calendar.get(Calendar.MONTH)
        val targetDay = calendar.get(Calendar.DAY_OF_MONTH)

        val allRecords = firestoreDataSource.getUserWorkoutRecords(userId)

        return allRecords.filter { record ->
            calendar.timeInMillis = record.date
            val recordYear = calendar.get(Calendar.YEAR)
            val recordMonth = calendar.get(Calendar.MONTH)
            val recordDay = calendar.get(Calendar.DAY_OF_MONTH)

            recordYear == targetYear &&
            recordMonth == targetMonth &&
            recordDay == targetDay
        }
    }

    override suspend fun getUserWorkoutRecords(userId: String): List<WorkoutRecord> {
        if (userId.isEmpty()) {
            return emptyList()
        }
        return firestoreDataSource.getUserWorkoutRecords(userId)
    }

    override suspend fun getWorkoutRecordById(recordId: String): WorkoutRecord? {
        // This would need userId - for now return null
        return null
    }

    override suspend fun createWorkoutRecord(record: WorkoutRecord): WorkoutRecord {
        if (record.userId.isNotEmpty()) {
            firestoreDataSource.createWorkoutRecord(record.userId, record)
        }
        return record
    }

    override suspend fun updateWorkoutRecord(record: WorkoutRecord): WorkoutRecord {
        if (record.userId.isNotEmpty()) {
            firestoreDataSource.updateWorkoutRecord(record.userId, record)
        }
        return record
    }

    override suspend fun deleteWorkoutRecord(userId: String, recordId: String) {
        if (userId.isNotEmpty()) {
            firestoreDataSource.deleteWorkoutRecord(userId, recordId)
        }
    }
}
