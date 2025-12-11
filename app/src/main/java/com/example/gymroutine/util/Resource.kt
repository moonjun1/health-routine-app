package com.example.gymroutine.util

// UI 상태 처리를 위한 Resource 래퍼
// Idle: 초기 상태, 작업 없음
// Success: 데이터 로드 성공
// Error: 오류 발생 및 메시지 포함
// Loading: 데이터 로딩 중
sealed class Resource<out T> {
    object Idle : Resource<Nothing>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}
