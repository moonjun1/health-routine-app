package com.example.gymroutine.data.remote

import com.example.gymroutine.data.model.openai.OpenAIRequest
import com.example.gymroutine.data.model.openai.OpenAIResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

// OpenAI API 서비스 인터페이스
interface OpenAIApiService {
    @POST("v1/chat/completions")
    suspend fun createChatCompletion(
        @Header("Authorization") authorization: String,
        @Header("Content-Type") contentType: String = "application/json",
        @Body request: OpenAIRequest
    ): OpenAIResponse
}
