package com.example.gymroutine.presentation.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymroutine.data.model.Routine
import com.example.gymroutine.data.model.WorkoutRecord
import com.example.gymroutine.domain.repository.AuthRepository
import com.example.gymroutine.domain.repository.RoutineRepository
import com.example.gymroutine.domain.repository.WorkoutRecordRepository
import com.example.gymroutine.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

/**
 * ViewModel for calendar screen
 */
@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val workoutRecordRepository: WorkoutRecordRepository,
    private val routineRepository: RoutineRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _currentYear = MutableStateFlow(Calendar.getInstance().get(Calendar.YEAR))
    val currentYear: StateFlow<Int> = _currentYear

    private val _currentMonth = MutableStateFlow(Calendar.getInstance().get(Calendar.MONTH) + 1)
    val currentMonth: StateFlow<Int> = _currentMonth

    private val _workoutRecordsState = MutableStateFlow<Resource<List<WorkoutRecord>>>(Resource.Loading)
    val workoutRecordsState: StateFlow<Resource<List<WorkoutRecord>>> = _workoutRecordsState

    private val _selectedDateRecords = MutableStateFlow<List<WorkoutRecord>>(emptyList())
    val selectedDateRecords: StateFlow<List<WorkoutRecord>> = _selectedDateRecords

    private val _routinesState = MutableStateFlow<Resource<List<Routine>>>(Resource.Loading)
    val routinesState: StateFlow<Resource<List<Routine>>> = _routinesState

    init {
        loadWorkoutRecords()
        loadRoutines()
    }

    /**
     * Load user's routines
     */
    private fun loadRoutines() {
        viewModelScope.launch {
            _routinesState.value = Resource.Loading
            try {
                val userId = authRepository.getCurrentUserId() ?: ""
                val routines = routineRepository.getUserRoutines(userId)
                _routinesState.value = Resource.Success(routines)
            } catch (e: Exception) {
                _routinesState.value = Resource.Error(e.message ?: "루틴 로드 실패")
            }
        }
    }

    /**
     * Load workout records for current month
     */
    fun loadWorkoutRecords() {
        viewModelScope.launch {
            _workoutRecordsState.value = Resource.Loading
            try {
                val userId = authRepository.getCurrentUserId() ?: ""
                val records = workoutRecordRepository.getWorkoutRecordsByMonth(
                    userId = userId,
                    year = _currentYear.value,
                    month = _currentMonth.value
                )
                _workoutRecordsState.value = Resource.Success(records)
            } catch (e: Exception) {
                _workoutRecordsState.value = Resource.Error(e.message ?: "운동 기록 로드 실패")
            }
        }
    }

    /**
     * Navigate to previous month
     */
    fun previousMonth() {
        if (_currentMonth.value == 1) {
            _currentMonth.value = 12
            _currentYear.value -= 1
        } else {
            _currentMonth.value -= 1
        }
        loadWorkoutRecords()
    }

    /**
     * Navigate to next month
     */
    fun nextMonth() {
        if (_currentMonth.value == 12) {
            _currentMonth.value = 1
            _currentYear.value += 1
        } else {
            _currentMonth.value += 1
        }
        loadWorkoutRecords()
    }

    /**
     * Go to today
     */
    fun goToToday() {
        val calendar = Calendar.getInstance()
        _currentYear.value = calendar.get(Calendar.YEAR)
        _currentMonth.value = calendar.get(Calendar.MONTH) + 1
        loadWorkoutRecords()
    }

    /**
     * Select a specific date
     */
    fun selectDate(year: Int, month: Int, day: Int) {
        viewModelScope.launch {
            try {
                val userId = authRepository.getCurrentUserId() ?: ""
                val calendar = Calendar.getInstance()
                calendar.set(year, month - 1, day, 0, 0, 0)
                calendar.set(Calendar.MILLISECOND, 0)

                val records = workoutRecordRepository.getWorkoutRecordsByDate(
                    userId = userId,
                    date = calendar.timeInMillis
                )
                _selectedDateRecords.value = records
            } catch (e: Exception) {
                _selectedDateRecords.value = emptyList()
            }
        }
    }

    /**
     * Check if a specific date has workout records
     */
    fun hasWorkoutOnDate(year: Int, month: Int, day: Int): Boolean {
        val records = (_workoutRecordsState.value as? Resource.Success)?.data ?: return false
        val calendar = Calendar.getInstance()

        return records.any { record ->
            calendar.timeInMillis = record.date
            val recordYear = calendar.get(Calendar.YEAR)
            val recordMonth = calendar.get(Calendar.MONTH) + 1
            val recordDay = calendar.get(Calendar.DAY_OF_MONTH)

            recordYear == year && recordMonth == month && recordDay == day
        }
    }

    /**
     * Get workout count for a specific date
     */
    fun getWorkoutCountOnDate(year: Int, month: Int, day: Int): Int {
        val records = (_workoutRecordsState.value as? Resource.Success)?.data ?: return 0
        val calendar = Calendar.getInstance()

        return records.count { record ->
            calendar.timeInMillis = record.date
            val recordYear = calendar.get(Calendar.YEAR)
            val recordMonth = calendar.get(Calendar.MONTH) + 1
            val recordDay = calendar.get(Calendar.DAY_OF_MONTH)

            recordYear == year && recordMonth == month && recordDay == day
        }
    }

    /**
     * Get routine colors for a specific date
     */
    fun getRoutineColorsForDate(year: Int, month: Int, day: Int): List<String> {
        val records = (_workoutRecordsState.value as? Resource.Success)?.data ?: return emptyList()
        val routines = (_routinesState.value as? Resource.Success)?.data ?: return emptyList()
        val calendar = Calendar.getInstance()

        val recordsForDate = records.filter { record ->
            calendar.timeInMillis = record.date
            val recordYear = calendar.get(Calendar.YEAR)
            val recordMonth = calendar.get(Calendar.MONTH) + 1
            val recordDay = calendar.get(Calendar.DAY_OF_MONTH)

            recordYear == year && recordMonth == month && recordDay == day
        }

        // Get unique routine colors for this date
        return recordsForDate
            .mapNotNull { record ->
                routines.find { it.id == record.routineId }?.color
            }
            .distinct()
            .take(3) // Limit to 3 colors for display
    }

    /**
     * Add a new workout record
     */
    fun addWorkoutRecord(
        date: Long,
        routineId: String,
        routineName: String,
        duration: Int,
        notes: String
    ) {
        viewModelScope.launch {
            try {
                val userId = authRepository.getCurrentUserId() ?: ""
                val record = WorkoutRecord(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    routineId = routineId,
                    routineName = routineName,
                    date = date,
                    exercises = emptyList(), // Will be filled when workout execution is implemented
                    duration = duration,
                    notes = notes,
                    createdAt = System.currentTimeMillis()
                )

                workoutRecordRepository.createWorkoutRecord(record)
                loadWorkoutRecords() // Reload to show the new record
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    /**
     * Delete a workout record
     */
    fun deleteWorkoutRecord(recordId: String) {
        viewModelScope.launch {
            try {
                val userId = authRepository.getCurrentUserId() ?: ""
                workoutRecordRepository.deleteWorkoutRecord(userId, recordId)
                loadWorkoutRecords() // Reload to remove the deleted record
                // Also reload selected date records
                _selectedDateRecords.value = _selectedDateRecords.value.filter { it.id != recordId }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    /**
     * Get weekly workout count (current week)
     */
    fun getWeeklyWorkoutCount(): Int {
        val records = (_workoutRecordsState.value as? Resource.Success)?.data ?: return 0
        val calendar = Calendar.getInstance()

        // Get start of current week (Sunday)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val weekStart = calendar.timeInMillis

        // Get end of current week (Saturday)
        calendar.add(Calendar.DAY_OF_WEEK, 6)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val weekEnd = calendar.timeInMillis

        return records.count { record ->
            record.date in weekStart..weekEnd
        }
    }

    /**
     * Get monthly workout count (current month)
     */
    fun getMonthlyWorkoutCount(): Int {
        val records = (_workoutRecordsState.value as? Resource.Success)?.data ?: return 0
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH) + 1

        return records.count { record ->
            calendar.timeInMillis = record.date
            val recordYear = calendar.get(Calendar.YEAR)
            val recordMonth = calendar.get(Calendar.MONTH) + 1

            recordYear == currentYear && recordMonth == currentMonth
        }
    }

    /**
     * Get consecutive workout days (current streak)
     */
    fun getConsecutiveWorkoutDays(): Int {
        val records = (_workoutRecordsState.value as? Resource.Success)?.data ?: return 0
        if (records.isEmpty()) return 0

        val calendar = Calendar.getInstance()
        val today = Calendar.getInstance()
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)

        // Get unique workout dates sorted in descending order
        val workoutDates = records
            .map { record ->
                calendar.timeInMillis = record.date
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                calendar.timeInMillis
            }
            .distinct()
            .sortedDescending()

        if (workoutDates.isEmpty()) return 0

        // Check if there's a workout today or yesterday
        val latestWorkout = workoutDates[0]
        val yesterday = today.timeInMillis - (24 * 60 * 60 * 1000)

        if (latestWorkout < yesterday) {
            // Streak is broken if last workout was before yesterday
            return 0
        }

        // Count consecutive days
        var streak = 0
        var currentDate = latestWorkout

        for (workoutDate in workoutDates) {
            if (workoutDate == currentDate) {
                streak++
                currentDate -= (24 * 60 * 60 * 1000) // Move to previous day
            } else if (workoutDate < currentDate) {
                // Gap found, stop counting
                break
            }
        }

        return streak
    }
}
