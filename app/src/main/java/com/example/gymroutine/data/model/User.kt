package com.example.gymroutine.data.model

/**
 * User data model
 * Firestore collection: users/{userId}
 */
data class User(
    val id: String = "",
    val email: String = "",
    val gymId: String? = null,
    val createdAt: Long = System.currentTimeMillis()
) {
    // No-arg constructor for Firestore
    constructor() : this("", "", null, 0L)

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "email" to email,
            "gymId" to gymId,
            "createdAt" to createdAt
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any?>): User {
            return User(
                id = map["id"] as? String ?: "",
                email = map["email"] as? String ?: "",
                gymId = map["gymId"] as? String,
                createdAt = map["createdAt"] as? Long ?: 0L
            )
        }
    }
}
