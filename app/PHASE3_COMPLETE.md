# Phase 3 완료: 헬스장 검색 및 등록

## ✅ 완료된 작업

### 1. Kakao Local API 통합

#### DTOs
- ✅ **KakaoLocalResponse.kt**
  - `KakaoLocalSearchResponse`: 검색 결과 응답
  - `KakaoPlace`: 장소 정보
  - `KakaoMeta`: 메타데이터
  - `KakaoRegionInfo`: 지역 정보

- ✅ **KakaoLocalApi.kt**
  - `searchPlaces()`: 키워드로 장소 검색
  - `searchByCategory()`: 카테고리로 장소 검색

- ✅ **KakaoLocalDataSource.kt**
  - `searchNearbyGyms()`: 현재 위치 기반 헬스장 검색
  - `searchGymsByKeyword()`: 키워드로 헬스장 검색
  - `toGym()`: KakaoPlace → Gym 변환

### 2. 위치 권한 처리

- ✅ **LocationHelper.kt**
  - `hasLocationPermission()`: 위치 권한 확인
  - `isLocationEnabled()`: 위치 서비스 활성화 확인
  - `getCurrentLocation()`: 현재 위치 가져오기
  - `getLastKnownLocation()`: 마지막 위치 가져오기 (빠름)

### 3. Domain Layer 확장

#### Repository Interface
- ✅ **GymRepository.kt**
  - `searchNearbyGyms()`: 주변 헬스장 검색
  - `searchGymsByKeyword()`: 키워드 검색
  - `getGymById()`: ID로 헬스장 조회
  - `registerGym()`: 헬스장 등록
  - `updateGym()`: 헬스장 정보 수정
  - `deleteGym()`: 헬스장 삭제
  - `getAllGyms()`: 모든 헬스장 조회

#### Repository Implementation
- ✅ **GymRepositoryImpl.kt**
  - Kakao Local API + Firestore 통합
  - 검색은 Kakao API 사용
  - 등록/수정/삭제는 Firestore 사용

#### Use Cases
- ✅ **SearchNearbyGymsUseCase.kt**
  - 좌표 유효성 검증
  - 반경 범위 검증 (0-20000m)
  - 주변 헬스장 검색 수행

- ✅ **SearchGymsByKeywordUseCase.kt**
  - 키워드 유효성 검증 (2자 이상)
  - 선택적 위치 기반 정렬
  - 키워드 검색 수행

- ✅ **RegisterGymUseCase.kt**
  - 헬스장 정보 유효성 검증
  - Firestore에 헬스장 등록
  - 사용자의 gymId 업데이트

### 4. Presentation Layer 확장

#### GymSearch 화면
- ✅ **GymSearchViewModel.kt**
  - StateFlow 기반 상태 관리
  - 위치 권한 확인
  - 현재 위치 가져오기
  - 주변 검색 / 키워드 검색

- ✅ **GymSearchScreen.kt**
  - 위치 권한 요청
  - 검색어 입력 UI
  - 주변 헬스장 찾기 버튼
  - 검색 결과 리스트
  - 로딩/에러 상태 표시

#### GymRegister 화면
- ✅ **GymRegisterViewModel.kt**
  - 헬스장 등록 상태 관리
  - RegisterGymUseCase 통합

- ✅ **GymRegisterScreen.kt**
  - 선택한 헬스장 정보 표시
  - 등록 확인 UI
  - 안내 메시지
  - 등록/취소 버튼

### 5. Navigation 업데이트

- ✅ **NavGraph.kt** 확장
  - GymSearch 라우트 추가
  - GymRegister 라우트 추가 (Gym 객체 전달)
  - HomeScreen에서 GymSearch로 이동
  - GymSearch에서 GymRegister로 이동
  - 등록 완료 후 Home으로 이동

### 6. Data Model 확장

- ✅ **Gym.kt** 필드 추가
  - `latitude: Double`: 위도
  - `longitude: Double`: 경도
  - `phone: String`: 전화번호
  - `toMap()` / `fromMap()` 메서드 업데이트

### 7. Dependency Injection 확장

- ✅ **NetworkModule.kt** 업데이트
  - `provideKakaoLocalApi()`: Kakao API 제공

- ✅ **RepositoryModule.kt** 업데이트
  - `provideGymRepository()`: GymRepository 제공

### 8. 필수 Dependencies 추가

- ✅ **Google Play Services Location**
  - `play-services-location:21.0.1`
  - FusedLocationProviderClient 사용

- ✅ **Kakao SDK 설정**
  - AndroidManifest.xml에 App Key 추가
  - build.gradle.kts에 REST API KEY 추가

---

## 🏗️ 아키텍처 구조

```
┌─────────────────────────────────────────────────┐
│              Presentation Layer                 │
│                                                 │
│  GymSearchScreen → GymSearchViewModel           │
│  GymRegisterScreen → GymRegisterViewModel       │
│                                                 │
└────────────────────┬────────────────────────────┘
                     ↓
┌─────────────────────────────────────────────────┐
│               Domain Layer                      │
│                                                 │
│  SearchNearbyGymsUseCase                       │
│  SearchGymsByKeywordUseCase                    │
│  RegisterGymUseCase                            │
│  GymRepository (interface)                     │
│                                                 │
└────────────────────┬────────────────────────────┘
                     ↓
┌─────────────────────────────────────────────────┐
│                Data Layer                       │
│                                                 │
│  GymRepositoryImpl                             │
│  ├─ KakaoLocalDataSource (검색)               │
│  │  └─ KakaoLocalApi                          │
│  └─ FirestoreDataSource (등록/관리)           │
│                                                 │
└────────────────────┬────────────────────────────┘
                     ↓
┌─────────────────────────────────────────────────┐
│     Kakao API / Firebase Firestore              │
└─────────────────────────────────────────────────┘
```

---

## 🎯 데이터 플로우

### 헬스장 검색 플로우 (주변 검색)
```
1. User clicks "내 주변 헬스장 찾기"
2. GymSearchScreen → Check location permission
3. If permission granted → Get current location
4. GymSearchViewModel → SearchNearbyGymsUseCase
5. SearchNearbyGymsUseCase → GymRepository.searchNearbyGyms()
6. GymRepositoryImpl → KakaoLocalDataSource.searchNearbyGyms()
7. KakaoLocalApi → Kakao Local API
8. Response: List<KakaoPlace> → Convert to List<Gym>
9. Display results in GymSearchScreen
```

### 헬스장 검색 플로우 (키워드 검색)
```
1. User enters keyword and clicks search
2. GymSearchScreen → GymSearchViewModel.searchByKeyword()
3. GymSearchViewModel → SearchGymsByKeywordUseCase
4. SearchGymsByKeywordUseCase → GymRepository.searchGymsByKeyword()
5. GymRepositoryImpl → KakaoLocalDataSource.searchGymsByKeyword()
6. KakaoLocalApi → Kakao Local API
7. Response: List<KakaoPlace> → Convert to List<Gym>
8. Display results in GymSearchScreen
```

### 헬스장 등록 플로우
```
1. User selects gym from search results
2. Navigate to GymRegisterScreen with selected Gym
3. User clicks "이 헬스장으로 등록하기"
4. GymRegisterViewModel → RegisterGymUseCase
5. RegisterGymUseCase → GymRepository.registerGym()
6. GymRepositoryImpl → FirestoreDataSource.createGym()
7. Firestore: Create gym document in 'gyms' collection
8. Navigate to HomeScreen
```

---

## 🧪 테스트 방법

### 1. 앱 빌드 및 실행
```bash
./gradlew assembleDebug
# 또는 Android Studio에서 Run 버튼
```

### 2. 헬스장 검색 테스트 (주변 검색)

**전제 조건:**
- 카카오 개발자 콘솔에서 REST API 키 발급
- `build.gradle.kts`에 REST API 키 설정:
  ```kotlin
  buildConfigField("String", "KAKAO_REST_API_KEY", "\"YOUR_KAKAO_REST_API_KEY\"")
  ```
- `AndroidManifest.xml`에 Native 앱 키 설정:
  ```xml
  <meta-data
      android:name="com.kakao.sdk.AppKey"
      android:value="YOUR_KAKAO_NATIVE_APP_KEY" />
  ```

**테스트 단계:**
1. 앱 실행 → 로그인
2. Home 화면에서 "헬스장 검색" 버튼 클릭
3. 위치 권한 요청 → 허용
4. "내 주변 헬스장 찾기" 버튼 클릭
5. ✅ 성공 시 → 주변 헬스장 리스트 표시
6. ❌ 실패 시 → 에러 메시지 표시

### 3. 헬스장 검색 테스트 (키워드 검색)
1. 검색어 입력 (예: "헬스장", "피트니스")
2. 검색 아이콘 클릭 또는 엔터
3. ✅ 성공 시 → 검색 결과 리스트 표시
4. ❌ 실패 시 → 에러 메시지 표시

### 4. 헬스장 등록 테스트
1. 검색 결과에서 헬스장 선택
2. 등록 화면에서 헬스장 정보 확인
3. "이 헬스장으로 등록하기" 버튼 클릭
4. ✅ 성공 시 → Home 화면으로 이동
5. Firebase Console → Firestore → gyms 컬렉션에서 확인

### 5. Firebase Console 확인
1. [Firebase Console](https://console.firebase.google.com/) 접속
2. **Firestore Database** → `gyms` 컬렉션
   - 등록한 헬스장 문서 확인
   - 필드: placeId, name, address, latitude, longitude, phone, hours, equipments

---

## 🔧 구현된 기능

### ✅ 완료
- [x] Kakao Local API 연동
- [x] 위치 권한 처리 (Compose permission launcher)
- [x] Google Play Services Location 통합
- [x] 주변 헬스장 검색 (반경 5km)
- [x] 키워드 기반 헬스장 검색
- [x] 헬스장 정보 표시 (이름, 주소, 전화번호)
- [x] 헬스장 등록 (Firestore)
- [x] 입력 유효성 검증
- [x] 에러 처리 및 사용자 피드백
- [x] MVVM + Clean Architecture
- [x] Hilt 의존성 주입
- [x] Navigation Compose with object passing

### ⏳ 다음 단계 (Phase 4)
- [ ] 운동/기구 리스트 표시
- [ ] 기구별 운동 필터링
- [ ] 카테고리별 운동 분류
- [ ] 운동 상세 정보

---

## 📱 화면 플로우

```
LoginScreen
    ↓
HomeScreen
    ├─ "헬스장 검색" → GymSearchScreen
    │                    ├─ 주변 검색
    │                    ├─ 키워드 검색
    │                    └─ 헬스장 선택 → GymRegisterScreen
    │                                        ├─ 등록 성공 → HomeScreen
    │                                        └─ 취소 → GymSearchScreen
    └─ "내 루틴" → (Phase 5에서 구현)
```

---

## 🐛 알려진 이슈

### 해결된 이슈
- ✅ Kakao SDK 의존성 해결 (Kakao Maven 저장소 추가)
- ✅ Resource.Idle 상태 추가 (초기 로딩 상태 문제 해결)
- ✅ Gym 모델에 latitude, longitude, phone 필드 추가
- ✅ Google Play Services Location 의존성 추가

### 현재 이슈
- ⚠️ 에뮬레이터에서 Google Play Services Provider Installer 경고
  - **영향**: 기능 동작에는 문제 없음
  - **해결**: Google Play가 포함된 에뮬레이터 이미지 사용 권장

---

## 📚 코드 위치

```
app/src/main/java/com/example/gymroutine/
├── data/
│   ├── model/
│   │   └── Gym.kt                         ← latitude, longitude, phone 추가
│   ├── remote/
│   │   ├── dto/
│   │   │   └── KakaoLocalResponse.kt      ← Kakao API DTOs
│   │   ├── KakaoLocalApi.kt               ← Kakao API Interface
│   │   ├── KakaoLocalDataSource.kt        ← Kakao API DataSource
│   │   └── FirestoreDataSource.kt         ← deleteGym, getGyms 추가
│   └── repository/
│       └── GymRepositoryImpl.kt           ← Kakao + Firestore 통합
│
├── domain/
│   ├── repository/
│   │   └── GymRepository.kt               ← Gym 관련 repository
│   └── usecase/
│       └── gym/
│           ├── SearchNearbyGymsUseCase.kt
│           ├── SearchGymsByKeywordUseCase.kt
│           └── RegisterGymUseCase.kt
│
├── presentation/
│   ├── gym/
│   │   ├── GymSearchScreen.kt             ← 검색 UI
│   │   ├── GymSearchViewModel.kt          ← 검색 ViewModel
│   │   ├── GymRegisterScreen.kt           ← 등록 UI
│   │   └── GymRegisterViewModel.kt        ← 등록 ViewModel
│   └── navigation/
│       └── NavGraph.kt                    ← Gym 화면 라우트 추가
│
├── util/
│   └── LocationHelper.kt                  ← 위치 권한 및 위치 가져오기
│
└── di/
    ├── NetworkModule.kt                   ← KakaoLocalApi 제공
    └── RepositoryModule.kt                ← GymRepository 제공
```

---

## 🔑 필수 설정

### 1. Kakao Developers Console
1. [Kakao Developers](https://developers.kakao.com/) 접속
2. 애플리케이션 생성
3. **REST API 키** 발급
4. **Native 앱 키** 발급
5. 플랫폼 설정 → Android 추가
   - 패키지명: `com.example.gymroutine`
   - 키 해시 등록

### 2. build.gradle.kts 설정
```kotlin
defaultConfig {
    // ...
    buildConfigField("String", "KAKAO_REST_API_KEY", "\"YOUR_REST_API_KEY\"")
}
```

### 3. AndroidManifest.xml 설정
```xml
<meta-data
    android:name="com.kakao.sdk.AppKey"
    android:value="YOUR_NATIVE_APP_KEY" />
```

---

## 🎉 Phase 3 완료!

**현재 상태**: 헬스장 검색 및 등록 완료 ✅
**다음 단계**: Phase 4 - 운동/기구 리스트 구현

사용자는 이제 카카오 API를 통해 주변 헬스장을 검색하고, Firestore에 등록할 수 있습니다!
