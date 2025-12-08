# Phase 6 완료: 루틴 생성 고도화

## 구현 완료 항목

### 1. RoutineCreateViewModel 구현
**주요 기능**:
- 루틴 기본 정보 관리 (이름, 설명, 카테고리)
- 운동 선택 및 관리
- 운동 설정 (세트, 횟수, 무게, 휴식시간)
- 운동 순서 변경 (위/아래 이동)
- 루틴 생성 및 저장 (로컬/Firebase)

**주요 메서드**:
```kotlin
- updateRoutineName(name: String)
- updateDescription(desc: String)
- addExercise(exercise: Exercise)
- removeExercise(exerciseId: String)
- updateExerciseSettings(exerciseId, sets, reps, weight, restTime)
- moveExerciseUp(exerciseId: String)
- moveExerciseDown(exerciseId: String)
- createRoutine()
- canCreateRoutine(): Boolean
```

### 2. ExerciseSelectionScreen 구현
**주요 기능**:
- 전체 운동 목록 표시
- 카테고리별 필터링 (전체, 가슴, 등, 어깨, 팔, 하체, 코어, 유산소)
- 검색 기능 (운동 이름, 설명)
- 이미 선택된 운동 표시 (체크 아이콘)
- 운동 선택/해제 토글

**UI 컴포넌트**:
- SearchBar: 운동 검색
- FilterChip: 카테고리 필터
- ExerciseSelectionItem: 운동 카드 (선택 상태 표시)

### 3. ExerciseSettingsDialog 구현
**세부 설정 항목**:
- **세트 수**: 정수 입력
- **반복 횟수**: 정수 입력
- **무게**: 소수점 입력 (선택사항, kg)
- **휴식 시간**: 정수 입력 (초)
  - 빠른 설정 버튼: 30초, 60초, 90초, 120초

**입력 검증**:
- 숫자 키보드로 입력 제한
- 잘못된 입력 시 기존 값 유지

### 4. RoutineCreateScreen 고도화
**새로운 기능**:
- 운동 목록 표시 및 관리
- 운동 순서 변경 (위/아래 화살표)
- 운동 설정 다이얼로그 열기
- 운동 삭제
- 실시간 운동 개수 표시
- 빈 목록 안내 메시지

**SelectedExerciseItem 컴포넌트**:
```
[순서]. 운동 이름
세트 × 횟수 × 무게kg | 휴식 시간초
[↑] [↓] [설정] [삭제]
```

### 5. Navigation 업데이트
**새로운 라우트**:
- `exercise_selection`: 운동 선택 화면

**ViewModel 공유**:
- RoutineCreateScreen과 ExerciseSelectionScreen이 동일한 ViewModel 공유
- `hiltViewModel(parentEntry)` 패턴 사용
- 화면 간 데이터 실시간 동기화

**Navigation 흐름**:
```
RoutineList → RoutineCreate → ExerciseSelection → RoutineCreate
                ↓ (선택 완료)
           루틴 생성 → RoutineList
```

## 기술 스택

- **Architecture**: Clean Architecture + MVVM
- **UI**: Jetpack Compose + Material3
- **State Management**: Kotlin StateFlow
- **Navigation**: Navigation Compose (ViewModel 공유)
- **DI**: Hilt/Dagger
- **Backend**: Firebase Firestore + Local SharedPreferences

## 주요 기능

### 1. 운동 선택
1. 루틴 생성 화면에서 "운동 추가" 버튼 클릭
2. 운동 선택 화면으로 이동
3. 카테고리 필터 또는 검색으로 운동 찾기
4. 운동 카드 클릭하여 선택
5. 자동으로 루틴에 추가됨
6. 뒤로가기로 루틴 생성 화면 복귀

### 2. 운동 설정
1. 선택된 운동의 "설정" 아이콘 클릭
2. 다이얼로그에서 세트/횟수/무게/휴식시간 입력
3. "저장" 버튼으로 적용
4. 운동 카드에 설정 내용 반영

### 3. 운동 순서 변경
1. 선택된 운동의 "↑" 또는 "↓" 버튼 클릭
2. 즉시 순서 변경됨
3. 첫 번째 운동은 "↑" 비활성화
4. 마지막 운동은 "↓" 비활성화

### 4. 운동 삭제
1. 선택된 운동의 "삭제" 아이콘 클릭
2. 즉시 목록에서 제거
3. 순서 자동 재정렬

### 5. 루틴 생성
1. 루틴 이름 입력 (필수)
2. 설명 입력 (선택사항)
3. 운동 1개 이상 추가 (필수)
4. "루틴 생성" 버튼 클릭
5. 로그인 상태에 따라 Firebase 또는 로컬 저장
6. 루틴 목록으로 이동

## 파일 구조

```
app/src/main/java/com/example/gymroutine/
├── presentation/
│   ├── routine/
│   │   ├── RoutineCreateViewModel.kt (NEW)
│   │   ├── RoutineCreateScreen.kt (UPDATED)
│   │   ├── ExerciseSelectionScreen.kt (NEW)
│   │   ├── ExerciseSettingsDialog.kt (NEW)
│   │   ├── RoutineListViewModel.kt (EXISTING)
│   │   ├── RoutineListScreen.kt (EXISTING)
│   │   └── RoutineDetailScreen.kt (EXISTING)
│   └── navigation/
│       ├── NavGraph.kt (UPDATED)
│       └── Screen.kt (UPDATED)
```

## 테스트 가이드

### 시나리오 1: 루틴 생성 (전체 플로우)
1. 루틴 목록에서 "+" FAB 클릭
2. 루틴 이름 입력: "상체 루틴"
3. 설명 입력: "가슴과 어깨 집중"
4. "운동 추가" 버튼 클릭
5. 카테고리 "가슴" 선택
6. "벤치프레스" 선택 → 자동 추가됨
7. 뒤로가기
8. 벤치프레스 "설정" 클릭
9. 세트: 4, 횟수: 10, 무게: 60, 휴식: 90초 입력
10. "저장" 클릭
11. "운동 추가" 버튼 다시 클릭
12. 카테고리 "어깨" 선택
13. "숄더프레스" 선택
14. 뒤로가기
15. "루틴 생성" 버튼 클릭
16. 루틴 목록에 "상체 루틴" 표시 확인

### 시나리오 2: 운동 순서 변경
1. 루틴 생성 화면에서 3개 운동 추가
2. 2번째 운동의 "↓" 버튼 클릭
3. 3번째로 이동 확인
4. 새로운 3번째 운동의 "↑" 버튼 클릭
5. 다시 2번째로 이동 확인

### 시나리오 3: 운동 삭제
1. 루틴 생성 화면에서 운동 3개 추가
2. 2번째 운동의 "삭제" 아이콘 클릭
3. 즉시 삭제 및 순서 재정렬 확인
4. 남은 운동이 1, 2번으로 표시되는지 확인

### 시나리오 4: 입력 검증
1. 루틴 이름 없이 "루틴 생성" 버튼 → 비활성화 확인
2. 루틴 이름 입력
3. 운동 추가 없이 "루틴 생성" 버튼 → 비활성화 확인
4. 운동 1개 추가
5. "루틴 생성" 버튼 활성화 확인

### 시나리오 5: 검색 및 필터
1. 운동 선택 화면 진입
2. 검색창에 "프레스" 입력
3. 벤치프레스, 숄더프레스 등만 표시 확인
4. 검색 지우기
5. "하체" 카테고리 선택
6. 스쿼트, 데드리프트 등 하체 운동만 표시 확인

## 개선 사항

### Phase 5 대비 개선점
1. ✅ 운동 선택 기능 추가 (Phase 5에서 미구현)
2. ✅ 세트/횟수/무게 설정 기능
3. ✅ 운동 순서 변경 기능
4. ✅ 실시간 운동 개수 표시
5. ✅ 입력 검증 강화

### 사용자 경험 개선
1. ✅ 빠른 휴식시간 설정 버튼
2. ✅ 이미 선택된 운동 시각적 표시
3. ✅ 카테고리 필터로 빠른 검색
4. ✅ 운동 카드에 설정 내용 표시
5. ✅ 버튼 활성화/비활성화로 액션 가능 여부 명확화

## 알려진 제한사항

1. **드래그앤드롭 미구현**: 화살표 버튼으로 순서 변경 (Phase 5 계획에서 언급된 기능)
2. **루틴 수정 미구현**: 생성 후 수정 불가 (다음 Phase에서 구현 예정)
3. **운동 복사 기능 없음**: 같은 운동을 여러 번 추가 불가

## 다음 단계 (Phase 7)

### 루틴 수정 기능
1. **RoutineEditScreen 생성**
   - RoutineCreateScreen과 유사한 UI
   - 기존 루틴 데이터 로드
   - 수정 사항 저장

2. **루틴 실행 기능**
   - RoutineExecutionScreen 생성
   - 세트별 완료 체크
   - 타이머 기능
   - 운동 기록 저장

3. **통계 및 기록**
   - 운동 히스토리
   - 진행 상황 그래프
   - 개인 기록 (PR)

## 완료 확인

- ✅ RoutineCreateViewModel 구현
- ✅ ExerciseSelectionScreen 구현
- ✅ ExerciseSettingsDialog 구현
- ✅ RoutineCreateScreen 고도화
- ✅ 운동 순서 변경 기능
- ✅ Navigation 업데이트
- ✅ ViewModel 공유 패턴 적용
- ✅ 입력 검증 구현

Phase 6 완료! 🎉

## 현재 앱 구조

```
헬스 루틴 앱
├─ 인증 (Phase 2) ✅
│  ├─ 로그인
│  ├─ 회원가입
│  └─ 로그인 선택 사항 (로컬 저장)
├─ 헬스장 검색 (Phase 4) ✅
│  ├─ 위치 기반 검색
│  ├─ 키워드 검색
│  └─ 헬스장 등록
├─ 운동 목록 (Phase 3) ✅
│  ├─ 운동 목록 조회
│  ├─ 카테고리/기구 필터
│  ├─ 운동 상세 정보
│  └─ YouTube 영상 연동
└─ 루틴 관리 (Phase 5 + Phase 6) ✅
   ├─ 루틴 목록
   ├─ 루틴 생성 (고도화) ⭐ NEW
   │  ├─ 운동 선택
   │  ├─ 세트/횟수/무게 설정
   │  └─ 운동 순서 변경
   ├─ 루틴 상세
   └─ 루틴 삭제
```

다음 업데이트에서는 루틴 수정 기능과 루틴 실행 기능을 추가할 예정입니다.
