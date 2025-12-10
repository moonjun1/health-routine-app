package com.example.gymroutine.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymroutine.data.model.Gym
import com.example.gymroutine.data.model.Routine
import com.example.gymroutine.data.model.User
import com.example.gymroutine.data.repository.GymRepositoryImpl
import com.example.gymroutine.data.repository.RoutineRepositoryImpl
import com.example.gymroutine.data.repository.UserRepositoryImpl
import com.example.gymroutine.domain.repository.AuthRepository
import com.example.gymroutine.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Home screen ViewModel
 * Manages user info, gym info, and recent routines
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepositoryImpl,
    private val gymRepository: GymRepositoryImpl,
    private val routineRepository: RoutineRepositoryImpl
) : ViewModel() {

    private val _userState = MutableStateFlow<Resource<User>>(Resource.Idle)
    val userState: StateFlow<Resource<User>> = _userState.asStateFlow()

    private val _gymState = MutableStateFlow<Resource<Gym>>(Resource.Idle)
    val gymState: StateFlow<Resource<Gym>> = _gymState.asStateFlow()

    private val _userGymsState = MutableStateFlow<Resource<List<Gym>>>(Resource.Idle)
    val userGymsState: StateFlow<Resource<List<Gym>>> = _userGymsState.asStateFlow()

    private val _recentRoutinesState = MutableStateFlow<Resource<List<Routine>>>(Resource.Idle)
    val recentRoutinesState: StateFlow<Resource<List<Routine>>> = _recentRoutinesState.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        val currentUser = authRepository.getCurrentUser()
        _isLoggedIn.value = currentUser != null

        if (currentUser != null) {
            loadUserData()
            loadUserGyms()
            loadRecentRoutines()
        }
    }

    private fun loadUserGyms() {
        viewModelScope.launch {
            val userId = authRepository.getCurrentUserId() ?: return@launch

            _userGymsState.value = Resource.Loading
            try {
                val gyms = gymRepository.getUserGyms(userId)
                _userGymsState.value = if (gyms.isNotEmpty()) {
                    Resource.Success(gyms)
                } else {
                    Resource.Error("등록된 헬스장이 없습니다")
                }
            } catch (e: Exception) {
                _userGymsState.value = Resource.Error(e.message ?: "헬스장 목록 로드 실패")
            }
        }
    }

    fun loadUserData() {
        viewModelScope.launch {
            val userId = authRepository.getCurrentUserId() ?: return@launch

            _userState.value = Resource.Loading
            try {
                val user = userRepository.getUser(userId)
                if (user != null) {
                    _userState.value = Resource.Success(user)

                    // Load gym info if user has a gym
                    if (!user.gymId.isNullOrEmpty()) {
                        loadGymData(user.gymId)
                    }
                } else {
                    _userState.value = Resource.Error("사용자 정보를 찾을 수 없습니다")
                }
            } catch (e: Exception) {
                _userState.value = Resource.Error(e.message ?: "사용자 정보 로드 실패")
            }
        }
    }

    private fun loadGymData(gymId: String) {
        viewModelScope.launch {
            _gymState.value = Resource.Loading
            try {
                val gym = gymRepository.getGymById(gymId)
                if (gym != null) {
                    _gymState.value = Resource.Success(gym)
                } else {
                    _gymState.value = Resource.Error("헬스장 정보를 찾을 수 없습니다")
                }
            } catch (e: Exception) {
                _gymState.value = Resource.Error(e.message ?: "헬스장 정보 로드 실패")
            }
        }
    }

    fun loadRecentRoutines() {
        viewModelScope.launch {
            val userId = authRepository.getCurrentUserId()

            _recentRoutinesState.value = Resource.Loading
            try {
                val routines = if (userId != null) {
                    // Logged in: Load from Firebase
                    routineRepository.getUserRoutines(userId)
                } else {
                    // Not logged in: Load from local storage
                    routineRepository.getUserRoutines("")
                }

                // Get top 3 most recent routines
                val recentRoutines = routines
                    .sortedByDescending { it.updatedAt }
                    .take(3)

                _recentRoutinesState.value = Resource.Success(recentRoutines)
            } catch (e: Exception) {
                _recentRoutinesState.value = Resource.Error(e.message ?: "루틴 로드 실패")
            }
        }
    }

    /**
     * Check if gym is currently open
     */
    fun isGymOpen(gym: Gym): Boolean {
        val calendar = Calendar.getInstance()
        val dayOfWeek = when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> "mon"
            Calendar.TUESDAY -> "tue"
            Calendar.WEDNESDAY -> "wed"
            Calendar.THURSDAY -> "thu"
            Calendar.FRIDAY -> "fri"
            Calendar.SATURDAY -> "sat"
            Calendar.SUNDAY -> "sun"
            else -> "mon"
        }

        val hours = gym.hours[dayOfWeek] ?: return false

        val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        return currentTime >= hours.open && currentTime <= hours.close
    }

    /**
     * Get gym operating hours for today
     */
    fun getTodayHours(gym: Gym): String {
        val calendar = Calendar.getInstance()
        val dayOfWeek = when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> "mon"
            Calendar.TUESDAY -> "tue"
            Calendar.WEDNESDAY -> "wed"
            Calendar.THURSDAY -> "thu"
            Calendar.FRIDAY -> "fri"
            Calendar.SATURDAY -> "sat"
            Calendar.SUNDAY -> "sun"
            else -> "mon"
        }

        val hours = gym.hours[dayOfWeek]
        return if (hours != null) {
            "${hours.open} - ${hours.close}"
        } else {
            "운영 시간 정보 없음"
        }
    }

    fun refresh() {
        checkLoginStatus()
    }
}
