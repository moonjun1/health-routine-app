package com.example.gymroutine.presentation.gym

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymroutine.data.model.Gym
import com.example.gymroutine.domain.repository.AuthRepository
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
    private val registerGymUseCase: RegisterGymUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    companion object {
        private const val TAG = "GymRegisterViewModel"
    }

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
                Log.e(TAG, "registerGym: gym is null")
                return@launch
            }

            if (_selectedEquipments.value.isEmpty()) {
                _registerState.value = Resource.Error("보유 기구를 1개 이상 선택해주세요")
                Log.e(TAG, "registerGym: no equipments selected")
                return@launch
            }

            // Get current user ID
            val userId = authRepository.getCurrentUserId()
            if (userId.isNullOrEmpty()) {
                _registerState.value = Resource.Error("로그인이 필요합니다")
                Log.e(TAG, "registerGym: user not logged in")
                return@launch
            }

            _registerState.value = Resource.Loading

            // Add selected equipments and registeredBy to gym
            val gymWithEquipments = gym.copy(
                equipments = _selectedEquipments.value,
                registeredBy = userId
            )

            Log.d(TAG, "registerGym: Registering gym ${gymWithEquipments.name} for user $userId")
            Log.d(TAG, "registerGym: Equipments count: ${gymWithEquipments.equipments.size}")

            val result = registerGymUseCase(gymWithEquipments)

            _registerState.value = if (result.isSuccess) {
                Log.d(TAG, "registerGym: Success! Gym registered with ID: ${result.getOrThrow().placeId}")
                Resource.Success(result.getOrThrow())
            } else {
                val error = result.exceptionOrNull()?.message ?: "등록 실패"
                Log.e(TAG, "registerGym: Failed - $error", result.exceptionOrNull())
                Resource.Error(error)
            }
        }
    }

    fun resetState() {
        _registerState.value = Resource.Idle
    }
}
