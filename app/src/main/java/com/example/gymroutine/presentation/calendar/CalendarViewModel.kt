package com.example.gymroutine.presentation.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymroutine.data.model.WorkoutRecord
import com.example.gymroutine.domain.repository.AuthRepository
import com.example.gymroutine.domain.repository.WorkoutRecordRepository
import com.example.gymroutine.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

/**
 * ViewModel for calendar screen
 */
@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val workoutRecordRepository: WorkoutRecordRepository,
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

    init {
        loadWorkoutRecords()
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
}
