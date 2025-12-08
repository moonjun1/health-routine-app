package com.example.gymroutine.presentation.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymroutine.domain.repository.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for exercise detail screen
 */
@HiltViewModel
class ExerciseDetailViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    private val _equipmentName = MutableStateFlow("")
    val equipmentName: StateFlow<String> = _equipmentName.asStateFlow()

    /**
     * Load equipment name by id
     */
    fun loadEquipmentName(equipmentId: String) {
        viewModelScope.launch {
            try {
                val equipment = exerciseRepository.getEquipmentById(equipmentId)
                _equipmentName.value = equipment?.name ?: "알 수 없음"
            } catch (e: Exception) {
                _equipmentName.value = "알 수 없음"
            }
        }
    }
}
