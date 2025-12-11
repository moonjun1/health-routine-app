package com.example.gymroutine.data.model

// 사용자 데이터 모델
// Firestore 컬렉션: users/{userId}
data class User(
    val id: String = "",
    val email: String = "",
    val gymId: String? = null, // Deprecated: 하위 호환성을 위해 유지
    val myGymId: String? = null, // 내 헬스장 (주로 사용하는 헬스장)
    val createdAt: Long = System.currentTimeMillis()
) {
    // Firestore용 매개변수 없는 생성자
    constructor() : this("", "", null, null, 0L)

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "email" to email,
            "gymId" to gymId,
            "myGymId" to myGymId,
            "createdAt" to createdAt
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any?>): User {
            return User(
                id = map["id"] as? String ?: "",
                email = map["email"] as? String ?: "",
                gymId = map["gymId"] as? String,
                myGymId = map["myGymId"] as? String,
                createdAt = map["createdAt"] as? Long ?: 0L
            )
        }
    }
}
