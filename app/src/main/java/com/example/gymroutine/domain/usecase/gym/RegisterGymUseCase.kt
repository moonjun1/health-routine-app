package com.example.gymroutine.domain.usecase.gym

import com.example.gymroutine.data.model.Gym
import com.example.gymroutine.domain.repository.GymRepository
import javax.inject.Inject

/**
 * Use case for registering new gym
 */
class RegisterGymUseCase @Inject constructor(
    private val gymRepository: GymRepository
) {
    suspend operator fun invoke(gym: Gym): Result<Gym> {
        return try {
            // Validate gym data
            if (gym.name.isBlank()) {
                return Result.failure(Exception("헬스장 이름을 입력해주세요"))
            }
            if (gym.address.isBlank()) {
                return Result.failure(Exception("주소를 입력해주세요"))
            }
            if (gym.latitude !in -90.0..90.0) {
                return Result.failure(Exception("올바른 위치를 선택해주세요"))
            }
            if (gym.longitude !in -180.0..180.0) {
                return Result.failure(Exception("올바른 위치를 선택해주세요"))
            }

            val registeredGym = gymRepository.registerGym(gym)
            Result.success(registeredGym)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
