package com.example.gymroutine.presentation.routine

import androidx.lifecycle.ViewModel
import com.example.gymroutine.data.model.Exercise
import com.example.gymroutine.domain.repository.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for routine detail screen
 */
@HiltViewModel
class RoutineDetailViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    /**
     * Get exercise by id
     */
    suspend fun getExerciseById(exerciseId: String): Exercise? {
        return try {
            exerciseRepository.getExerciseById(exerciseId)
        } catch (e: Exception) {
            null
        }
    }
}
