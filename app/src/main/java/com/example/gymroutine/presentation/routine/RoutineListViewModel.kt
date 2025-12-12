package com.example.gymroutine.presentation.routine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymroutine.data.model.Routine
import com.example.gymroutine.data.model.WorkoutRecord
import com.example.gymroutine.domain.repository.RoutineRepository
import com.example.gymroutine.domain.repository.AuthRepository
import com.example.gymroutine.domain.repository.WorkoutRecordRepository
import com.example.gymroutine.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

/**
 * 루틴 목록 화면을 위한 ViewModel
 */
@HiltViewModel
class RoutineListViewModel @Inject constructor(
    private val routineRepository: RoutineRepository,
    private val authRepository: AuthRepository,
    private val workoutRecordRepository: WorkoutRecordRepository
) : ViewModel() {

    private val _routinesState = MutableStateFlow<Resource<List<Routine>>>(Resource.Idle)
    val routinesState: StateFlow<Resource<List<Routine>>> = _routinesState.asStateFlow()

    private val _deleteState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val deleteState: StateFlow<Resource<Unit>> = _deleteState.asStateFlow()

    init {
        loadRoutines()
    }

    /**
     * 사용자의 루틴 로드
     */
    fun loadRoutines() {
        viewModelScope.launch {
            _routinesState.value = Resource.Loading
            try {
                // userId 가져오기 (로그인하지 않은 경우 빈 문자열)
                val userId = authRepository.getCurrentUserId() ?: ""
                val routines = routineRepository.getUserRoutines(userId)
                _routinesState.value = Resource.Success(routines.sortedByDescending { it.updatedAt })
            } catch (e: Exception) {
                _routinesState.value = Resource.Error(e.message ?: "루틴 목록을 불러올 수 없습니다")
            }
        }
    }

    /**
     * 루틴 삭제
     */
    fun deleteRoutine(routineId: String) {
        viewModelScope.launch {
            _deleteState.value = Resource.Loading
            try {
                // userId 가져오기 (로그인하지 않은 경우 빈 문자열)
                val userId = authRepository.getCurrentUserId() ?: ""
                routineRepository.deleteRoutine(userId, routineId)
                _deleteState.value = Resource.Success(Unit)
                // 삭제 후 루틴 다시 로드
                loadRoutines()
            } catch (e: Exception) {
                _deleteState.value = Resource.Error(e.message ?: "루틴을 삭제할 수 없습니다")
            }
        }
    }

    /**
     * 삭제 상태 초기화
     */
    fun resetDeleteState() {
        _deleteState.value = Resource.Idle
    }

    /**
     * 루틴에 대한 새로운 운동 기록 추가
     */
    fun addWorkoutRecord(
        routineId: String,
        routineName: String,
        date: Long,
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
                    exercises = emptyList(),
                    duration = duration,
                    notes = notes,
                    createdAt = System.currentTimeMillis()
                )

                workoutRecordRepository.createWorkoutRecord(record)
            } catch (e: Exception) {
                // 현재는 오류를 조용히 처리
            }
        }
    }
}
