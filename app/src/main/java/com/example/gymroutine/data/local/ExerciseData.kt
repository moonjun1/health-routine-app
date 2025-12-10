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
        Exercise(
            id = "ex024",
            name = "디클라인 벤치프레스",
            equipmentId = "eq001",
            category = "가슴",
            description = "하부 가슴을 집중적으로 발달시키는 운동입니다.",
            tips = "벤치를 15-30도 아래로 설정하고, 하부 가슴에 집중합니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=LfyQBUKR8SE"
        ),
        Exercise(
            id = "ex025",
            name = "덤벨 프레스",
            equipmentId = "eq002",
            category = "가슴",
            description = "가슴의 안정성과 균형을 발달시키는 운동입니다.",
            tips = "덤벨을 가슴 위에서 수직으로 밀어올립니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=VmB1G1K7v94"
        ),
        Exercise(
            id = "ex026",
            name = "케이블 크로스오버",
            equipmentId = "eq005",
            category = "가슴",
            description = "가슴 중앙과 내측을 집중적으로 자극하는 운동입니다.",
            tips = "케이블을 가슴 중앙으로 모으면서 수축에 집중합니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=taI4XduLpTk"
        ),
        Exercise(
            id = "ex027",
            name = "체스트 프레스 머신",
            equipmentId = "eq003",
            category = "가슴",
            description = "가슴을 안전하게 발달시킬 수 있는 머신 운동입니다.",
            tips = "등을 시트에 밀착하고, 가슴 수축에 집중합니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=xUm0BiZCWlQ"
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
        Exercise(
            id = "ex028",
            name = "덤벨 로우",
            equipmentId = "eq002",
            category = "등",
            description = "한쪽씩 집중하여 등의 균형을 발달시키는 운동입니다.",
            tips = "벤치에 한 손과 무릎을 대고, 덤벨을 옆구리로 당깁니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=roCP6wCXPqo"
        ),
        Exercise(
            id = "ex029",
            name = "시티드 로우",
            equipmentId = "eq005",
            category = "등",
            description = "등 중앙부를 집중적으로 발달시키는 케이블 운동입니다.",
            tips = "가슴을 펴고, 팔꿈치를 몸 쪽으로 당기며 어깨뼈를 모읍니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=GZbfZ033f74"
        ),
        Exercise(
            id = "ex030",
            name = "T-바 로우",
            equipmentId = "eq001",
            category = "등",
            description = "등의 두께와 밀도를 높이는 운동입니다.",
            tips = "무릎을 약간 구부리고, 바를 가슴 쪽으로 당깁니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=j3Igk5zpAOY"
        ),
        Exercise(
            id = "ex031",
            name = "친업",
            equipmentId = "eq010",
            category = "등",
            description = "이두근 참여가 많은 등 운동입니다.",
            tips = "손바닥이 얼굴을 향하게 하고, 가슴을 바에 가깝게 당깁니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=iywjgr-0kCg"
        ),
        Exercise(
            id = "ex032",
            name = "페이스 풀",
            equipmentId = "eq005",
            category = "등",
            description = "후면 어깨와 상부 등을 발달시키는 운동입니다.",
            tips = "케이블을 얼굴 쪽으로 당기면서 팔꿈치를 높게 유지합니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=rep-qVOkqgk"
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
        Exercise(
            id = "ex033",
            name = "덤벨 숄더 프레스",
            equipmentId = "eq002",
            category = "어깨",
            description = "어깨의 안정성을 높이면서 발달시키는 운동입니다.",
            tips = "덤벨을 귀 옆에서 시작하여 머리 위로 밀어올립니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=qEwKCR5JCog"
        ),
        Exercise(
            id = "ex034",
            name = "리어 델트 플라이",
            equipmentId = "eq002",
            category = "어깨",
            description = "후면 어깨를 집중적으로 발달시키는 운동입니다.",
            tips = "상체를 숙이고, 덤벨을 옆으로 들어올립니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=EA7u4Q_8jQ0"
        ),
        Exercise(
            id = "ex035",
            name = "아놀드 프레스",
            equipmentId = "eq002",
            category = "어깨",
            description = "어깨 전체를 자극하는 복합 운동입니다.",
            tips = "손바닥을 얼굴 쪽으로 시작하여 회전하며 프레스합니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=6Z15_WdXmVw"
        ),
        Exercise(
            id = "ex036",
            name = "케이블 레터럴 레이즈",
            equipmentId = "eq005",
            category = "어깨",
            description = "측면 어깨에 지속적인 긴장을 주는 운동입니다.",
            tips = "케이블을 몸 반대편에서 잡고 옆으로 들어올립니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=PPrzBWZDOhA"
        ),
        Exercise(
            id = "ex037",
            name = "업라이트 로우",
            equipmentId = "eq001",
            category = "어깨",
            description = "어깨와 승모근을 함께 발달시키는 운동입니다.",
            tips = "바벨을 몸에 가깝게 하여 턱 높이까지 당깁니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=c4qGmf2fUy0"
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
        Exercise(
            id = "ex038",
            name = "덤벨 컬",
            equipmentId = "eq002",
            category = "팔",
            description = "각 팔의 균형있는 발달을 위한 운동입니다.",
            tips = "팔꿈치를 고정하고, 손목을 회전시키며 컬을 합니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=sAq_ocpRh_I"
        ),
        Exercise(
            id = "ex039",
            name = "케이블 트라이셉스 푸시다운",
            equipmentId = "eq005",
            category = "팔",
            description = "삼두근에 지속적인 긴장을 주는 운동입니다.",
            tips = "팔꿈치를 옆구리에 고정하고, 바를 아래로 밉니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=-xa-6cQaZKY"
        ),
        Exercise(
            id = "ex040",
            name = "컨센트레이션 컬",
            equipmentId = "eq002",
            category = "팔",
            description = "이두근에 집중하여 고립시키는 운동입니다.",
            tips = "벤치에 앉아 팔꿈치를 허벅지 안쪽에 고정합니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=Jvj6wjYFHlQ"
        ),
        Exercise(
            id = "ex041",
            name = "딥스",
            equipmentId = "eq010",
            category = "팔",
            description = "체중을 이용한 삼두근 운동입니다.",
            tips = "몸을 앞으로 약간 기울이고, 팔꿈치를 90도까지 구부립니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=2z8JmcrW-As"
        ),
        Exercise(
            id = "ex042",
            name = "프리처 컬",
            equipmentId = "eq001",
            category = "팔",
            description = "이두근 하부를 집중적으로 발달시키는 운동입니다.",
            tips = "프리처 벤치에 팔을 대고, 순수하게 이두근만 사용합니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=vnfZEZcinbQ"
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
        Exercise(
            id = "ex043",
            name = "루마니안 데드리프트",
            equipmentId = "eq001",
            category = "하체",
            description = "햄스트링과 둔근을 집중적으로 발달시키는 운동입니다.",
            tips = "무릎을 살짝만 구부리고, 바벨을 정강이를 따라 내립니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=2SHsk9AzdjA"
        ),
        Exercise(
            id = "ex044",
            name = "레그 익스텐션",
            equipmentId = "eq006",
            category = "하체",
            description = "대퇴사두근을 고립하여 발달시키는 운동입니다.",
            tips = "무릎을 완전히 펴고, 대퇴사두근에 집중합니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=YyvSfVjQeL0"
        ),
        Exercise(
            id = "ex045",
            name = "불가리안 스플릿 스쿼트",
            equipmentId = "eq002",
            category = "하체",
            description = "한쪽 다리에 집중하여 균형을 발달시키는 운동입니다.",
            tips = "뒷발을 벤치에 올리고, 앞 다리로 스쿼트를 합니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=2C-uNgKwPLE"
        ),
        Exercise(
            id = "ex046",
            name = "스미스머신 스쿼트",
            equipmentId = "eq004",
            category = "하체",
            description = "안정된 궤도로 안전하게 하체를 발달시키는 운동입니다.",
            tips = "발을 앞으로 내밀고, 수직으로 앉았다 일어납니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=PYKbdRTLBUg"
        ),
        Exercise(
            id = "ex047",
            name = "프론트 스쿼트",
            equipmentId = "eq001",
            category = "하체",
            description = "대퇴사두근을 더 집중적으로 자극하는 스쿼트입니다.",
            tips = "바벨을 앞쪽 어깨에 올리고, 상체를 세운 채로 스쿼트합니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=uYumuL_G_V0"
        ),
        Exercise(
            id = "ex048",
            name = "카프 레이즈",
            equipmentId = "eq004",
            category = "하체",
            description = "종아리 근육을 발달시키는 운동입니다.",
            tips = "발끝으로 서서 최대한 높이 올라갔다 내립니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=gwLzBJYoWlI"
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
        Exercise(
            id = "ex049",
            name = "러시안 트위스트",
            equipmentId = "eq002",
            category = "코어",
            description = "복사근을 발달시키는 회전 운동입니다.",
            tips = "상체를 뒤로 기울이고, 몸통을 좌우로 회전합니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=wkD8rjkodUI"
        ),
        Exercise(
            id = "ex050",
            name = "행잉 레그 레이즈",
            equipmentId = "eq010",
            category = "코어",
            description = "하복부와 코어 전체를 강력하게 자극하는 운동입니다.",
            tips = "바에 매달려 다리를 수평까지 올립니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=hdng3Nm1x_E"
        ),
        Exercise(
            id = "ex051",
            name = "사이드 플랭크",
            equipmentId = "eq010",
            category = "코어",
            description = "복사근을 집중적으로 강화하는 운동입니다.",
            tips = "몸을 옆으로 일직선으로 유지하고 버팁니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=K2VljzCC16g"
        ),
        Exercise(
            id = "ex052",
            name = "바이시클 크런치",
            equipmentId = "eq010",
            category = "코어",
            description = "복부 전체를 자극하는 동적 운동입니다.",
            tips = "반대쪽 팔꿈치와 무릎을 교차로 당기며 회전합니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=9FGilxCbdz8"
        ),
        Exercise(
            id = "ex053",
            name = "케이블 크런치",
            equipmentId = "eq005",
            category = "코어",
            description = "복부에 지속적인 긴장을 주는 운동입니다.",
            tips = "무릎을 꿇고 케이블을 잡아 복부를 수축합니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=Hb6ZpZ2jITk"
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
        ),
        Exercise(
            id = "ex054",
            name = "인터벌 러닝",
            equipmentId = "eq008",
            category = "유산소",
            description = "고강도와 저강도를 반복하는 효과적인 유산소 운동입니다.",
            tips = "30초 전력질주, 1분 걷기를 반복합니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=4eP6RPv9pO0"
        ),
        Exercise(
            id = "ex055",
            name = "버피",
            equipmentId = "eq010",
            category = "유산소",
            description = "전신을 사용하는 고강도 유산소 운동입니다.",
            tips = "스쿼트-푸시업-점프를 연속으로 수행합니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=TU8QYVW0gDU"
        ),
        Exercise(
            id = "ex056",
            name = "마운틴 클라이머",
            equipmentId = "eq010",
            category = "유산소",
            description = "코어와 심폐지구력을 동시에 향상시킵니다.",
            tips = "플랭크 자세에서 무릎을 가슴 쪽으로 빠르게 당깁니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=nmwgirgXLYM"
        ),
        Exercise(
            id = "ex057",
            name = "줄넘기",
            equipmentId = "eq010",
            category = "유산소",
            description = "효율적인 칼로리 소모와 발목 강화 운동입니다.",
            tips = "발 앞쪽으로 가볍게 뛰며, 일정한 리듬을 유지합니다.",
            youtubeUrl = "https://www.youtube.com/watch?v=FJmRQ5iTXKE"
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
