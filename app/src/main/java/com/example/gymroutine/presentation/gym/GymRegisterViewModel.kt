package com.example.gymroutine.presentation.gym

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymroutine.data.model.Gym
import com.example.gymroutine.domain.usecase.gym.RegisterGymUseCase
import com.example.gymroutine.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for gym registration screen
 */
@HiltViewModel
class GymRegisterViewModel @Inject constructor(
    private val registerGymUseCase: RegisterGymUseCase
) : ViewModel() {

    private val _registerState = MutableStateFlow<Resource<Gym>>(Resource.Idle)
    val registerState: StateFlow<Resource<Gym>> = _registerState.asStateFlow()

    private val _selectedGym = MutableStateFlow<Gym?>(null)
    val selectedGym: StateFlow<Gym?> = _selectedGym.asStateFlow()

    private val _selectedEquipments = MutableStateFlow<List<String>>(emptyList())
    val selectedEquipments: StateFlow<List<String>> = _selectedEquipments.asStateFlow()

    fun setSelectedGym(gym: Gym) {
        _selectedGym.value = gym
        // Pre-fill equipments if already registered
        _selectedEquipments.value = gym.equipments
    }

    fun toggleEquipment(equipment: String) {
        val current = _selectedEquipments.value.toMutableList()
        if (current.contains(equipment)) {
            current.remove(equipment)
        } else {
            current.add(equipment)
        }
        _selectedEquipments.value = current
    }

    fun registerGym() {
        viewModelScope.launch {
            val gym = _selectedGym.value
            if (gym == null) {
                _registerState.value = Resource.Error("헬스장을 선택해주세요")
                return@launch
            }

            if (_selectedEquipments.value.isEmpty()) {
                _registerState.value = Resource.Error("보유 기구를 1개 이상 선택해주세요")
                return@launch
            }

            _registerState.value = Resource.Loading

            // Add selected equipments to gym
            val gymWithEquipments = gym.copy(equipments = _selectedEquipments.value)

            val result = registerGymUseCase(gymWithEquipments)

            _registerState.value = if (result.isSuccess) {
                Resource.Success(result.getOrThrow())
            } else {
                Resource.Error(result.exceptionOrNull()?.message ?: "등록 실패")
            }
        }
    }

    fun resetState() {
        _registerState.value = Resource.Idle
    }
}
