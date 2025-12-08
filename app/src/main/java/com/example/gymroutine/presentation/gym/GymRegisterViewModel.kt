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

    fun setSelectedGym(gym: Gym) {
        _selectedGym.value = gym
    }

    fun registerGym() {
        viewModelScope.launch {
            val gym = _selectedGym.value
            if (gym == null) {
                _registerState.value = Resource.Error("헬스장을 선택해주세요")
                return@launch
            }

            _registerState.value = Resource.Loading

            val result = registerGymUseCase(gym)

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
