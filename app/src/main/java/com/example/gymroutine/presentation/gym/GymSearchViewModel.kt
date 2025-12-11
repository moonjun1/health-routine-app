package com.example.gymroutine.presentation.gym

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymroutine.data.model.Gym
import com.example.gymroutine.domain.usecase.gym.SearchGymsByKeywordUseCase
import com.example.gymroutine.domain.usecase.gym.SearchNearbyGymsUseCase
import com.example.gymroutine.util.LocationHelper
import com.example.gymroutine.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 헬스장 검색 화면 뷰모델
@HiltViewModel
class GymSearchViewModel @Inject constructor(
    private val searchNearbyGymsUseCase: SearchNearbyGymsUseCase,
    private val searchGymsByKeywordUseCase: SearchGymsByKeywordUseCase,
    private val locationHelper: LocationHelper
) : ViewModel() {

    private val _searchState = MutableStateFlow<Resource<List<Gym>>>(Resource.Idle)
    val searchState: StateFlow<Resource<List<Gym>>> = _searchState.asStateFlow()

    private val _searchKeyword = MutableStateFlow("")
    val searchKeyword: StateFlow<String> = _searchKeyword.asStateFlow()

    private val _hasLocationPermission = MutableStateFlow(false)
    val hasLocationPermission: StateFlow<Boolean> = _hasLocationPermission.asStateFlow()

    private val _currentLocation = MutableStateFlow<Pair<Double, Double>?>(null)
    val currentLocation: StateFlow<Pair<Double, Double>?> = _currentLocation.asStateFlow()

    init {
        checkLocationPermission()
    }

    fun onSearchKeywordChange(keyword: String) {
        _searchKeyword.value = keyword
    }

    fun checkLocationPermission() {
        _hasLocationPermission.value = locationHelper.hasLocationPermission()
    }

    fun getCurrentLocation() {
        viewModelScope.launch {
            try {
                val location = locationHelper.getCurrentLocation()
                if (location != null) {
                    _currentLocation.value = Pair(location.latitude, location.longitude)
                }
            } catch (e: Exception) {
                // 오류를 조용히 처리
            }
        }
    }

    fun searchNearbyGyms(radius: Int = 5000) {
        viewModelScope.launch {
            _searchState.value = Resource.Loading

            val location = _currentLocation.value
            if (location == null) {
                _searchState.value = Resource.Error("위치 정보를 가져올 수 없습니다")
                return@launch
            }

            val result = searchNearbyGymsUseCase(
                latitude = location.first,
                longitude = location.second,
                radius = radius
            )

            _searchState.value = if (result.isSuccess) {
                Resource.Success(result.getOrThrow())
            } else {
                Resource.Error(result.exceptionOrNull()?.message ?: "검색 실패")
            }
        }
    }

    fun searchByKeyword() {
        viewModelScope.launch {
            _searchState.value = Resource.Loading

            val location = _currentLocation.value
            val result = searchGymsByKeywordUseCase(
                keyword = _searchKeyword.value,
                latitude = location?.first,
                longitude = location?.second
            )

            _searchState.value = if (result.isSuccess) {
                Resource.Success(result.getOrThrow())
            } else {
                Resource.Error(result.exceptionOrNull()?.message ?: "검색 실패")
            }
        }
    }

    fun resetState() {
        _searchState.value = Resource.Idle
    }
}
