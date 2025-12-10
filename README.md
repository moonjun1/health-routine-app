# 🏋️ GymRoutine

AI 기반 맞춤형 운동 루틴 생성 앱

<img width="409" height="856" alt="image" src="https://github.com/user-attachments/assets/ffc28a51-0d06-451e-902f-d93fcfa43d42" />
<img width="412" height="871" alt="image" src="https://github.com/user-attachments/assets/109b8586-8eff-4cc8-8df6-51aee3b3c2da" />
<img width="420" height="863" alt="image" src="https://github.com/user-attachments/assets/9cf2d8f0-c093-443c-a72d-f786718bf067" />
<img width="418" height="875" alt="image" src="https://github.com/user-attachments/assets/5193f3df-60fd-4ce4-8100-7fadf355c825" />
<img width="416" height="866" alt="image" src="https://github.com/user-attachments/assets/39366b4c-b221-44e5-87af-e076e57ca925" />

## 📱 주요 기능

### 🏢 헬스장 관리
- **Google Maps 기반 헬스장 검색**: 위치 기반으로 주변 헬스장 찾기
- **여러 헬스장 등록**: 집/회사 근처 등 여러 헬스장 등록 가능
- **보유 기구 관리**: 각 헬스장의 보유 기구 목록 등록
- **로그인 없이도 정보 확인**: 로그인하지 않아도 헬스장 정보 조회 가능

### 🤖 AI 루틴 생성
- **헬스장 기구 기반 생성**: 선택한 헬스장의 보유 기구만 사용
- **맞춤형 루틴**: 목표/경력/운동 횟수/시간에 따라 최적화
- **운동 부위 선택**: 가슴, 등, 어깨, 팔, 하체, 코어 중 선택
- **상세 운동 정보**: 세트/횟수/휴식시간/운동 설명 제공

### 📊 루틴 관리
- **무제한 루틴 저장**: 목적별로 여러 루틴 생성 및 저장
- **루틴 수정/삭제**: 언제든지 루틴 편집 가능
- **운동 기록**: 운동 진행 상황 체크 및 기록

## 🚀 빠른 시작 가이드

### 1. 헬스장 등록하기 (필수!)
```
홈 화면 → "헬스장 검색" → 헬스장 선택 → 보유 기구 선택 → 등록
```

### 2. AI 루틴 생성하기
```
"AI 루틴" 버튼 → 헬스장 선택 → 조건 입력 → 생성
```

### 3. 운동 시작하기
```
"루틴" 탭 → 루틴 선택 → 운동 시작
```

**📖 자세한 사용법은 [USER_GUIDE.md](./USER_GUIDE.md)를 참고하세요!**

## 🛠️ 기술 스택

### Android
- **Language**: Kotlin
- **UI**: Jetpack Compose
- **Architecture**: MVVM + Clean Architecture
- **DI**: Hilt
- **Async**: Kotlin Coroutines + Flow

### Backend & API
- **Database**: Firebase Firestore
- **Authentication**: Firebase Auth
- **Maps**: Google Maps API
- **Location Search**: Kakao Local API
- **AI**: GPT API (루틴 생성)

### Libraries
- Retrofit2 (네트워크)
- Coil (이미지 로딩)
- Gson (JSON 파싱)
- Google Maps SDK
- Material3 (UI Components)

## 📁 프로젝트 구조

```
app/src/main/java/com/example/gymroutine/
├── data/
│   ├── model/          # 데이터 모델
│   ├── remote/         # API 데이터 소스
│   └── repository/     # Repository 구현
├── domain/
│   └── repository/     # Repository 인터페이스
├── presentation/
│   ├── auth/           # 인증 화면
│   ├── exercise/       # 운동 목록
│   ├── gym/            # 헬스장 검색/등록
│   ├── home/           # 홈 화면
│   ├── navigation/     # 네비게이션
│   ├── profile/        # 프로필
│   └── routine/        # 루틴 관리/AI 생성
├── di/                 # Dependency Injection
└── util/               # 유틸리티
```

## ⚙️ 설정 방법

### 1. API Keys 설정

`local.properties` 파일에 다음 내용 추가:

```properties
KAKAO_API_KEY=your_kakao_api_key
GOOGLE_MAPS_API_KEY=your_google_maps_api_key
GPT_API_KEY=your_openai_api_key
```

### 2. Firebase 설정

1. [Firebase Console](https://console.firebase.google.com/)에서 프로젝트 생성
2. Android 앱 추가
3. `google-services.json` 다운로드 후 `app/` 디렉토리에 추가
4. Firestore Database 활성화
5. Authentication 활성화 (Email/Password)

### 3. Google Maps 설정

1. [Google Cloud Console](https://console.cloud.google.com/)에서 프로젝트 생성
2. Maps SDK for Android 활성화
3. API 키 생성 후 `local.properties`에 추가

## 🔑 주요 기능 상세

### AI 루틴 생성 알고리즘
1. **헬스장 기구 필터링**: 선택한 헬스장의 보유 기구만 사용
2. **목표별 최적화**: 근력/체중감량/체력향상/근비대/지구력
3. **경험 수준 고려**: 초보자/중급자/고급자별 강도 조절
4. **운동 부위 배분**: 선호 부위에 더 많은 운동 할당
5. **과학적 프로그램**: 세트/횟수/휴식시간 과학적 기반 설정

### 헬스장 검색
- **Kakao Local API**: 정확한 헬스장 위치 정보
- **Google Maps**: 지도에서 시각적으로 위치 확인
- **거리 계산**: 현재 위치로부터 거리 표시
- **영업시간 표시**: 현재 영업 여부 실시간 표시

## 📊 데이터베이스 구조

### Firestore Collections

#### users
```
{
  id: string,
  email: string,
  myGymId: string,      // 주로 사용하는 헬스장
  createdAt: timestamp
}
```

#### gyms
```
{
  placeId: string,
  name: string,
  address: string,
  latitude: number,
  longitude: number,
  equipments: string[], // 보유 기구 목록
  registeredBy: string, // 등록한 사용자 ID
  createdAt: timestamp
}
```

#### routines/{userId}/user_routines
```
{
  id: string,
  name: string,
  description: string,
  category: string,
  exercises: ExerciseSet[],
  createdAt: timestamp,
  updatedAt: timestamp
}
```

## 🐛 알려진 이슈

- [ ] 오프라인 모드 미지원
- [ ] 운동 기록 통계 기능 미구현
- [ ] 루틴 공유 기능 미구현

## 🔜 향후 계획

- [ ] 운동 기록 통계 및 그래프
- [ ] 루틴 공유 기능
- [ ] 운동 영상 추가
- [ ] 운동 타이머 기능
- [ ] 프로그램 자동 진행 (Progressive Overload)

## 👨‍💻 개발자

**moonjun1** - [GitHub](https://github.com/moonjun1)

## 📄 라이선스

This project is licensed under the MIT License.

## 🙏 감사의 말

- Firebase for backend infrastructure
- Google Maps for location services
- OpenAI for AI capabilities
- Kakao for local search API
