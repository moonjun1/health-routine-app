package com.example.gymroutine.util

/**
 * Resource wrapper for handling UI states
 * Idle: Initial state, no operation
 * Success: Data loaded successfully
 * Error: Error occurred with message
 * Loading: Data is being loaded
 */
sealed class Resource<out T> {
    object Idle : Resource<Nothing>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}
