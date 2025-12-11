package com.example.gymroutine.presentation.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymroutine.data.model.Equipment
import com.example.gymroutine.data.model.Exercise
import com.example.gymroutine.domain.repository.ExerciseRepository
import com.example.gymroutine.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 운동 목록 화면 뷰모델
@HiltViewModel
class ExerciseListViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    private val _exercisesState = MutableStateFlow<Resource<List<Exercise>>>(Resource.Idle)
    val exercisesState: StateFlow<Resource<List<Exercise>>> = _exercisesState.asStateFlow()

    private val _equipmentState = MutableStateFlow<Resource<List<Equipment>>>(Resource.Idle)
    val equipmentState: StateFlow<Resource<List<Equipment>>> = _equipmentState.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _selectedEquipment = MutableStateFlow<String?>(null)
    val selectedEquipment: StateFlow<String?> = _selectedEquipment.asStateFlow()

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories.asStateFlow()

    private var allExercises: List<Exercise> = emptyList()

    init {
        loadCategories()
        loadEquipment()
        loadExercises()
    }

    // 모든 카테고리 로드
    private fun loadCategories() {
        viewModelScope.launch {
            try {
                _categories.value = exerciseRepository.getCategories()
            } catch (e: Exception) {
                // 필요시 오류 처리
            }
        }
    }

    // 모든 기구 로드
    private fun loadEquipment() {
        viewModelScope.launch {
            _equipmentState.value = Resource.Loading
            try {
                val equipment = exerciseRepository.getAllEquipment()
                _equipmentState.value = Resource.Success(equipment)
            } catch (e: Exception) {
                _equipmentState.value = Resource.Error(e.message ?: "장비 목록을 불러올 수 없습니다")
            }
        }
    }

    // 모든 운동 로드
    fun loadExercises() {
        viewModelScope.launch {
            _exercisesState.value = Resource.Loading
            try {
                allExercises = exerciseRepository.getAllExercises()
                applyFilters()
            } catch (e: Exception) {
                _exercisesState.value = Resource.Error(e.message ?: "운동 목록을 불러올 수 없습니다")
            }
        }
    }

    // 카테고리 필터 선택
    fun selectCategory(category: String?) {
        _selectedCategory.value = category
        applyFilters()
    }

    // 기구 필터 선택
    fun selectEquipment(equipmentId: String?) {
        _selectedEquipment.value = equipmentId
        applyFilters()
    }

    // 모든 필터 초기화
    fun clearFilters() {
        _selectedCategory.value = null
        _selectedEquipment.value = null
        applyFilters()
    }

    // 현재 필터를 운동 목록에 적용
    private fun applyFilters() {
        var filteredExercises = allExercises

        // 카테고리 필터 적용
        _selectedCategory.value?.let { category ->
            filteredExercises = filteredExercises.filter { it.category == category }
        }

        // 기구 필터 적용
        _selectedEquipment.value?.let { equipmentId ->
            filteredExercises = filteredExercises.filter { it.equipmentId == equipmentId }
        }

        _exercisesState.value = Resource.Success(filteredExercises)
    }

    // ID로 기구 이름 가져오기
    fun getEquipmentName(equipmentId: String): String {
        return when (val state = _equipmentState.value) {
            is Resource.Success -> {
                state.data.find { it.id == equipmentId }?.name ?: "알 수 없음"
            }
            else -> "알 수 없음"
        }
    }
}
