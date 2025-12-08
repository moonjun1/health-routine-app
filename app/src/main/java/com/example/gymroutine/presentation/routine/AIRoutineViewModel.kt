package com.example.gymroutine.presentation.routine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymroutine.data.model.AIRoutineRequest
import com.example.gymroutine.data.model.AIRoutineResponse
import com.example.gymroutine.data.model.Routine
import com.example.gymroutine.data.model.ExerciseSet
import com.example.gymroutine.data.repository.RoutineRepositoryImpl
import com.example.gymroutine.domain.repository.AIRoutineRepository
import com.example.gymroutine.domain.repository.AuthRepository
import com.example.gymroutine.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

/**
 * ViewModel for AI-powered routine generation
 */
@HiltViewModel
class AIRoutineViewModel @Inject constructor(
    private val aiRoutineRepository: AIRoutineRepository,
    private val routineRepository: RoutineRepositoryImpl,
    private val authRepository: AuthRepository
) : ViewModel() {

    // User inputs
    private val _goal = MutableStateFlow("")
    val goal: StateFlow<String> = _goal.asStateFlow()

    private val _experienceLevel = MutableStateFlow("초보자")
    val experienceLevel: StateFlow<String> = _experienceLevel.asStateFlow()

    private val _workoutsPerWeek = MutableStateFlow(3)
    val workoutsPerWeek: StateFlow<Int> = _workoutsPerWeek.asStateFlow()

    private val _workoutDuration = MutableStateFlow(60)
    val workoutDuration: StateFlow<Int> = _workoutDuration.asStateFlow()

    private val _selectedCategories = MutableStateFlow<List<String>>(emptyList())
    val selectedCategories: StateFlow<List<String>> = _selectedCategories.asStateFlow()

    private val _additionalInfo = MutableStateFlow("")
    val additionalInfo: StateFlow<String> = _additionalInfo.asStateFlow()

    // Generation states
    private val _generationState = MutableStateFlow<Resource<AIRoutineResponse>>(Resource.Idle)
    val generationState: StateFlow<Resource<AIRoutineResponse>> = _generationState.asStateFlow()

    private val _saveState = MutableStateFlow<Resource<Routine>>(Resource.Idle)
    val saveState: StateFlow<Resource<Routine>> = _saveState.asStateFlow()

    /**
     * Update user inputs
     */
    fun updateGoal(value: String) {
        _goal.value = value
    }

    fun updateExperienceLevel(value: String) {
        _experienceLevel.value = value
    }

    fun updateWorkoutsPerWeek(value: Int) {
        _workoutsPerWeek.value = value
    }

    fun updateWorkoutDuration(value: Int) {
        _workoutDuration.value = value
    }

    fun toggleCategory(category: String) {
        val current = _selectedCategories.value.toMutableList()
        if (current.contains(category)) {
            current.remove(category)
        } else {
            current.add(category)
        }
        _selectedCategories.value = current
    }

    fun updateAdditionalInfo(value: String) {
        _additionalInfo.value = value
    }

    /**
     * Generate routine using GPT
     */
    fun generateRoutine() {
        viewModelScope.launch {
            _generationState.value = Resource.Loading

            try {
                val request = AIRoutineRequest(
                    goal = _goal.value,
                    experienceLevel = _experienceLevel.value,
                    workoutsPerWeek = _workoutsPerWeek.value,
                    workoutDuration = _workoutDuration.value,
                    preferredCategories = _selectedCategories.value,
                    additionalInfo = _additionalInfo.value
                )

                val response = aiRoutineRepository.generateRoutine(request)
                _generationState.value = Resource.Success(response)
            } catch (e: Exception) {
                _generationState.value = Resource.Error(
                    e.message ?: "AI 루틴 생성에 실패했습니다"
                )
            }
        }
    }

    /**
     * Save generated routine
     */
    fun saveGeneratedRoutine(aiResponse: AIRoutineResponse) {
        viewModelScope.launch {
            _saveState.value = Resource.Loading

            try {
                val userId = authRepository.getCurrentUserId() ?: ""

                val exerciseSets = aiResponse.exercises.map { aiExercise ->
                    ExerciseSet(
                        exerciseId = aiExercise.exerciseId,
                        exerciseName = aiExercise.exerciseName,
                        sets = aiExercise.sets,
                        reps = aiExercise.reps,
                        weight = aiExercise.weight,
                        restTime = aiExercise.restTime,
                        order = aiResponse.exercises.indexOf(aiExercise)
                    )
                }

                val routine = Routine(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    name = aiResponse.routineName,
                    description = aiResponse.description,
                    category = aiResponse.category,
                    exercises = exerciseSets,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )

                val savedRoutine = routineRepository.createRoutine(routine)
                _saveState.value = Resource.Success(savedRoutine)
            } catch (e: Exception) {
                _saveState.value = Resource.Error(
                    e.message ?: "루틴 저장에 실패했습니다"
                )
            }
        }
    }

    /**
     * Reset states
     */
    fun resetGenerationState() {
        _generationState.value = Resource.Idle
    }

    fun resetSaveState() {
        _saveState.value = Resource.Idle
    }

    /**
     * Validate if can generate routine
     */
    fun canGenerateRoutine(): Boolean {
        return _goal.value.isNotEmpty() &&
               _selectedCategories.value.isNotEmpty()
    }
}
