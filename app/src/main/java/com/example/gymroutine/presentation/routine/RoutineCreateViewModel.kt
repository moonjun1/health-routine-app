package com.example.gymroutine.presentation.routine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymroutine.data.model.Exercise
import com.example.gymroutine.data.model.ExerciseSet
import com.example.gymroutine.data.model.Routine
import com.example.gymroutine.domain.repository.RoutineRepository
import com.example.gymroutine.domain.repository.AuthRepository
import com.example.gymroutine.domain.repository.ExerciseRepository
import com.example.gymroutine.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

/**
 * ViewModel for routine creation with exercise selection
 */
@HiltViewModel
class RoutineCreateViewModel @Inject constructor(
    private val routineRepository: RoutineRepository,
    private val exerciseRepository: ExerciseRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _routineName = MutableStateFlow("")
    val routineName: StateFlow<String> = _routineName.asStateFlow()

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description.asStateFlow()

    private val _category = MutableStateFlow("")
    val category: StateFlow<String> = _category.asStateFlow()

    private val _selectedExercises = MutableStateFlow<List<SelectedExercise>>(emptyList())
    val selectedExercises: StateFlow<List<SelectedExercise>> = _selectedExercises.asStateFlow()

    private val _createState = MutableStateFlow<Resource<Routine>>(Resource.Idle)
    val createState: StateFlow<Resource<Routine>> = _createState.asStateFlow()

    private val _allExercises = MutableStateFlow<List<Exercise>>(emptyList())
    val allExercises: StateFlow<List<Exercise>> = _allExercises.asStateFlow()

    init {
        loadExercises()
    }

    /**
     * Load all exercises
     */
    private fun loadExercises() {
        viewModelScope.launch {
            try {
                val exercises = exerciseRepository.getAllExercises()
                _allExercises.value = exercises
            } catch (e: Exception) {
                // Handle error silently - exercises will be empty
            }
        }
    }

    /**
     * Update routine name
     */
    fun updateRoutineName(name: String) {
        _routineName.value = name
    }

    /**
     * Update description
     */
    fun updateDescription(desc: String) {
        _description.value = desc
    }

    /**
     * Update category
     */
    fun updateCategory(cat: String) {
        _category.value = cat
    }

    /**
     * Add exercise to routine
     */
    fun addExercise(exercise: Exercise) {
        val currentList = _selectedExercises.value.toMutableList()

        // Check if already added
        if (currentList.any { it.exercise.id == exercise.id }) {
            return
        }

        val selectedExercise = SelectedExercise(
            exercise = exercise,
            sets = 3,
            reps = 10,
            weight = 0.0,
            restTime = 60,
            order = currentList.size
        )

        currentList.add(selectedExercise)
        _selectedExercises.value = currentList
    }

    /**
     * Remove exercise from routine
     */
    fun removeExercise(exerciseId: String) {
        val currentList = _selectedExercises.value
            .filter { it.exercise.id != exerciseId }
            .mapIndexed { index, selectedExercise ->
                selectedExercise.copy(order = index)
            }
        _selectedExercises.value = currentList
    }

    /**
     * Update exercise settings
     */
    fun updateExerciseSettings(
        exerciseId: String,
        sets: Int? = null,
        reps: Int? = null,
        weight: Double? = null,
        restTime: Int? = null
    ) {
        val currentList = _selectedExercises.value.map { selectedExercise ->
            if (selectedExercise.exercise.id == exerciseId) {
                selectedExercise.copy(
                    sets = sets ?: selectedExercise.sets,
                    reps = reps ?: selectedExercise.reps,
                    weight = weight ?: selectedExercise.weight,
                    restTime = restTime ?: selectedExercise.restTime
                )
            } else {
                selectedExercise
            }
        }
        _selectedExercises.value = currentList
    }

    /**
     * Move exercise up in order
     */
    fun moveExerciseUp(exerciseId: String) {
        val currentList = _selectedExercises.value.toMutableList()
        val index = currentList.indexOfFirst { it.exercise.id == exerciseId }

        if (index > 0) {
            val temp = currentList[index]
            currentList[index] = currentList[index - 1].copy(order = index)
            currentList[index - 1] = temp.copy(order = index - 1)
            _selectedExercises.value = currentList
        }
    }

    /**
     * Move exercise down in order
     */
    fun moveExerciseDown(exerciseId: String) {
        val currentList = _selectedExercises.value.toMutableList()
        val index = currentList.indexOfFirst { it.exercise.id == exerciseId }

        if (index < currentList.size - 1) {
            val temp = currentList[index]
            currentList[index] = currentList[index + 1].copy(order = index)
            currentList[index + 1] = temp.copy(order = index + 1)
            _selectedExercises.value = currentList
        }
    }

    /**
     * Create routine
     */
    fun createRoutine() {
        viewModelScope.launch {
            _createState.value = Resource.Loading

            try {
                val userId = authRepository.getCurrentUserId() ?: ""

                val exerciseSets = _selectedExercises.value.map { selectedExercise ->
                    ExerciseSet(
                        exerciseId = selectedExercise.exercise.id,
                        exerciseName = selectedExercise.exercise.name,
                        sets = selectedExercise.sets,
                        reps = selectedExercise.reps,
                        weight = selectedExercise.weight,
                        restTime = selectedExercise.restTime,
                        order = selectedExercise.order
                    )
                }

                val routine = Routine(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    name = _routineName.value.trim(),
                    description = _description.value.trim(),
                    category = _category.value.trim().ifEmpty { "기타" },
                    exercises = exerciseSets,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )

                val createdRoutine = routineRepository.createRoutine(routine)
                _createState.value = Resource.Success(createdRoutine)
            } catch (e: Exception) {
                _createState.value = Resource.Error(e.message ?: "루틴 생성에 실패했습니다")
            }
        }
    }

    /**
     * Reset create state
     */
    fun resetCreateState() {
        _createState.value = Resource.Idle
    }

    /**
     * Validate if routine can be created
     */
    fun canCreateRoutine(): Boolean {
        return _routineName.value.trim().isNotEmpty() &&
               _selectedExercises.value.isNotEmpty()
    }
}

/**
 * Data class for selected exercise with settings
 */
data class SelectedExercise(
    val exercise: Exercise,
    val sets: Int,
    val reps: Int,
    val weight: Double,
    val restTime: Int,
    val order: Int
)
