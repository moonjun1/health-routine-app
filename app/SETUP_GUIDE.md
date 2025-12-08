# 헬스 루틴 앱 - 개발 시작 가이드

## ✅ 완료된 작업 (Phase 1)

### 1. 프로젝트 구조 생성
```
app/src/main/java/com/example/gymroutine/
├── di/                     ✅ Hilt 의존성 주입 모듈
│   ├── AppModule.kt       (Firebase, Context)
│   └── NetworkModule.kt   (Retrofit, OkHttp)
├── data/
│   └── model/             ✅ 데이터 모델
│       ├── User.kt
│       ├── Gym.kt
│       ├── Exercise.kt
│       ├── Equipment.kt
│       └── Routine.kt
├── util/                   ✅ 유틸리티
│   ├── Resource.kt        (상태 래퍼)
│   ├── Constants.kt       (상수)
│   └── Extensions.kt      (확장 함수)
└── GymRoutineApp.kt       ✅ Hilt Application
```

### 2. Gradle 의존성 설정 완료
- ✅ Hilt (의존성 주입)
- ✅ Firebase (Auth, Firestore)
- ✅ Retrofit & OkHttp (네트워킹)
- ✅ Coroutines (비동기 처리)
- ✅ Navigation Compose (화면 전환)
- ✅ Coil (이미지 로딩)
- ✅ Kakao SDK (지도/검색)

### 3. Android 권한 설정
- ✅ INTERNET
- ✅ ACCESS_NETWORK_STATE
- ✅ ACCESS_FINE_LOCATION
- ✅ ACCESS_COARSE_LOCATION

---

## ⚠️ Firebase 설정 필요

프로젝트를 빌드하기 전에 Firebase 설정이 필요합니다:

### 1. Firebase Console에서 프로젝트 생성
1. [Firebase Console](https://console.firebase.google.com/) 접속
2. "프로젝트 추가" 클릭
3. 프로젝트 이름 입력 (예: "Gym Routine App")
4. Google Analytics 설정 (선택사항)

### 2. Android 앱 등록
1. Firebase 프로젝트 → "Android 앱 추가"
2. Android 패키지 이름: `com.example.gymroutine`
3. 앱 닉네임: `Gym Routine`
4. 디버그 서명 인증서 SHA-1 (선택사항)

### 3. google-services.json 다운로드
1. Firebase Console에서 `google-services.json` 다운로드
2. 파일을 `app/` 폴더에 복사
   ```
   GymRoutine/
   └── app/
       ├── google-services.json  ← 여기에 배치
       ├── build.gradle.kts
       └── src/
   ```

### 4. Firebase Authentication 활성화
1. Firebase Console → Authentication → "시작하기"
2. 로그인 방법 → "이메일/비밀번호" 사용 설정

### 5. Firestore Database 생성
1. Firebase Console → Firestore Database → "데이터베이스 만들기"
2. **테스트 모드**로 시작 (개발 중)
3. 위치 선택: `asia-northeast3 (Seoul)`

---

## 🚀 빌드 및 실행

### 1. Gradle Sync
```bash
# Android Studio에서 자동으로 실행되거나
./gradlew build
```

### 2. 앱 실행
- Android Studio에서 `Run` 버튼 클릭
- 또는 단축키: Shift + F10 (Windows/Linux), Ctrl + R (Mac)

---

## 📋 다음 단계 (Phase 2: 인증 시스템)

### 구현할 기능
1. **Firebase Auth 연동**
   - `data/remote/FirebaseAuthDataSource.kt`
   - `data/remote/FirestoreDataSource.kt`

2. **Repository 구현**
   - `domain/repository/AuthRepository.kt` (인터페이스)
   - `data/repository/AuthRepositoryImpl.kt` (구현)

3. **UseCase 구현**
   - `domain/usecase/auth/LoginUseCase.kt`
   - `domain/usecase/auth/SignupUseCase.kt`
   - `domain/usecase/auth/LogoutUseCase.kt`

4. **UI 구현 (Compose)**
   - `presentation/auth/LoginScreen.kt`
   - `presentation/auth/SignupScreen.kt`
   - `presentation/auth/LoginViewModel.kt`
   - `presentation/auth/SignupViewModel.kt`

5. **Navigation 설정**
   - `presentation/navigation/NavGraph.kt`
   - `presentation/navigation/Screen.kt`

---

## 📚 참고 자료

- [DESIGN.md](./DESIGN.md) - 전체 시스템 설계 문서
- [TASKS.md](./TASKS.md) - 상세 태스크 목록
- [Firebase Android 문서](https://firebase.google.com/docs/android/setup)
- [Jetpack Compose 문서](https://developer.android.com/jetpack/compose)
- [Hilt 문서](https://developer.android.com/training/dependency-injection/hilt-android)

---

## 🔥 자주 발생하는 문제

### 1. `google-services.json` 파일이 없습니다
**해결**: 위의 "Firebase 설정 필요" 섹션 참고

### 2. Gradle Sync 실패
**해결**:
```bash
# Gradle 캐시 정리
./gradlew clean
# 다시 빌드
./gradlew build
```

### 3. Hilt 관련 에러
**해결**: `kapt` 플러그인이 올바르게 적용되었는지 확인
```kotlin
plugins {
    alias(libs.plugins.kotlin.kapt)  // 이 줄 확인
}
```

---

**현재 상태**: Phase 1 완료 ✅
**다음 단계**: Firebase 설정 후 Phase 2 (인증 시스템) 시작
