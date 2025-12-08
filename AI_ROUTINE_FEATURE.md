# AI 루틴 생성 기능 (GPT API 통합)

## 개요

OpenAI GPT-4o-mini를 활용하여 사용자의 목표, 경력, 선호도에 맞는 최적의 운동 루틴을 자동 생성하는 기능입니다.

## 주요 기능

### 1. 사용자 입력 기반 맞춤 루틴
- **운동 목표**: 근력 증가, 체중 감량, 체력 향상, 근비대, 지구력
- **운동 경력**: 초보자, 중급자, 고급자
- **주당 운동 횟수**: 3-6회 (슬라이더)
- **운동 시간**: 30-120분 (슬라이더)
- **선호 부위**: 가슴, 등, 어깨, 팔, 하체, 코어 (다중 선택)
- **추가 정보**: 부상, 제한사항 등 (선택사항)

### 2. AI 생성 결과
- **루틴 이름**: 목표에 맞는 명확한 이름
- **설명**: 루틴 목적 및 특징
- **카테고리**: 상체/하체/전신/코어
- **운동 목록**:
  - 23개 운동 데이터베이스에서 자동 선택
  - 각 운동의 세트/횟수/휴식시간 자동 설정
  - 운동 순서 최적화 (큰 근육 → 작은 근육)
  - 각 운동에 대한 설명/주의사항

### 3. 결과 확인 및 저장
- 생성된 루틴 미리보기 다이얼로그
- 운동 목록 및 세부 설정 확인
- 한 번의 클릭으로 저장
- 로그인 여부에 따라 Firebase 또는 로컬 저장

## API 키 보안

### local.properties 사용
```properties
# local.properties (Git에서 제외됨)
OPENAI_API_KEY=sk-proj-xxxxxxxxxxxxx
```

### 설정 방법
1. `local.properties.template` 파일을 `local.properties`로 복사
2. OpenAI API 키 입력: https://platform.openai.com/api-keys
3. Git에 절대 커밋되지 않음 (`.gitignore`에 포함)

### build.gradle.kts 통합
```kotlin
val localProperties = java.util.Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { localProperties.load(it) }
}

defaultConfig {
    val openaiApiKey = localProperties.getProperty("OPENAI_API_KEY") ?: ""
    buildConfigField("String", "OPENAI_API_KEY", "\"$openaiApiKey\"")
}
```

## 기술 구조

### 데이터 모델

**OpenAI API Request/Response**:
```kotlin
data class OpenAIRequest(
    val model: String = "gpt-4o-mini",
    val messages: List<Message>,
    val temperature: Double = 0.7,
    val maxTokens: Int = 2000
)

data class OpenAIResponse(
    val choices: List<Choice>,
    val usage: Usage
)
```

**AI Routine Request/Response**:
```kotlin
data class AIRoutineRequest(
    val goal: String,
    val experienceLevel: String,
    val workoutsPerWeek: Int,
    val workoutDuration: Int,
    val preferredCategories: List<String>,
    val additionalInfo: String
)

data class AIRoutineResponse(
    val routineName: String,
    val description: String,
    val category: String,
    val exercises: List<AIExerciseRecommendation>
)
```

### API Integration

**OpenAIApiService (Retrofit)**:
```kotlin
interface OpenAIApiService {
    @POST("v1/chat/completions")
    suspend fun createChatCompletion(
        @Header("Authorization") authorization: String,
        @Body request: OpenAIRequest
    ): OpenAIResponse
}
```

**OpenAIDataSource**:
- GPT 프롬프트 엔지니어링
- 23개 운동 목록 제공
- 경력별 가이드라인 (세트/횟수/휴식)
- JSON 응답 파싱

### Repository 레이어

```kotlin
interface AIRoutineRepository {
    suspend fun generateRoutine(request: AIRoutineRequest): AIRoutineResponse
}

class AIRoutineRepositoryImpl(
    private val openAIDataSource: OpenAIDataSource
) : AIRoutineRepository
```

### ViewModel

**AIRoutineViewModel**:
- 사용자 입력 상태 관리
- GPT API 호출
- 생성된 루틴 저장
- 로딩/에러 상태 처리

### UI 화면

**AIRoutineScreen**:
- 입력 폼 (목표, 경력, 횟수, 시간, 부위)
- 생성 버튼 (로딩 표시)
- 결과 다이얼로그
  - 루틴 정보
  - 운동 목록 (카드)
  - 저장/취소 버튼

**RoutineListScreen 통합**:
- 목록 상단에 AI 루틴 생성 카드
- 빈 목록일 때 AI 버튼 강조

## GPT 프롬프트 구조

### System Prompt
```
당신은 전문 퍼스널 트레이너입니다.
사용자의 목표와 조건에 맞는 최적의 운동 루틴을 생성해주세요.

[JSON 형식 정의]
[23개 운동 목록]
[경력별 가이드라인]
```

### User Prompt
```
다음 조건에 맞는 운동 루틴을 생성해주세요:
- 목표: {goal}
- 경력: {experienceLevel}
- 주당 운동 횟수: {workoutsPerWeek}회
- 운동 시간: {workoutDuration}분
- 선호 부위: {preferredCategories}
- 추가 정보: {additionalInfo}
```

### 응답 예시
```json
{
  "routineName": "초보자 상체 근력 루틴",
  "description": "상체 기초 근력 향상을 위한 3일 루틴",
  "category": "상체",
  "exercises": [
    {
      "exerciseId": "ex001",
      "exerciseName": "벤치프레스",
      "sets": 3,
      "reps": 10,
      "weight": 0,
      "restTime": 90,
      "notes": "가슴과 삼두에 집중하며 천천히 내리세요"
    }
  ]
}
```

## 파일 구조

```
app/src/main/java/com/example/gymroutine/
├── data/
│   ├── model/
│   │   ├── openai/
│   │   │   └── OpenAIRequest.kt (NEW)
│   │   └── AIRoutineRequest.kt (NEW)
│   ├── remote/
│   │   ├── OpenAIApiService.kt (NEW)
│   │   └── OpenAIDataSource.kt (NEW)
│   └── repository/
│       └── AIRoutineRepositoryImpl.kt (NEW)
├── domain/
│   └── repository/
│       └── AIRoutineRepository.kt (NEW)
├── presentation/
│   └── routine/
│       ├── AIRoutineViewModel.kt (NEW)
│       ├── AIRoutineScreen.kt (NEW)
│       └── RoutineListScreen.kt (UPDATED)
├── di/
│   ├── NetworkModule.kt (UPDATED - OpenAI Retrofit)
│   └── RepositoryModule.kt (UPDATED - AI Repository)
└── navigation/
    ├── Screen.kt (UPDATED - AIRoutine route)
    └── NavGraph.kt (UPDATED - AI screen)

루트/
├── .gitignore (UPDATED - API 키 제외)
├── local.properties.template (NEW - 템플릿)
└── app/
    └── build.gradle.kts (UPDATED - API 키 로드)
```

## 사용 흐름

### 시나리오 1: 첫 루틴 생성
1. 앱 실행 → 루틴 목록 (빈 상태)
2. "AI로 루틴 생성" 버튼 클릭
3. 입력 폼 작성:
   - 목표: 근력 증가
   - 경력: 초보자
   - 주 3회, 60분
   - 선호: 가슴, 등
4. "AI 루틴 생성" 버튼 클릭
5. GPT가 루틴 생성 (5-10초)
6. 결과 확인 다이얼로그
7. "루틴 저장" 버튼 클릭
8. 루틴 목록으로 이동

### 시나리오 2: 추가 루틴 생성
1. 루틴 목록 (기존 루틴 있음)
2. 상단 "AI로 맞춤 루틴 생성" 카드 클릭
3. 다른 조건으로 입력 (예: 하체 집중)
4. AI 생성 및 저장

### 시나리오 3: 에러 처리
1. API 키가 없는 경우: 에러 메시지 표시
2. 네트워크 오류: "네트워크 연결을 확인하세요"
3. GPT 응답 파싱 실패: "응답 형식 오류"

## 비용 및 성능

### OpenAI API 비용
- 모델: gpt-4o-mini
- 평균 토큰: 1000-1500 tokens/request
- 예상 비용: ~$0.001-0.002/request
- 한 달 100명 사용 시: ~$10-20

### 성능
- 응답 시간: 평균 5-10초
- 로딩 인디케이터로 UX 개선
- 에러 핸들링으로 안정성 확보

## 보안 고려사항

### ✅ 구현된 보안
1. **API 키 보호**:
   - `local.properties`에 저장 (Git 제외)
   - `BuildConfig`로 안전하게 접근
   - 소스 코드에 하드코딩 절대 금지

2. **`.gitignore` 설정**:
   ```
   /local.properties
   secrets.properties
   ```

3. **템플릿 파일**:
   - `local.properties.template` 제공
   - 사용자가 직접 API 키 입력

### ⚠️ 주의사항
1. **절대 커밋하지 말 것**:
   - `local.properties`
   - API 키가 포함된 파일

2. **GitHub 업로드 전 확인**:
   ```bash
   git status  # local.properties가 없는지 확인
   ```

3. **API 키 노출 시**:
   - 즉시 OpenAI에서 키 삭제
   - 새 키 생성

## 테스트 가이드

### 기능 테스트
1. **입력 검증**:
   - 목표 미선택 시 버튼 비활성화
   - 부위 미선택 시 버튼 비활성화
   - 슬라이더 동작 확인

2. **API 호출**:
   - 정상 응답 확인
   - 로딩 상태 표시
   - 에러 메시지 표시

3. **결과 저장**:
   - 다이얼로그 표시
   - 루틴 목록에 추가
   - 상세 화면 이동

### API 키 테스트
1. API 키 없이 실행 → 에러 확인
2. 잘못된 키 입력 → 인증 오류 확인
3. 올바른 키 입력 → 정상 동작 확인

## 향후 개선 사항

### 우선순위 높음
1. **루틴 재생성**: 마음에 안 들면 다시 생성
2. **운동 조정**: AI 결과를 수동으로 조정
3. **다양한 프롬프트**: 더 세밀한 조건 입력

### 우선순위 중간
4. **히스토리**: 생성된 루틴 기록 저장
5. **프리셋**: 인기 있는 조건 프리셋
6. **공유**: AI 루틴을 다른 사용자와 공유

## 완료 확인

- ✅ OpenAI API 통합
- ✅ API 키 보안 설정
- ✅ 데이터 모델 및 Repository
- ✅ ViewModel 구현
- ✅ UI 화면 구현
- ✅ Navigation 연동
- ✅ 에러 처리
- ✅ 로딩 상태
- ✅ .gitignore 업데이트
- ✅ 문서 작성

AI 루틴 생성 기능 완료! 🎉🤖
