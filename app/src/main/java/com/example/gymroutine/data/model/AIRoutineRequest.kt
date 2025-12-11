package com.example.gymroutine.data.model

// AI Routine generation request model
data class AIRoutineRequest(
    val goal: String, // "근력 증가", "체중 감량", "체력 향상", "근비대", "지구력"
    val experienceLevel: String, // "초보자", "중급자", "고급자"
    val workoutsPerWeek: Int, // 주당 운동 횟수 (3-6)
    val workoutDuration: Int, // 운동 시간 (분) (30-120)
    val preferredCategories: List<String>, // 선호 부위: ["가슴", "등", "어깨", "팔", "하체", "코어"]
    val equipment: List<String> = listOf("바벨", "덤벨", "머신", "케이블", "맨몸"), // 사용 가능한 기구
    val additionalInfo: String = "" // 추가 정보 (부상, 제한사항 등)
)

// AI generated routine response
data class AIRoutineResponse(
    val routineName: String,
    val description: String,
    val category: String,
    val exercises: List<AIExerciseRecommendation>
)

// AI exercise recommendation
data class AIExerciseRecommendation(
    val exerciseId: String,
    val exerciseName: String,
    val equipment: String = "", // 사용 기구 (헬스장 보유 기구 목록에서)
    val sets: Int,
    val reps: Int,
    val weight: Double = 0.0, // 0 means bodyweight or needs to be set by user
    val restTime: Int,
    val notes: String = "" // AI의 추가 설명 (자세, 주의사항 등)
)
