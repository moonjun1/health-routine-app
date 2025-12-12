package com.example.gymroutine.presentation.routine

import androidx.lifecycle.ViewModel
import com.example.gymroutine.data.model.Exercise
import com.example.gymroutine.domain.repository.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 루틴 상세 화면을 위한 ViewModel
 */
@HiltViewModel
class RoutineDetailViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    /**
     * ID로 운동 가져오기
     */
    suspend fun getExerciseById(exerciseId: String): Exercise? {
        return try {
            exerciseRepository.getExerciseById(exerciseId)
        } catch (e: Exception) {
            null
        }
    }
}
