package com.example.gymroutine.data.repository

import com.example.gymroutine.data.model.WorkoutRecord
import com.example.gymroutine.data.remote.FirestoreDataSource
import com.example.gymroutine.domain.repository.WorkoutRecordRepository
import java.util.Calendar
import javax.inject.Inject

// 운동 기록 레포지토리 구현
// 로그인 사용자는 Firebase 사용
class WorkoutRecordRepositoryImpl @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource
) : WorkoutRecordRepository {

    override suspend fun getWorkoutRecordsByMonth(
        userId: String,
        year: Int,
        month: Int
    ): List<WorkoutRecord> {
        if (userId.isEmpty()) {
            return emptyList() // 현재 게스트 사용자용 로컬 저장소 없음
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
        // userId 필요 - 현재는 null 반환
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
