package com.example.gymroutine.data.model

// 헬스장 기구 카테고리 및 항목
object GymEquipment {

// 표준 헬스장 기구 목록 조회
    fun getStandardEquipmentList(): List<String> {
        return listOf(
            // 프리 웨이트
            "바벨 (Barbell)",
            "덤벨 (Dumbbell)",
            "EZ 바 (EZ Bar)",
            "케틀벨 (Kettlebell)",

            // 가슴
            "벤치프레스 (Bench Press)",
            "인클라인 벤치프레스 (Incline Bench Press)",
            "체스트 프레스 머신 (Chest Press Machine)",
            "펙 덱 플라이 (Pec Deck Fly)",
            "케이블 크로스오버 (Cable Crossover)",

            // 등
            "풀업 바 (Pull-up Bar)",
            "랫 풀다운 머신 (Lat Pulldown Machine)",
            "로우 머신 (Row Machine)",
            "케이블 로우 (Cable Row)",

            // 어깨
            "숄더 프레스 머신 (Shoulder Press Machine)",
            "래터럴 레이즈 머신 (Lateral Raise Machine)",

            // 하체
            "스쿼트 랙 (Squat Rack)",
            "레그 프레스 (Leg Press)",
            "레그 익스텐션 (Leg Extension)",
            "레그 컬 (Leg Curl)",
            "칼프 레이즈 머신 (Calf Raise Machine)",

            // 팔
            "케이블 머신 (Cable Machine)",
            "프리처 컬 벤치 (Preacher Curl Bench)",

            // 유산소
            "런닝머신 (Treadmill)",
            "사이클 (Stationary Bike)",
            "로잉 머신 (Rowing Machine)",

            // 기타
            "스미스 머신 (Smith Machine)",
            "멀티 케이블 머신 (Multi Cable Machine)"
        )
    }

// AI 프롬프트용 기구 설명 조회
    fun getEquipmentDescription(): String {
        return """
            헬스장 보유 기구 목록:

            [프리 웨이트]
            - 바벨, 덤벨, EZ 바, 케틀벨

            [가슴 운동 기구]
            - 벤치프레스, 인클라인 벤치프레스, 체스트 프레스 머신, 펙 덱 플라이, 케이블 크로스오버

            [등 운동 기구]
            - 풀업 바, 랫 풀다운 머신, 로우 머신, 케이블 로우

            [어깨 운동 기구]
            - 숄더 프레스 머신, 래터럴 레이즈 머신

            [하체 운동 기구]
            - 스쿼트 랙, 레그 프레스, 레그 익스텐션, 레그 컬, 칼프 레이즈 머신

            [팔 운동 기구]
            - 케이블 머신, 프리처 컬 벤치

            [유산소 기구]
            - 런닝머신, 사이클, 로잉 머신

            [기타]
            - 스미스 머신, 멀티 케이블 머신
        """.trimIndent()
    }

// 경험 수준별 가이드라인 조회
    fun getExperienceLevelGuidelines(): String {
        return """
            경험 수준별 가이드라인:

            [초보자]
            - 기본 동작 위주 (스쿼트, 벤치프레스, 데드리프트 등)
            - 가벼운 무게로 시작 (본인 체중의 30-50%)
            - 세트당 8-12회 반복
            - 3세트 정도
            - 충분한 휴식 (90-120초)
            - 머신 운동 위주로 안전하게
            - 폼(자세) 익히기에 집중

            [중급자]
            - 기본 + 중급 동작 조합
            - 중간 무게 (본인 체중의 60-80%)
            - 세트당 6-15회 반복
            - 3-4세트
            - 적절한 휴식 (60-90초)
            - 프리 웨이트와 머신 혼합
            - 다양한 각도와 그립 변형

            [고급자]
            - 고급 기술 및 복합 운동
            - 무거운 무게 (본인 체중의 80-100%+)
            - 세트당 4-12회 반복
            - 4-5세트
            - 짧은 휴식 (45-60초) 또는 긴 휴식 (120초+, 고중량 시)
            - 프리 웨이트 중심
            - 슈퍼셋, 드롭셋 등 고급 기법 활용
        """.trimIndent()
    }
}
