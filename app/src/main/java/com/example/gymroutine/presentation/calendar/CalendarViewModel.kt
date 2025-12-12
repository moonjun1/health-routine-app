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
 * 캘린더 화면용 ViewModel
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

    private val _allWorkoutRecords = MutableStateFlow<List<WorkoutRecord>>(emptyList())

    private val _selectedDateRecords = MutableStateFlow<List<WorkoutRecord>>(emptyList())
    val selectedDateRecords: StateFlow<List<WorkoutRecord>> = _selectedDateRecords

    private val _routinesState = MutableStateFlow<Resource<List<Routine>>>(Resource.Loading)
    val routinesState: StateFlow<Resource<List<Routine>>> = _routinesState

    init {
        loadWorkoutRecords()
        loadRoutines()
        loadAllWorkoutRecords()
    }

    /**
     * 사용자 루틴 로드
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
     * 통계용 모든 운동 기록 로드
     */
    private fun loadAllWorkoutRecords() {
        viewModelScope.launch {
            try {
                val userId = authRepository.getCurrentUserId() ?: ""
                val records = workoutRecordRepository.getUserWorkoutRecords(userId)
                _allWorkoutRecords.value = records
            } catch (e: Exception) {
                _allWorkoutRecords.value = emptyList()
            }
        }
    }

    /**
     * 현재 월의 운동 기록 로드
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
     * 이전 달로 이동
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
     * 다음 달로 이동
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
     * 오늘로 이동
     */
    fun goToToday() {
        val calendar = Calendar.getInstance()
        _currentYear.value = calendar.get(Calendar.YEAR)
        _currentMonth.value = calendar.get(Calendar.MONTH) + 1
        loadWorkoutRecords()
    }

    /**
     * 특정 날짜 선택
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
     * 특정 날짜에 운동 기록이 있는지 확인
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
     * 특정 날짜의 운동 횟수 조회
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
     * 특정 날짜의 루틴 색상 조회
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

        // 이 날짜의 고유한 루틴 색상 가져오기
        return recordsForDate
            .mapNotNull { record ->
                routines.find { it.id == record.routineId }?.color
            }
            .distinct()
            .take(3) // 표시를 위해 3개 색상으로 제한
    }

    /**
     * 새 운동 기록 추가
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
                    exercises = emptyList(), // 운동 실행 기능 구현 시 채워짐
                    duration = duration,
                    notes = notes,
                    createdAt = System.currentTimeMillis()
                )

                workoutRecordRepository.createWorkoutRecord(record)
                loadWorkoutRecords() // 새 기록을 표시하기 위해 재로드
                loadAllWorkoutRecords() // 통계를 위해 모든 기록 재로드
            } catch (e: Exception) {
                // 오류 처리
            }
        }
    }

    /**
     * 운동 기록 삭제
     */
    fun deleteWorkoutRecord(recordId: String) {
        viewModelScope.launch {
            try {
                val userId = authRepository.getCurrentUserId() ?: ""
                workoutRecordRepository.deleteWorkoutRecord(userId, recordId)
                loadWorkoutRecords() // 삭제된 기록을 제거하기 위해 재로드
                loadAllWorkoutRecords() // 통계를 위해 모든 기록 재로드
                // 선택된 날짜 기록도 재로드
                _selectedDateRecords.value = _selectedDateRecords.value.filter { it.id != recordId }
            } catch (e: Exception) {
                // 오류 처리
            }
        }
    }

    /**
     * 주간 운동 횟수 조회 (현재 주)
     */
    fun getWeeklyWorkoutCount(): Int {
        val records = _allWorkoutRecords.value
        if (records.isEmpty()) return 0

        val calendar = Calendar.getInstance()

        // 현재 주의 시작일 (일요일) 가져오기
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val weekStart = calendar.timeInMillis

        // 현재 주의 종료일 (토요일) 가져오기
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
     * 월간 운동 횟수 조회 (현재 월)
     */
    fun getMonthlyWorkoutCount(): Int {
        val records = _allWorkoutRecords.value
        if (records.isEmpty()) return 0

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
     * 연속 운동 일수 조회 (현재 연속 기록)
     */
    fun getConsecutiveWorkoutDays(): Int {
        val records = _allWorkoutRecords.value
        if (records.isEmpty()) return 0

        val calendar = Calendar.getInstance()
        val today = Calendar.getInstance()
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)

        // 내림차순으로 정렬된 고유한 운동 날짜 가져오기
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

        // 오늘이나 어제 운동 기록이 있는지 확인
        val latestWorkout = workoutDates[0]
        val yesterday = today.timeInMillis - (24 * 60 * 60 * 1000)

        if (latestWorkout < yesterday) {
            // 마지막 운동이 어제 이전이면 연속 기록이 끊김
            return 0
        }

        // 연속 일수 계산
        var streak = 0
        var currentDate = latestWorkout

        for (workoutDate in workoutDates) {
            if (workoutDate == currentDate) {
                streak++
                currentDate -= (24 * 60 * 60 * 1000) // 이전 날로 이동
            } else if (workoutDate < currentDate) {
                // 간격 발견, 카운트 중지
                break
            }
        }

        return streak
    }
}
