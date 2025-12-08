package com.example.gymroutine.data.local

import com.example.gymroutine.data.model.Equipment
import com.example.gymroutine.data.model.Exercise

/**
 * Predefined exercise and equipment data
 */
object ExerciseData {

    // Equipment list
    val equipments = listOf(
        Equipment(
            id = "eq001",
            name = "바벨",
            category = "프리웨이트",
            targetMuscle = "전신",
            description = "가장 기본적인 웨이트 트레이닝 기구"
        ),
        Equipment(
            id = "eq002",
            name = "덤벨",
            category = "프리웨이트",
            targetMuscle = "전신",
            description = "자유롭게 움직일 수 있는 웨이트"
        ),
        Equipment(
            id = "eq003",
            name = "벤치프레스",
            category = "머신",
            targetMuscle = "가슴",
            description = "가슴 운동의 기본 기구"
        ),
        Equipment(
            id = "eq004",
            name = "스미스머신",
            category = "머신",
            targetMuscle = "전신",
            description = "고정된 궤도로 안전한 웨이트 트레이닝"
        ),
        Equipment(
            id = "eq005",
            name = "케이블머신",
            category = "머신",
            targetMuscle = "전신",
            description = "다양한 각도의 운동 가능"
        ),
        Equipment(
            id = "eq006",
            name = "레그프레스",
            category = "머신",
            targetMuscle = "하체",
            description = "하체 근력 강화 기구"
        ),
        Equipment(
            id = "eq007",
            name = "렛풀다운",
            category = "머신",
            targetMuscle = "등",
            description = "등 운동의 기본 기구"
        ),
        Equipment(
            id = "eq008",
            name = "트레드밀",
            category = "유산소",
            targetMuscle = "하체",
            description = "실내 러닝 기구"
        ),
        Equipment(
            id = "eq009",
            name = "사이클",
            category = "유산소",
            targetMuscle = "하체",
            description = "실내 자전거"
        ),
        Equipment(
            id = "eq010",
            name = "맨몸",
            category = "프리웨이트",
            targetMuscle = "전신",
            description = "기구 없이 체중을 이용한 운동"
        )
    )

    // Exercise list
    val exercises = listOf(
        // 가슴 운동
        Exercise(
            id = "ex001",
            name = "벤치프레스",
            equipmentId = "eq001",
            category = "가슴",
            description = "가슴의 대표적인 복합 운동으로, 대흉근 전체를 발달시킵니다.",
            tips = "팔꿈치를 45도 각도로 유지하고, 바벨을 가슴 중앙에 내립니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=rT7DgCr-3pg"
        ),
        Exercise(
            id = "ex002",
            name = "덤벨 플라이",
            equipmentId = "eq002",
            category = "가슴",
            description = "가슴 근육을 스트레칭하면서 발달시키는 고립 운동입니다.",
            tips = "팔꿈치를 약간 구부린 상태를 유지하고, 가슴으로 덤벨을 모읍니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=eozdVDA78K0"
        ),
        Exercise(
            id = "ex003",
            name = "인클라인 벤치프레스",
            equipmentId = "eq001",
            category = "가슴",
            description = "상부 가슴을 집중적으로 발달시키는 운동입니다.",
            tips = "벤치를 30-45도 각도로 설정하고, 상부 가슴에 집중합니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=SrqOu55lrYU"
        ),
        Exercise(
            id = "ex004",
            name = "푸시업",
            equipmentId = "eq010",
            category = "가슴",
            description = "맨몸으로 할 수 있는 기본적인 가슴 운동입니다.",
            tips = "몸을 일직선으로 유지하고, 가슴이 바닥에 닿을 때까지 내립니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=IODxDxX7oi4"
        ),

        // 등 운동
        Exercise(
            id = "ex005",
            name = "데드리프트",
            equipmentId = "eq001",
            category = "등",
            description = "등과 하체를 동시에 발달시키는 최고의 복합 운동입니다.",
            tips = "허리를 곧게 펴고, 바벨을 몸에 가깝게 유지합니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=op9kVnSso6Q"
        ),
        Exercise(
            id = "ex006",
            name = "렛풀다운",
            equipmentId = "eq007",
            category = "등",
            description = "광배근을 발달시키는 기본적인 등 운동입니다.",
            tips = "가슴을 내밀고, 팔꿈치를 뒤로 당기면서 바를 가슴 상부로 내립니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=CAwf7n6Luuc"
        ),
        Exercise(
            id = "ex007",
            name = "바벨 로우",
            equipmentId = "eq001",
            category = "등",
            description = "등의 두께를 만드는 핵심 운동입니다.",
            tips = "상체를 45도 숙이고, 바벨을 배꼽 쪽으로 당깁니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=T3N-TO4reLQ"
        ),
        Exercise(
            id = "ex008",
            name = "풀업",
            equipmentId = "eq010",
            category = "등",
            description = "체중을 이용한 최고의 등 운동입니다.",
            tips = "가슴을 바에 닿게 한다는 느낌으로 몸을 끌어올립니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=eGo4IYlbE5g"
        ),

        // 어깨 운동
        Exercise(
            id = "ex009",
            name = "오버헤드 프레스",
            equipmentId = "eq001",
            category = "어깨",
            description = "어깨 전체를 발달시키는 기본 운동입니다.",
            tips = "코어에 힘을 주고, 바벨을 머리 위로 밀어올립니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=2yjwXTZQDDI"
        ),
        Exercise(
            id = "ex010",
            name = "사이드 레터럴 레이즈",
            equipmentId = "eq002",
            category = "어깨",
            description = "측면 어깨(중간 삼각근)를 집중적으로 발달시킵니다.",
            tips = "팔꿈치를 약간 구부리고, 어깨 높이까지 덤벨을 들어올립니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=3VcKaXpzqRo"
        ),
        Exercise(
            id = "ex011",
            name = "프론트 레이즈",
            equipmentId = "eq002",
            category = "어깨",
            description = "전면 어깨를 발달시키는 고립 운동입니다.",
            tips = "덤벨을 앞으로 어깨 높이까지 들어올립니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=qsl5zDJC-LE"
        ),

        // 팔 운동
        Exercise(
            id = "ex012",
            name = "바벨 컬",
            equipmentId = "eq001",
            category = "팔",
            description = "이두근을 발달시키는 기본 운동입니다.",
            tips = "팔꿈치를 고정하고, 이두근의 수축에 집중합니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=ykJmrZ5v0Oo"
        ),
        Exercise(
            id = "ex013",
            name = "트라이셉스 익스텐션",
            equipmentId = "eq002",
            category = "팔",
            description = "삼두근을 집중적으로 발달시키는 운동입니다.",
            tips = "팔꿈치를 고정하고, 삼두근만 사용하여 팔을 펴줍니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=YbX7Wd8jQ-Q"
        ),
        Exercise(
            id = "ex014",
            name = "해머 컬",
            equipmentId = "eq002",
            category = "팔",
            description = "이두근과 상완근을 동시에 발달시킵니다.",
            tips = "손바닥이 서로 마주보게 하고 컬을 합니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=zC3nLlEvin4"
        ),

        // 하체 운동
        Exercise(
            id = "ex015",
            name = "스쿼트",
            equipmentId = "eq001",
            category = "하체",
            description = "하체의 왕 운동으로, 전체 하체를 발달시킵니다.",
            tips = "무릎이 발끝을 넘지 않게 하고, 엉덩이를 뒤로 빼면서 앉습니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=ultWZbUMPL8"
        ),
        Exercise(
            id = "ex016",
            name = "레그프레스",
            equipmentId = "eq006",
            category = "하체",
            description = "하체 근력을 안전하게 발달시킬 수 있는 머신 운동입니다.",
            tips = "무릎을 완전히 펴지 말고, 발은 어깨 너비로 벌립니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=IZxyjW7MPJQ"
        ),
        Exercise(
            id = "ex017",
            name = "런지",
            equipmentId = "eq002",
            category = "하체",
            description = "하체의 균형과 안정성을 발달시키는 운동입니다.",
            tips = "앞 무릎이 발끝을 넘지 않게 하고, 뒤 무릎이 바닥에 닿을 듯 내립니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=QOVaHwm-Q6U"
        ),
        Exercise(
            id = "ex018",
            name = "레그 컬",
            equipmentId = "eq006",
            category = "하체",
            description = "햄스트링을 집중적으로 발달시키는 고립 운동입니다.",
            tips = "무릎이 벤치 끝에 위치하게 하고, 햄스트링에 집중합니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=1Tq3QdYUuHs"
        ),

        // 코어 운동
        Exercise(
            id = "ex019",
            name = "플랭크",
            equipmentId = "eq010",
            category = "코어",
            description = "코어 전체를 강화하는 정적 운동입니다.",
            tips = "몸을 일직선으로 유지하고, 복부에 힘을 줍니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=ASdvN_XEl_c"
        ),
        Exercise(
            id = "ex020",
            name = "크런치",
            equipmentId = "eq010",
            category = "코어",
            description = "복직근을 발달시키는 기본 복부 운동입니다.",
            tips = "목에 힘을 빼고, 복부 수축에 집중합니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=Xyd_fa5zoEU"
        ),
        Exercise(
            id = "ex021",
            name = "레그 레이즈",
            equipmentId = "eq010",
            category = "코어",
            description = "하복부를 집중적으로 발달시키는 운동입니다.",
            tips = "허리를 바닥에 붙이고, 다리를 천천히 올렸다 내립니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=JB2oyawG9KI"
        ),

        // 유산소 운동
        Exercise(
            id = "ex022",
            name = "러닝",
            equipmentId = "eq008",
            category = "유산소",
            description = "가장 기본적인 유산소 운동입니다.",
            tips = "자신의 페이스를 유지하고, 호흡에 집중합니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=wCVSv7UxB2E"
        ),
        Exercise(
            id = "ex023",
            name = "사이클",
            equipmentId = "eq009",
            category = "유산소",
            description = "무릎에 부담이 적은 유산소 운동입니다.",
            tips = "저항을 적절히 조절하고, 일정한 속도를 유지합니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=SBJlZF4AdbU"
        )
    )

    /**
     * Get exercises by category
     */
    fun getExercisesByCategory(category: String): List<Exercise> {
        return exercises.filter { it.category == category }
    }

    /**
     * Get exercises by equipment
     */
    fun getExercisesByEquipment(equipmentId: String): List<Exercise> {
        return exercises.filter { it.equipmentId == equipmentId }
    }

    /**
     * Get equipment by id
     */
    fun getEquipmentById(equipmentId: String): Equipment? {
        return equipments.find { it.id == equipmentId }
    }

    /**
     * Get all categories
     */
    fun getCategories(): List<String> {
        return listOf("가슴", "등", "어깨", "팔", "하체", "코어", "유산소")
    }
}
