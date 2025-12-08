package com.example.gymroutine.data.repository

import com.example.gymroutine.data.model.AIRoutineRequest
import com.example.gymroutine.data.model.AIRoutineResponse
import com.example.gymroutine.data.remote.OpenAIDataSource
import com.example.gymroutine.domain.repository.AIRoutineRepository
import javax.inject.Inject

/**
 * Implementation of AIRoutineRepository
 */
class AIRoutineRepositoryImpl @Inject constructor(
    private val openAIDataSource: OpenAIDataSource
) : AIRoutineRepository {

    override suspend fun generateRoutine(request: AIRoutineRequest): AIRoutineResponse {
        return openAIDataSource.generateRoutine(request)
    }
}
