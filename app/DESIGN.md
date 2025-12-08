# 헬스 루틴 앱 - 시스템 설계 문서 (System Design Document)

> 📅 **생성일**: 2025-12-08
> 🎯 **목표**: Kotlin 기반 Android 헬스장 맞춤 루틴 관리 앱
> 📋 **기반 문서**: [TASKS.md](./TASKS.md)

---

## 📐 1. 시스템 아키텍처 (System Architecture)

### 1.1 아키텍처 패턴

**Clean Architecture + MVVM (Model-View-ViewModel)**

```
┌─────────────────────────────────────────────────┐
│              Presentation Layer                 │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐     │
│  │ Fragment │→│ ViewModel │→│   State  │     │
│  └──────────┘  └──────────┘  └──────────┘     │
└─────────────────────────────────────────────────┘
                     ↓↑
┌─────────────────────────────────────────────────┐
│               Domain Layer                      │
│  ┌──────────┐  ┌──────────┐                    │
│  │ UseCase  │  │  Model   │                    │
│  └──────────┘  └──────────┘                    │
└─────────────────────────────────────────────────┘
                     ↓↑
┌─────────────────────────────────────────────────┐
│                Data Layer                       │
│  ┌──────────────┐  ┌────────────────┐          │
│  │  Repository  │→│   DataSource   │          │
│  └──────────────┘  └────────────────┘          │
│                    ┌────────────────┐          │
│                    │ Firebase/Kakao │          │
│                    └────────────────┘          │
└─────────────────────────────────────────────────┘
```

**설계 원칙**:
- ✅ **단방향 데이터 플로우**: View → ViewModel → UseCase → Repository → DataSource
- ✅ **의존성 역전**: 상위 레이어가 하위 레이어의 인터페이스에 의존
- ✅ **관심사 분리**: UI, 비즈니스 로직, 데이터 접근 계층 명확히 분리
- ✅ **테스트 용이성**: 각 계층 독립적 테스트 가능

---

## 📂 2. 프로젝트 구조 (Project Structure)

### 2.1 디렉토리 구조

```
app/
├── di/                                  # 의존성 주입
│   ├── AppModule.kt                    # 앱 레벨 의존성
│   ├── DatabaseModule.kt               # Firebase/Room 의존성
│   └── NetworkModule.kt                # Retrofit/OkHttp 의존성
│
├── data/                                # 데이터 계층
│   ├── model/                          # 데이터 모델 (DTO)
│   │   ├── User.kt
│   │   ├── Gym.kt
│   │   ├── Exercise.kt
│   │   ├── Equipment.kt
│   │   └── Routine.kt
│   │
│   ├── repository/                     # Repository 구현
│   │   ├── AuthRepositoryImpl.kt
│   │   ├── GymRepositoryImpl.kt
│   │   ├── ExerciseRepositoryImpl.kt
│   │   └── RoutineRepositoryImpl.kt
│   │
│   ├── remote/                         # 원격 데이터 소스
│   │   ├── FirebaseAuthDataSource.kt
│   │   ├── FirestoreDataSource.kt
│   │   └── KakaoApiDataSource.kt
│   │
│   └── local/                          # 로컬 데이터 소스 (선택사항)
│       ├── dao/                        # Room DAO
│       └── AppDatabase.kt
│
├── domain/                              # 도메인 계층
│   ├── repository/                     # Repository 인터페이스
│   │   ├── AuthRepository.kt
│   │   ├── GymRepository.kt
│   │   ├── ExerciseRepository.kt
│   │   └── RoutineRepository.kt
│   │
│   ├── usecase/                        # 비즈니스 로직
│   │   ├── auth/
│   │   │   ├── LoginUseCase.kt
│   │   │   ├── SignupUseCase.kt
│   │   │   └── LogoutUseCase.kt
│   │   ├── gym/
│   │   │   ├── SearchGymUseCase.kt
│   │   │   ├── RegisterGymUseCase.kt
│   │   │   └── GetUserGymUseCase.kt
│   │   ├── exercise/
│   │   │   ├── GetExercisesByEquipmentUseCase.kt
│   │   │   └── GetExerciseDetailUseCase.kt
│   │   └── routine/
│   │       ├── CreateRoutineUseCase.kt
│   │       ├── GetUserRoutinesUseCase.kt
│   │       ├── UpdateRoutineUseCase.kt
│   │       └── DeleteRoutineUseCase.kt
│   │
│   └── model/                          # 도메인 모델 (필요시)
│
├── presentation/                        # UI 계층
│   ├── auth/
│   │   ├── LoginFragment.kt
│   │   ├── LoginViewModel.kt
│   │   ├── SignupFragment.kt
│   │   └── SignupViewModel.kt
│   │
│   ├── gym/
│   │   ├── search/
│   │   │   ├── GymSearchFragment.kt
│   │   │   └── GymSearchViewModel.kt
│   │   └── register/
│   │       ├── GymRegisterFragment.kt
│   │       └── GymRegisterViewModel.kt
│   │
│   ├── exercise/
│   │   ├── list/
│   │   │   ├── ExerciseListFragment.kt
│   │   │   └── ExerciseListViewModel.kt
│   │   └── detail/
│   │       ├── ExerciseDetailFragment.kt
│   │       └── ExerciseDetailViewModel.kt
│   │
│   ├── routine/
│   │   ├── list/
│   │   │   ├── RoutineListFragment.kt
│   │   │   └── RoutineListViewModel.kt
│   │   ├── create/
│   │   │   ├── RoutineCreateFragment.kt
│   │   │   └── RoutineCreateViewModel.kt
│   │   └── detail/
│   │       ├── RoutineDetailFragment.kt
│   │       └── RoutineDetailViewModel.kt
│   │
│   ├── home/
│   │   ├── HomeFragment.kt
│   │   └── HomeViewModel.kt
│   │
│   ├── settings/
│   │   ├── SettingsFragment.kt
│   │   └── SettingsViewModel.kt
│   │
│   └── common/
│       ├── BaseFragment.kt
│       ├── BaseViewModel.kt
│       └── adapter/
│           ├── ExerciseAdapter.kt
│           └── RoutineAdapter.kt
│
├── util/                                # 유틸리티
│   ├── Constants.kt                    # 상수
│   ├── Extensions.kt                   # 확장 함수
│   ├── DateTimeUtils.kt                # 날짜/시간 유틸
│   ├── PermissionUtils.kt              # 권한 처리
│   └── Resource.kt                     # 상태 래퍼 클래스
│
└── MainActivity.kt                      # 메인 액티비티 (NavHost)
```

### 2.2 네이밍 규칙

| 타입 | 규칙 | 예시 |
|------|------|------|
| **Fragment** | `{기능}Fragment` | `LoginFragment` |
| **ViewModel** | `{기능}ViewModel` | `LoginViewModel` |
| **Repository** | `{도메인}Repository` | `AuthRepository` |
| **UseCase** | `{동사}{대상}UseCase` | `CreateRoutineUseCase` |
| **Adapter** | `{대상}Adapter` | `ExerciseAdapter` |
| **Layout XML** | `fragment_{기능}` | `fragment_login.xml` |
| **Item Layout** | `item_{대상}` | `item_exercise.xml` |

---

## 🗄️ 3. 데이터 모델 설계 (Data Models)

### 3.1 Firestore 컬렉션 구조

```
firestore/
├── users/                              # 사용자 정보
│   └── {userId}/
│       ├── email: String
│       ├── gymId: String?              # 연동된 헬스장 ID
│       └── createdAt: Timestamp
│
├── gyms/                               # 헬스장 정보
│   └── {placeId}/                      # 카카오맵 placeId
│       ├── name: String
│       ├── address: String
│       ├── placeId: String
│       ├── registeredBy: String        # 등록한 유저 ID
│       ├── hours: Map<String, Hours>   # {mon: {open, close}, ...}
│       ├── equipments: List<String>    # 보유 기구 ID 배열
│       └── createdAt: Timestamp
│
├── equipments/                         # 기구 정보 (앱 전체 공통)
│   └── {equipmentId}/
│       ├── id: String
│       ├── name: String                # 예: "벤치프레스"
│       ├── category: String            # 예: "프리웨이트", "머신"
│       ├── targetMuscle: String        # 예: "가슴"
│       └── description: String
│
├── exercises/                          # 운동 정보 (앱 전체 공통)
│   └── {exerciseId}/
│       ├── id: String
│       ├── name: String                # 예: "플랫 벤치프레스"
│       ├── equipmentId: String         # 필요한 기구 ID
│       ├── category: String            # 예: "가슴", "등", "어깨"
│       ├── description: String
│       ├── tips: String
│       └── youtubeUrl: String?
│
└── routines/                           # 루틴 정보 (유저별)
    └── {userId}/
        └── {routineId}/
            ├── name: String
            ├── category: String        # 예: "가슴", "등", "전신"
            ├── exercises: List<ExerciseSet>
            │   └── {
            │       exerciseId: String,
            │       sets: Int,
            │       reps: Int,
            │       order: Int
            │     }
            └── createdAt: Timestamp
```

### 3.2 Kotlin 데이터 클래스

```kotlin
// data/model/User.kt
data class User(
    val id: String = "",
    val email: String = "",
    val gymId: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

// data/model/Gym.kt
data class Gym(
    val placeId: String = "",
    val name: String = "",
    val address: String = "",
    val registeredBy: String = "",
    val hours: Map<String, Hours> = emptyMap(),
    val equipments: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)

data class Hours(
    val open: String = "00:00",  // HH:mm
    val close: String = "23:59"
)

// data/model/Equipment.kt
data class Equipment(
    val id: String = "",
    val name: String = "",
    val category: String = "",
    val targetMuscle: String = "",
    val description: String = ""
)

// data/model/Exercise.kt
data class Exercise(
    val id: String = "",
    val name: String = "",
    val equipmentId: String = "",
    val category: String = "",
    val description: String = "",
    val tips: String = "",
    val youtubeUrl: String? = null
)

// data/model/Routine.kt
data class Routine(
    val id: String = "",
    val name: String = "",
    val category: String = "",
    val exercises: List<ExerciseSet> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)

data class ExerciseSet(
    val exerciseId: String = "",
    val sets: Int = 3,
    val reps: Int = 10,
    val order: Int = 0
)
```

---

## 🔄 4. 데이터 플로우 (Data Flow)

### 4.1 인증 플로우

```
┌─────────────┐
│LoginFragment│
└──────┬──────┘
       │ 1. 로그인 버튼 클릭
       ↓
┌──────────────┐
│LoginViewModel│
└──────┬───────┘
       │ 2. loginUseCase.invoke(email, password)
       ↓
┌──────────────┐
│ LoginUseCase │
└──────┬───────┘
       │ 3. authRepository.login(email, password)
       ↓
┌─────────────────────┐
│AuthRepositoryImpl   │
└──────┬──────────────┘
       │ 4. firebaseAuthDataSource.signIn()
       ↓
┌─────────────────────────┐
│FirebaseAuthDataSource   │
└──────┬──────────────────┘
       │ 5. Firebase Auth API
       ↓
┌─────────────────┐
│  Firebase Auth  │
└─────────────────┘

← Result<User> (StateFlow)
← UI 업데이트
```

### 4.2 헬스장 검색 플로우

```
1. 사용자 위치 권한 요청
   └→ GymSearchFragment.requestLocationPermission()

2. Kakao Map SDK 현재 위치 획득
   └→ MapView.setCurrentLocation()

3. 카카오 로컬 API 장소 검색
   └→ KakaoApiDataSource.searchPlaces("헬스장", lat, lng)
   └→ Result<List<KakaoPlace>>

4. 검색 결과 마커 표시
   └→ MapView.addMarkers(places)

5. 마커 클릭 시
   ├→ Firestore 조회: gyms/{placeId}
   │  ├─ 존재 → "이미 등록된 헬스장입니다" → 연동 옵션
   │  └─ 미존재 → "새로 등록하시겠습니까?" → GymRegisterFragment
   │
   └→ 연동 선택 시
      └→ UpdateUserGymUseCase.invoke(userId, gymId)
      └→ Firestore: users/{userId}.gymId = placeId
```

### 4.3 루틴 생성 플로우

```
1. RoutineCreateFragment 진입
   ├→ GetUserGymUseCase() → 사용자의 gymId 조회
   └→ GetGymEquipmentsUseCase(gymId) → 헬스장 보유 기구 목록

2. 운동 추가 버튼 클릭
   └→ ExerciseSelectionDialog 표시
      └→ GetExercisesByEquipmentUseCase(equipmentIds)
      └→ 헬스장 기구로만 가능한 운동 필터링

3. 운동 선택 + 세트/횟수 입력
   └→ ExerciseSet(exerciseId, sets, reps, order) 생성
   └→ ViewModel.addExerciseToRoutine()

4. 저장 버튼 클릭
   └→ CreateRoutineUseCase.invoke(routine)
   └→ Firestore: routines/{userId}/{routineId} 저장
   └→ Result<Routine> → UI 업데이트
```

---

## 🛠️ 5. 기술 스택 (Tech Stack)

### 5.1 핵심 라이브러리

| 카테고리 | 라이브러리 | 버전 | 용도 |
|---------|-----------|------|------|
| **언어** | Kotlin | 1.9+ | 주 개발 언어 |
| **UI** | Jetpack Compose | - | (선택사항) 또는 XML |
| **비동기** | Coroutines | 1.7+ | 비동기 처리 |
| **DI** | Hilt | 2.48+ | 의존성 주입 |
| **네트워크** | Retrofit | 2.9+ | HTTP 클라이언트 |
| | OkHttp | 4.11+ | HTTP 인터셉터 |
| | Gson | 2.10+ | JSON 파싱 |
| **Firebase** | firebase-auth-ktx | 22.3+ | 인증 |
| | firebase-firestore-ktx | 24.10+ | 데이터베이스 |
| **카카오** | kakao-sdk | 2.19+ | 지도/검색 API |
| **이미지** | Coil | 2.5+ | 이미지 로딩 |
| **Navigation** | Navigation Component | 2.7+ | 화면 전환 |
| **ViewModel** | lifecycle-viewmodel-ktx | 2.6+ | MVVM 패턴 |

### 5.2 Gradle 의존성 (build.gradle.kts)

```kotlin
dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.20")

    // AndroidX
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")

    // ViewModel & LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")

    // Retrofit & OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Kakao SDK
    implementation("com.kakao.sdk:v2-all:2.19.0")

    // Image Loading
    implementation("io.coil-kt:coil:2.5.0")

    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
```

---

## 🔐 6. 보안 설계 (Security Design)

### 6.1 Firebase Security Rules

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {

    // 인증된 유저만 접근 가능
    function isAuthenticated() {
      return request.auth != null;
    }

    // 본인 데이터인지 확인
    function isOwner(userId) {
      return request.auth.uid == userId;
    }

    // users 컬렉션: 본인만 읽기/쓰기
    match /users/{userId} {
      allow read, write: if isOwner(userId);
    }

    // gyms 컬렉션: 로그인 유저 읽기, 등록자만 수정
    match /gyms/{gymId} {
      allow read: if isAuthenticated();
      allow create: if isAuthenticated();
      allow update, delete: if isAuthenticated() &&
                                request.auth.uid == resource.data.registeredBy;
    }

    // exercises/equipments: 모든 로그인 유저 읽기
    match /exercises/{exerciseId} {
      allow read: if isAuthenticated();
      allow write: if false;  // 관리자만 수정 (Firebase Console)
    }

    match /equipments/{equipmentId} {
      allow read: if isAuthenticated();
      allow write: if false;
    }

    // routines: 본인만 읽기/쓰기
    match /routines/{userId}/{routineId} {
      allow read, write: if isOwner(userId);
    }
  }
}
```

### 6.2 Android 권한 (AndroidManifest.xml)

```xml
<!-- 필수 권한 -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

<!-- 위치 권한 (런타임 요청 필요) -->
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

<application>
    <!-- 카카오 API 키 -->
    <meta-data
        android:name="com.kakao.sdk.AppKey"
        android:value="@string/kakao_api_key" />
</application>
```

### 6.3 API 키 보호

**local.properties** (Git 제외):
```properties
kakao.api.key=YOUR_KAKAO_API_KEY
```

**build.gradle.kts**:
```kotlin
val properties = Properties().apply {
    load(FileInputStream(rootProject.file("local.properties")))
}

android {
    defaultConfig {
        buildConfigField("String", "KAKAO_API_KEY",
            "\"${properties["kakao.api.key"]}\"")
    }
}
```

---

## 🎨 7. UI/UX 설계 (UI/UX Design)

### 7.1 Navigation Graph

```xml
<!-- res/navigation/nav_graph.xml -->
<navigation>
    <!-- 인증 플로우 -->
    <navigation id="@+id/auth_nav_graph" startDestination="@id/loginFragment">
        <fragment id="@+id/loginFragment" name="...LoginFragment">
            <action id="@+id/action_to_signup" destination="@id/signupFragment"/>
            <action id="@+id/action_to_home" destination="@id/main_nav_graph"/>
        </fragment>
        <fragment id="@+id/signupFragment" name="...SignupFragment"/>
    </navigation>

    <!-- 메인 플로우 -->
    <navigation id="@+id/main_nav_graph" startDestination="@id/homeFragment">
        <fragment id="@+id/homeFragment" name="...HomeFragment">
            <action id="@+id/action_to_gym_search" destination="@id/gymSearchFragment"/>
            <action id="@+id/action_to_routine_list" destination="@id/routineListFragment"/>
        </fragment>

        <fragment id="@+id/gymSearchFragment" name="...GymSearchFragment">
            <action id="@+id/action_to_gym_register" destination="@id/gymRegisterFragment"/>
        </fragment>

        <fragment id="@+id/exerciseListFragment" name="...ExerciseListFragment">
            <action id="@+id/action_to_exercise_detail" destination="@id/exerciseDetailFragment"/>
        </fragment>

        <fragment id="@+id/routineListFragment" name="...RoutineListFragment">
            <action id="@+id/action_to_routine_create" destination="@id/routineCreateFragment"/>
            <action id="@+id/action_to_routine_detail" destination="@id/routineDetailFragment"/>
        </fragment>
    </navigation>
</navigation>
```

### 7.2 Bottom Navigation

```xml
<!-- res/menu/bottom_nav_menu.xml -->
<menu>
    <item
        android:id="@+id/homeFragment"
        android:icon="@drawable/ic_home"
        android:title="@string/home" />

    <item
        android:id="@+id/exerciseListFragment"
        android:icon="@drawable/ic_exercise"
        android:title="@string/exercise" />

    <item
        android:id="@+id/routineListFragment"
        android:icon="@drawable/ic_routine"
        android:title="@string/routine" />

    <item
        android:id="@+id/settingsFragment"
        android:icon="@drawable/ic_settings"
        android:title="@string/settings" />
</menu>
```

### 7.3 Material Design 3 컴포넌트

| 화면 | 주요 컴포넌트 |
|------|-------------|
| **Login/Signup** | TextInputLayout, MaterialButton, ProgressBar |
| **Gym Search** | MapView, SearchBar, BottomSheet, RecyclerView |
| **Gym Register** | TimePicker, Chip (기구 선택), FloatingActionButton |
| **Exercise List** | RecyclerView, TabLayout, CardView |
| **Routine Create** | RecyclerView (drag & drop), NumberPicker, Chip |
| **Home** | MaterialCardView, TextView, Button |

---

## 🚀 8. 구현 로드맵 (Implementation Roadmap)

### Phase 1: 프로젝트 기반 구축 (1-2일)

**목표**: 개발 환경 세팅 및 기본 구조 생성

- [x] **1.1 프로젝트 생성**
  - Android Studio Arctic Fox 이상
  - Min SDK 24, Target SDK 34
  - Kotlin DSL (build.gradle.kts)

- [ ] **1.2 의존성 설정**
  - Firebase, Kakao SDK, Hilt, Navigation 추가
  - google-services.json 설정

- [ ] **1.3 패키지 구조 생성**
  - di, data, domain, presentation, util 패키지 생성
  - Base 클래스 (BaseFragment, BaseViewModel) 작성

- [ ] **1.4 Navigation 설정**
  - nav_graph.xml 생성
  - MainActivity에 NavHostFragment 설정

- [ ] **1.5 Hilt 설정**
  - @HiltAndroidApp Application 클래스
  - AppModule, DatabaseModule 생성

---

### Phase 2: 인증 시스템 (1-2일)

**목표**: Firebase Auth 기반 로그인/회원가입 구현

- [ ] **2.1 UI 구현**
  - LoginFragment, SignupFragment 레이아웃
  - ViewBinding 설정

- [ ] **2.2 Firebase Auth 연동**
  - FirebaseAuthDataSource 구현
  - AuthRepository 구현
  - LoginUseCase, SignupUseCase 작성

- [ ] **2.3 ViewModel 구현**
  - LoginViewModel, SignupViewModel
  - StateFlow로 UI 상태 관리

- [ ] **2.4 자동 로그인**
  - SplashFragment에서 currentUser 확인
  - 로그인 상태에 따라 Navigation 분기

- [ ] **2.5 Firestore users 컬렉션 연동**
  - 회원가입 시 user 문서 생성
  - FirestoreDataSource 구현

---

### Phase 3: 데이터 준비 및 Repository 구현 (2일)

**목표**: 기구/운동 데이터 준비 및 Repository 계층 완성

- [ ] **3.1 초기 데이터 작성**
  - equipments.json 작성 (20+ 기구)
  - exercises.json 작성 (50+ 운동)

- [ ] **3.2 Firestore 데이터 업로드**
  - Firebase Console 또는 스크립트로 업로드
  - equipments, exercises 컬렉션 생성

- [ ] **3.3 Repository 구현**
  - ExerciseRepository, EquipmentRepository
  - GymRepository, RoutineRepository

- [ ] **3.4 UseCase 구현**
  - GetExercisesByEquipmentUseCase
  - GetEquipmentsByCategoryUseCase
  - CreateRoutineUseCase, GetUserRoutinesUseCase

---

### Phase 4: 헬스장 검색 및 등록 (3일)

**목표**: 카카오맵 연동 및 헬스장 기능 구현

- [ ] **4.1 카카오맵 SDK 설정**
  - build.gradle에 의존성 추가
  - API 키 설정 (local.properties)
  - AndroidManifest 권한 설정

- [ ] **4.2 GymSearchFragment UI**
  - MapView 레이아웃
  - SearchBar, BottomSheet

- [ ] **4.3 위치 권한 처리**
  - ActivityResultContracts 사용
  - 권한 요청 및 현재 위치 획득

- [ ] **4.4 장소 검색 API 연동**
  - Kakao Local API (Retrofit)
  - KakaoApiDataSource 구현
  - SearchGymUseCase 작성

- [ ] **4.5 헬스장 등록 화면**
  - GymRegisterFragment UI
  - TimePicker (운영시간)
  - Chip (기구 선택)
  - RegisterGymUseCase 구현

- [ ] **4.6 헬스장 연동**
  - UpdateUserGymUseCase
  - users/{userId}.gymId 업데이트

---

### Phase 5: 운동/루틴 관리 (3-4일)

**목표**: 핵심 기능인 운동 목록 및 루틴 관리 구현

- [ ] **5.1 ExerciseListFragment**
  - RecyclerView + ExerciseAdapter
  - 카테고리 필터 (TabLayout)
  - 내 헬스장 기구 기반 필터링

- [ ] **5.2 ExerciseDetailFragment**
  - 운동 정보 표시
  - 유튜브 영상 (WebView)
  - "루틴에 추가" 버튼

- [ ] **5.3 RoutineListFragment**
  - RecyclerView + RoutineAdapter
  - 루틴 아이템 (이름, 카테고리, 운동 개수)
  - 수정/삭제 옵션 (Swipe)

- [ ] **5.4 RoutineCreateFragment**
  - 루틴 이름 입력
  - 운동 추가 다이얼로그
  - RecyclerView (드래그 앤 드롭)
  - ItemTouchHelper 구현

- [ ] **5.5 RoutineDetailFragment**
  - 루틴 상세 정보
  - 운동 리스트 (세트/횟수)
  - 수정/삭제 버튼

- [ ] **5.6 Firestore 연동**
  - CreateRoutineUseCase
  - UpdateRoutineUseCase
  - DeleteRoutineUseCase

---

### Phase 6: 홈 화면 및 완성도 (2일)

**목표**: 홈 대시보드 및 전체 플로우 완성

- [ ] **6.1 HomeFragment 구현**
  - 오늘 헬스장 운영 정보
  - 내 헬스장 이름
  - 최근 루틴 바로가기
  - 빠른 메뉴 버튼

- [ ] **6.2 SettingsFragment**
  - 헬스장 변경
  - 로그아웃
  - 앱 정보

- [ ] **6.3 Bottom Navigation 연결**
  - MainActivity에 BottomNavigationView 추가
  - Navigation 연결 테스트

- [ ] **6.4 전체 테스트**
  - 플로우 테스트 (로그인 → 헬스장 등록 → 루틴 생성)
  - 에러 케이스 처리
  - UI/UX 개선

- [ ] **6.5 배포 준비**
  - 앱 아이콘 설정
  - ProGuard 설정
  - Release 빌드 생성
  - Firebase App Distribution 설정

---

## 📊 9. 상태 관리 (State Management)

### 9.1 StateFlow 기반 UI 상태

```kotlin
// util/Resource.kt
sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}

// presentation/auth/LoginViewModel.kt
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _loginState = MutableStateFlow<Resource<User>>(Resource.Loading)
    val loginState: StateFlow<Resource<User>> = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = Resource.Loading
            try {
                val user = loginUseCase(email, password)
                _loginState.value = Resource.Success(user)
            } catch (e: Exception) {
                _loginState.value = Resource.Error(e.message ?: "로그인 실패")
            }
        }
    }
}

// presentation/auth/LoginFragment.kt
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.loginState.collect { state ->
                when (state) {
                    is Resource.Loading -> showLoading()
                    is Resource.Success -> navigateToHome()
                    is Resource.Error -> showError(state.message)
                }
            }
        }
    }
}
```

---

## 🧪 10. 테스트 전략 (Testing Strategy)

### 10.1 Unit Test

```kotlin
// domain/usecase/LoginUseCaseTest.kt
class LoginUseCaseTest {

    @Mock
    private lateinit var authRepository: AuthRepository

    private lateinit var loginUseCase: LoginUseCase

    @Test
    fun `로그인 성공 시 User 반환`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        val expectedUser = User(id = "123", email = email)

        coEvery { authRepository.login(email, password) } returns expectedUser

        // When
        val result = loginUseCase(email, password)

        // Then
        assertEquals(expectedUser, result)
    }
}
```

### 10.2 Integration Test

```kotlin
// data/repository/AuthRepositoryImplTest.kt
@ExperimentalCoroutinesApi
class AuthRepositoryImplTest {

    private lateinit var repository: AuthRepositoryImpl
    private lateinit var firebaseAuth: FirebaseAuth

    @Test
    fun `Firebase Auth 연동 테스트`() = runTest {
        // 실제 Firebase Auth 연동 테스트
    }
}
```

---

## 📝 11. 다음 단계 (Next Steps)

### 즉시 실행 가능한 액션

1. **Android Studio 프로젝트 생성**
   ```bash
   패키지명: com.yourname.gymroutine
   Min SDK: API 24 (Android 7.0)
   Language: Kotlin
   Build configuration: Kotlin DSL
   ```

2. **Firebase 프로젝트 생성**
   - [Firebase Console](https://console.firebase.google.com/)
   - Android 앱 등록
   - google-services.json 다운로드

3. **카카오 개발자 등록**
   - [Kakao Developers](https://developers.kakao.com/)
   - 앱 생성 및 API 키 발급

4. **초기 데이터 준비**
   - `app/src/main/assets/equipments.json` 작성
   - `app/src/main/assets/exercises.json` 작성

5. **Phase 1 시작**
   - 패키지 구조 생성
   - Gradle 의존성 추가
   - Base 클래스 작성

---

## 📚 12. 참고 자료 (References)

- [Android Architecture Guide](https://developer.android.com/topic/architecture)
- [Firebase Android Documentation](https://firebase.google.com/docs/android/setup)
- [Kakao Map SDK](https://apis.map.kakao.com/android/)
- [Hilt Dependency Injection](https://developer.android.com/training/dependency-injection/hilt-android)
- [Kotlin Coroutines Guide](https://kotlinlang.org/docs/coroutines-guide.html)

---

**🎯 설계 요약**:
- ✅ Clean Architecture + MVVM 패턴
- ✅ Hilt 기반 의존성 주입
- ✅ Firestore 데이터베이스 설계 완료
- ✅ StateFlow 상태 관리
- ✅ 6개 Phase 로드맵 수립
- ✅ 보안 규칙 정의

**다음 단계**: Phase 1 프로젝트 세팅 시작 🚀
