package com.example.gymroutine.presentation.routine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymroutine.data.model.Routine
import com.example.gymroutine.domain.repository.RoutineRepository
import com.example.gymroutine.domain.repository.AuthRepository
import com.example.gymroutine.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for routine list screen
 */
@HiltViewModel
class RoutineListViewModel @Inject constructor(
    private val routineRepository: RoutineRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _routinesState = MutableStateFlow<Resource<List<Routine>>>(Resource.Idle)
    val routinesState: StateFlow<Resource<List<Routine>>> = _routinesState.asStateFlow()

    private val _deleteState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val deleteState: StateFlow<Resource<Unit>> = _deleteState.asStateFlow()

    init {
        loadRoutines()
    }

    /**
     * Load user's routines
     */
    fun loadRoutines() {
        viewModelScope.launch {
            _routinesState.value = Resource.Loading
            try {
                // Get userId (empty string if not logged in)
                val userId = authRepository.getCurrentUserId() ?: ""
                val routines = routineRepository.getUserRoutines(userId)
                _routinesState.value = Resource.Success(routines.sortedByDescending { it.updatedAt })
            } catch (e: Exception) {
                _routinesState.value = Resource.Error(e.message ?: "루틴 목록을 불러올 수 없습니다")
            }
        }
    }

    /**
     * Delete routine
     */
    fun deleteRoutine(routineId: String) {
        viewModelScope.launch {
            _deleteState.value = Resource.Loading
            try {
                // Get userId (empty string if not logged in)
                val userId = authRepository.getCurrentUserId() ?: ""
                routineRepository.deleteRoutine(userId, routineId)
                _deleteState.value = Resource.Success(Unit)
                // Reload routines after deletion
                loadRoutines()
            } catch (e: Exception) {
                _deleteState.value = Resource.Error(e.message ?: "루틴을 삭제할 수 없습니다")
            }
        }
    }

    /**
     * Reset delete state
     */
    fun resetDeleteState() {
        _deleteState.value = Resource.Idle
    }
}
