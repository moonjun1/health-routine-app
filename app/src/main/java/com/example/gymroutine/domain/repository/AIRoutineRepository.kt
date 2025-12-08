package com.example.gymroutine.domain.repository

import com.example.gymroutine.data.model.AIRoutineRequest
import com.example.gymroutine.data.model.AIRoutineResponse

/**
 * Repository for AI-powered routine generation
 */
interface AIRoutineRepository {
    suspend fun generateRoutine(request: AIRoutineRequest): AIRoutineResponse
}
