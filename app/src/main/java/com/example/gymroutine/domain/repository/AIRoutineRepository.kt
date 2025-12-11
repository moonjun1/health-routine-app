package com.example.gymroutine.domain.repository

import com.example.gymroutine.data.model.AIRoutineRequest
import com.example.gymroutine.data.model.AIRoutineResponse

// AI 기반 루틴 생성 레포지토리
interface AIRoutineRepository {
    suspend fun generateRoutine(request: AIRoutineRequest): AIRoutineResponse
}
