# Phase 5 완료: 루틴 생성 및 관리 구현

## 구현 완료 항목

### 1. 데이터 모델 확장
- ✅ Routine 모델 (userId, description, updatedAt 추가)
- ✅ ExerciseSet 모델 (exerciseName, weight, restTime 추가)

### 2. Repository 레이어
**RoutineRepository 인터페이스**:
```kotlin
interface RoutineRepository {
    suspend fun getUserRoutines(userId: String): List<Routine>
    suspend fun getRoutineById(routineId: String): Routine?
    suspend fun createRoutine(routine: Routine): Routine
    suspend fun updateRoutine(routine: Routine): Routine
    suspend fun deleteRoutine(routineId: String)
}
```

**RoutineRepositoryImpl**:
- FirestoreDataSource 연동
- CRUD 작업 구현
- 사용자별 루틴 관리

### 3. ViewModel 구현
**RoutineListViewModel**:
- 루틴 목록 조회 및 상태 관리
- 루틴 삭제 기능
- 최근 수정일 기준 정렬

### 4. UI 화면
**RoutineListScreen**:
- 루틴 목록 표시 (LazyColumn)
- 루틴 카드 UI (이름, 설명, 카테고리, 운동 개수, 최근 수정일)
- 삭제 확인 다이얼로그
- FloatingActionButton으로 루틴 생성
- 빈 목록 상태 처리

**RoutineCreateScreen** (기본 구현):
- 루틴 이름 입력
- 설명 입력
- 생성 버튼
- (운동 선택 기능은 향후 확장)

**RoutineDetailScreen**:
- 루틴 상세 정보 표시
- 운동 목록 표시
- 각 운동의 세트/횟수/무게 정보

### 5. Navigation 통합
- HomeScreen → RoutineList
- RoutineList → RoutineCreate
- RoutineList → RoutineDetail
- Gson을 사용한 객체 직렬화

## 기술 스택

- **Architecture**: Clean Architecture (Data/Domain/Presentation)
- **Design Pattern**: MVVM + Repository Pattern
- **DI**: Hilt/Dagger
- **UI**: Jetpack Compose
- **State Management**: StateFlow
- **Navigation**: Navigation Compose
- **Backend**: Firebase Firestore

## 주요 기능

### 1. 루틴 목록 조회
- 사용자의 모든 루틴 조회
- 최근 수정일 기준 정렬
- 루틴 정보 카드 표시 (이름, 설명, 운동 개수, 날짜)

### 2. 루틴 삭제
- 삭제 확인 다이얼로그
- Firestore에서 실시간 삭제
- 목록 자동 새로고침

### 3. 루틴 상세 보기
- 루틴 정보 표시
- 포함된 운동 목록
- 각 운동의 세트/횟수 정보

### 4. 루틴 생성 (기본)
- 루틴 이름 입력
- 설명 입력
- (운동 선택은 다음 업데이트)

## 파일 구조

```
app/src/main/java/com/example/gymroutine/
├── data/
│   ├── model/
│   │   └── Routine.kt (UPDATED)
│   ├── remote/
│   │   └── FirestoreDataSource.kt (EXISTING - routine methods)
│   └── repository/
│       └── RoutineRepositoryImpl.kt (NEW)
├── domain/
│   └── repository/
│       └── RoutineRepository.kt (NEW)
├── presentation/
│   ├── routine/
│   │   ├── RoutineListViewModel.kt (NEW)
│   │   ├── RoutineListScreen.kt (NEW)
│   │   ├── RoutineCreateScreen.kt (NEW)
│   │   └── RoutineDetailScreen.kt (NEW)
│   ├── home/
│   │   └── HomeScreen.kt (UPDATED)
│   └── navigation/
│       └── NavGraph.kt (UPDATED)
└── di/
    └── RepositoryModule.kt (UPDATED)
```

## 테스트 가이드

### 테스트 시나리오

**시나리오 1: 루틴 목록 조회**
1. 홈 화면에서 "내 루틴" 버튼 클릭
2. 저장된 루틴 목록 확인
3. 빈 목록일 경우 "첫 루틴 만들기" 버튼 표시 확인

**시나리오 2: 루틴 생성**
1. 루틴 목록 화면에서 + FAB 클릭
2. 루틴 이름 입력
3. 설명 입력 (선택)
4. "루틴 생성" 버튼 클릭
5. 목록으로 돌아가기

**시나리오 3: 루틴 상세 보기**
1. 루틴 목록에서 루틴 선택
2. 루틴 상세 정보 확인
3. 포함된 운동 목록 확인

**시나리오 4: 루틴 삭제**
1. 루틴 목록에서 삭제 아이콘 클릭
2. 삭제 확인 다이얼로그 확인
3. "삭제" 버튼 클릭
4. 목록에서 루틴이 제거되는지 확인

## 다음 단계 (Phase 6)

### 루틴 생성 고도화
1. **운동 선택 기능**
   - ExerciseListScreen에서 운동 선택
   - 선택된 운동을 루틴에 추가
   - 운동 순서 변경 (드래그앤드롭)

2. **세트/횟수/무게 설정**
   - 각 운동별 세트 수 설정
   - 각 운동별 횟수 설정
   - 각 운동별 무게 설정
   - 휴식 시간 설정

3. **루틴 수정 기능**
   - 루틴 정보 수정
   - 운동 추가/삭제
   - 운동 순서 변경

4. **루틴 실행 기능**
   - 루틴 시작
   - 세트별 완료 체크
   - 타이머 기능
   - 운동 기록 저장

5. **통계 및 기록**
   - 운동 히스토리
   - 진행 상황 그래프
   - 개인 기록 (PR)

## 알려진 이슈

1. **운동 선택 미구현**: RoutineCreateScreen에서 운동 선택 기능 필요
2. **루틴 수정 미구현**: 생성 후 수정 불가

## 개선 사항 제안

1. **운동 선택 UI**: ExerciseListScreen과 통합하여 운동 선택 모드 추가
2. **드래그앤드롭**: 운동 순서 변경을 위한 드래그앤드롭 구현
3. **템플릿**: 자주 사용하는 루틴 템플릿 제공
4. **공유**: 루틴을 다른 사용자와 공유
5. **복사**: 기존 루틴을 복사하여 새로운 루틴 생성

## 완료 확인

- ✅ Routine 데이터 모델 확장
- ✅ RoutineRepository 구현
- ✅ RoutineListViewModel 구현
- ✅ RoutineListScreen UI
- ✅ RoutineCreateScreen 기본 구현
- ✅ RoutineDetailScreen UI
- ✅ Navigation 통합
- ✅ Hilt DI 통합
- ✅ Firestore 연동

Phase 5 완료! 🎉

## 현재 앱 구조

```
헬스 루틴 앱
├─ 인증 (Phase 2) ✅
│  ├─ 로그인
│  └─ 회원가입
├─ 헬스장 검색 (Phase 3) ✅
│  ├─ 위치 기반 검색
│  ├─ 키워드 검색
│  └─ 헬스장 등록
├─ 운동 목록 (Phase 4) ✅
│  ├─ 운동 목록 조회
│  ├─ 카테고리/기구 필터
│  └─ 운동 상세 정보
└─ 루틴 관리 (Phase 5) ✅
   ├─ 루틴 목록
   ├─ 루틴 생성 (기본)
   ├─ 루틴 상세
   └─ 루틴 삭제
```

다음 업데이트에서는 루틴 생성 시 운동 선택 및 세부 설정 기능을 추가할 예정입니다.
