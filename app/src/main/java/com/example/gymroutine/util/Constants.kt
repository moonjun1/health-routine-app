package com.example.gymroutine.util

object Constants {
    // Firestore Collections
    const val COLLECTION_USERS = "users"
    const val COLLECTION_GYMS = "gyms"
    const val COLLECTION_EQUIPMENTS = "equipments"
    const val COLLECTION_EXERCISES = "exercises"
    const val COLLECTION_ROUTINES = "routines"

    // Equipment Categories
    const val CATEGORY_FREE_WEIGHT = "프리웨이트"
    const val CATEGORY_MACHINE = "머신"
    const val CATEGORY_CARDIO = "유산소"

    // Exercise Categories (Body Parts)
    const val EXERCISE_CHEST = "가슴"
    const val EXERCISE_BACK = "등"
    const val EXERCISE_SHOULDER = "어깨"
    const val EXERCISE_LEGS = "하체"
    const val EXERCISE_ARMS = "팔"
    const val EXERCISE_FULL_BODY = "전신"

    // Routine Categories
    val ROUTINE_CATEGORIES = listOf(
        EXERCISE_CHEST,
        EXERCISE_BACK,
        EXERCISE_SHOULDER,
        EXERCISE_LEGS,
        EXERCISE_ARMS,
        EXERCISE_FULL_BODY
    )

    // Error Messages
    const val ERROR_NETWORK = "네트워크 연결을 확인해주세요"
    const val ERROR_AUTH_FAILED = "인증에 실패했습니다"
    const val ERROR_EMAIL_ALREADY_EXISTS = "이미 사용 중인 이메일입니다"
    const val ERROR_INVALID_EMAIL = "올바른 이메일 형식이 아닙니다"
    const val ERROR_WEAK_PASSWORD = "비밀번호는 6자 이상이어야 합니다"
    const val ERROR_WRONG_PASSWORD = "이메일 또는 비밀번호가 일치하지 않습니다"
    const val ERROR_USER_NOT_FOUND = "사용자를 찾을 수 없습니다"
    const val ERROR_UNKNOWN = "알 수 없는 오류가 발생했습니다"

    // Validation
    const val MIN_PASSWORD_LENGTH = 6
    const val EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
}
