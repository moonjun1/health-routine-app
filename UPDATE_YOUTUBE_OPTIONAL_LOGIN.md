# 업데이트: YouTube 영상 + 로그인 선택 기능

## 변경 사항 요약

### 1. ✅ YouTube 영상 추가
모든 운동(23개)에 YouTube 추천 영상 링크 추가

### 2. ✅ 로그인 선택 사항
로그인 없이도 앱 사용 가능 (로컬 저장)

---

## 1. YouTube 영상 기능

### 구현 내용
- **23개 모든 운동에 YouTube URL 추가**
  - 가슴 운동 4개
  - 등 운동 4개
  - 어깨 운동 3개
  - 팔 운동 3개
  - 하체 운동 4개
  - 코어 운동 3개
  - 유산소 운동 2개

### 사용 방법
1. 운동 목록에서 운동 선택
2. 운동 상세 화면에서 **"YouTube 영상 보기"** 버튼 클릭
3. YouTube 앱 또는 브라우저에서 영상 재생

### 추가된 영상 예시
- **벤치프레스**: 올바른 자세와 호흡법
- **데드리프트**: 안전한 허리 사용법
- **스쿼트**: 무릎 보호 자세
- **플랭크**: 코어 강화 방법

### 수정된 파일
- `ExerciseData.kt` - 모든 운동에 youtubeUrl 추가
- `ExerciseDetailScreen.kt` - YouTube 버튼 및 Intent 연동

---

## 2. 로그인 선택 기능

### 개요
로그인하지 않아도 앱의 모든 기능 사용 가능:
- ✅ 운동 목록 조회 (로컬 데이터)
- ✅ 헬스장 검색 (Kakao API)
- ✅ 루틴 생성/관리 (로컬 저장)
- ✅ YouTube 영상 시청

### 저장 방식 차이

| 기능 | 로그인 안 함 | 로그인 함 |
|------|-------------|----------|
| 운동 목록 | 로컬 데이터 | 로컬 데이터 |
| 헬스장 검색 | Kakao API | Kakao API |
| 루틴 저장 | **기기에만 저장** | **Firebase 클라우드** |
| 데이터 동기화 | ❌ 불가 | ✅ 가능 |
| 기기 변경 시 | ❌ 데이터 손실 | ✅ 데이터 유지 |

### 구현 방식

#### A. 로컬 저장소 (SharedPreferences)
```kotlin
class LocalRoutineDataSource {
    // SharedPreferences에 JSON으로 저장
    - getAllRoutines(): 모든 루틴 조회
    - saveRoutine(): 루틴 저장/수정
    - deleteRoutine(): 루틴 삭제
    - getRoutineById(): 특정 루틴 조회
}
```

#### B. 하이브리드 Repository
```kotlin
class RoutineRepositoryImpl {
    // userId가 비어있으면 로컬, 아니면 Firebase

    getUserRoutines(userId):
        if userId.isEmpty() → Local
        else → Firebase

    createRoutine(routine):
        if routine.userId.isEmpty() → Local
        else → Firebase
}
```

#### C. 로그인 화면 업데이트
```kotlin
LoginScreen {
    - 로그인 버튼
    - 회원가입 버튼
    - "로그인 없이 계속하기" 버튼 (NEW!)
    - 안내 문구: "로그인하지 않으면 루틴은 기기에만 저장됩니다"
}
```

### 수정된 파일
1. **LocalRoutineDataSource.kt** (NEW)
   - SharedPreferences 기반 로컬 저장소

2. **RoutineRepositoryImpl.kt** (UPDATED)
   - 로그인 여부에 따라 Local/Firebase 자동 선택

3. **LoginScreen.kt** (UPDATED)
   - "로그인 없이 계속하기" 버튼 추가
   - 안내 문구 추가

4. **RoutineListViewModel.kt** (UPDATED)
   - userId가 null일 경우 빈 문자열로 처리

5. **NavGraph.kt** (UPDATED)
   - skip login 시 홈 화면으로 이동

### 사용 흐름

#### 시나리오 1: 로그인 없이 사용
```
1. 앱 실행
2. "로그인 없이 계속하기" 클릭
3. 홈 화면 → 모든 기능 사용
4. 루틴 생성 → 기기에 저장 (SharedPreferences)
5. 앱 종료 후 재실행 → 루틴 유지 ✅
6. 앱 삭제 → 루틴 삭제 ❌
```

#### 시나리오 2: 로그인 후 사용
```
1. 앱 실행
2. 로그인
3. 홈 화면 → 모든 기능 사용
4. 루틴 생성 → Firebase에 저장
5. 다른 기기에서 로그인 → 루틴 동기화 ✅
6. 앱 삭제 후 재설치 → 루틴 유지 ✅
```

#### 시나리오 3: 나중에 로그인
```
현재 구현: 로컬 데이터는 Firebase로 자동 동기화 안 됨
향후 구현: "로그인하여 데이터 동기화" 기능 추가 예정
```

---

## 테스트 가이드

### YouTube 기능 테스트
1. 운동 목록 → 아무 운동 선택
2. "YouTube 영상 보기" 버튼 확인
3. 버튼 클릭 → YouTube 앱/브라우저 실행 확인
4. 영상 재생 확인

### 로그인 선택 테스트
1. 앱 실행 → 로그인 화면
2. "로그인 없이 계속하기" 버튼 확인
3. 버튼 클릭 → 홈 화면 이동
4. 루틴 생성 → 저장 확인
5. 앱 종료 후 재실행 → 루틴 유지 확인

---

## 알려진 제한사항

### 로컬 저장소 한계
1. **데이터 동기화 불가**: 기기 간 공유 안 됨
2. **데이터 백업 없음**: 앱 삭제 시 데이터 손실
3. **용량 제한**: SharedPreferences는 대용량 데이터 부적합
4. **마이그레이션 없음**: 로컬 → Firebase 자동 이전 미구현

### 해결 방법
- **중요한 루틴**: 로그인하여 Firebase에 저장 권장
- **임시 사용**: 로컬 저장으로 충분
- **향후 개선**: 로컬 → Firebase 동기화 기능 추가 예정

---

## 다음 업데이트 계획

### 우선순위 높음
1. **로컬 → Firebase 동기화**
   - "데이터 클라우드에 백업" 기능
   - 로그인 시 로컬 데이터 업로드 옵션

2. **루틴 생성 고도화**
   - 운동 선택 UI
   - 세트/횟수/무게 설정
   - 운동 순서 변경 (드래그앤드롭)

### 우선순위 중간
3. **루틴 실행 기능**
   - 타이머
   - 세트 완료 체크
   - 운동 기록 저장

4. **통계 및 기록**
   - 운동 히스토리
   - 진행 상황 그래프
   - 개인 기록 (PR)

---

## 빌드 및 실행

```bash
# Android Studio에서 실행
1. Rebuild Project
2. Run 'app'

# 또는 Gradle로 빌드
./gradlew assembleDebug
```

---

## 완료 체크리스트

### YouTube 기능
- ✅ 23개 운동에 YouTube URL 추가
- ✅ ExerciseDetailScreen에 YouTube 버튼 추가
- ✅ Intent로 YouTube 앱/브라우저 연동
- ✅ 모든 운동 영상 테스트 완료

### 로그인 선택 기능
- ✅ LocalRoutineDataSource 구현 (SharedPreferences)
- ✅ RoutineRepositoryImpl 하이브리드 로직
- ✅ LoginScreen에 skip 버튼 추가
- ✅ Navigation 업데이트
- ✅ ViewModel 로그인 선택 처리
- ✅ 로컬 저장 테스트 완료

---

## 요약

### 추가된 기능
1. **YouTube 영상**: 23개 운동 → 각각 추천 영상
2. **로그인 선택**: 로그인 안 해도 모든 기능 사용 가능
3. **로컬 저장**: 루틴을 기기에 저장 (SharedPreferences)
4. **하이브리드 저장**: 로그인 여부 자동 감지

### 사용자 혜택
- ✅ 회원가입 부담 없이 바로 사용
- ✅ 운동 영상으로 올바른 자세 학습
- ✅ 기기에만 저장 → 개인정보 걱정 없음
- ✅ 나중에 로그인하면 클라우드 백업 가능

업데이트 완료! 🎉
