# 헬스 루틴 앱 - 개발 태스크

## 프로젝트 개요
- **앱 이름**: 헬스 루틴 앱 (가칭)
- **목적**: 내 헬스장 기구 기반 맞춤 운동 루틴 생성 및 관리
- **마감**: 2024년 12월 21일
- **기술 스택**: Kotlin, Android Studio, Firebase (Auth, Firestore), 카카오맵 SDK

---

## Phase 1: 프로젝트 세팅 (12/7~8)

### 1.1 Android 프로젝트 생성
- [ ] Android Studio에서 새 프로젝트 생성
- [ ] 패키지명 설정
- [ ] 최소 SDK 버전 설정 (API 24 권장)
- [ ] Kotlin DSL 또는 Groovy 선택

### 1.2 Firebase 연동
- [ ] Firebase 콘솔에서 새 프로젝트 생성
- [ ] Android 앱 등록 (패키지명 입력)
- [ ] google-services.json 다운로드 및 app 폴더에 추가
- [ ] build.gradle에 Firebase 의존성 추가
  - [ ] Firebase Auth
  - [ ] Cloud Firestore
- [ ] Firebase 연결 테스트

### 1.3 카카오맵 SDK 연동
- [ ] 카카오 개발자 등록
- [ ] 앱 등록 및 API 키 발급
- [ ] 카카오맵 SDK 의존성 추가
- [ ] AndroidManifest.xml에 API 키 등록
- [ ] 지도 표시 테스트

### 1.4 프로젝트 구조 설정
- [ ] 패키지 구조 생성
  ```
  app/
  ├── ui/
  │   ├── auth/
  │   ├── home/
  │   ├── gym/
  │   ├── exercise/
  │   └── routine/
  ├── data/
  │   ├── model/
  │   └── repository/
  └── util/
  ```
- [ ] Navigation Component 설정
- [ ] Base Activity/Fragment 생성

---

## Phase 2: 로그인/회원가입 (12/9~10)

### 2.1 UI 구현
- [ ] 로그인 화면 레이아웃
  - [ ] 이메일 입력 필드
  - [ ] 비밀번호 입력 필드
  - [ ] 로그인 버튼
  - [ ] 회원가입 이동 버튼
- [ ] 회원가입 화면 레이아웃
  - [ ] 이메일 입력 필드
  - [ ] 비밀번호 입력 필드
  - [ ] 비밀번호 확인 필드
  - [ ] 가입 버튼

### 2.2 Firebase Auth 연동
- [ ] 회원가입 기능 구현
  - [ ] 이메일/비밀번호 유효성 검사
  - [ ] createUserWithEmailAndPassword 호출
  - [ ] 에러 처리 (중복 이메일 등)
- [ ] 로그인 기능 구현
  - [ ] signInWithEmailAndPassword 호출
  - [ ] 에러 처리 (비밀번호 틀림 등)
- [ ] 자동 로그인 체크
  - [ ] 앱 시작 시 currentUser 확인
- [ ] 로그아웃 기능 구현

### 2.3 사용자 데이터 저장
- [ ] Firestore users 컬렉션 생성
- [ ] 회원가입 시 유저 문서 생성
  ```
  users/{userId}
  ├── email
  ├── gymId (null)
  └── createdAt
  ```

---

## Phase 3: 헬스장 검색 및 등록 (12/11~13)

### 3.1 헬스장 검색 화면 UI
- [ ] 카카오맵 지도 표시
- [ ] 검색바 구현
- [ ] 검색 결과 리스트 (BottomSheet 또는 RecyclerView)
- [ ] 내 위치 버튼

### 3.2 카카오맵 기능 구현
- [ ] 현재 위치 가져오기 (권한 요청)
- [ ] 키워드 검색 API 연동 ("헬스장" 검색)
- [ ] 검색 결과 마커 표시
- [ ] 마커 클릭 시 정보 표시

### 3.3 헬스장 선택 및 연동
- [ ] 검색 결과에서 헬스장 선택
- [ ] Firestore에서 해당 placeId 조회
  - [ ] 있으면: "정보 있음" 표시 → 바로 연동
  - [ ] 없으면: "새로 등록" 표시 → 등록 화면 이동
- [ ] 유저의 gymId 업데이트

### 3.4 헬스장 정보 등록 화면
- [ ] 헬스장 기본 정보 표시 (이름, 주소 - 카카오맵에서 가져옴)
- [ ] 운영 시간 입력 UI
  - [ ] 요일별 오픈/마감 시간
  - [ ] TimePicker 활용
- [ ] 보유 기구 선택 UI
  - [ ] 체크리스트 형태
  - [ ] 카테고리별 구분 (프리웨이트, 머신 등)
- [ ] 저장 버튼

### 3.5 Firestore 저장
- [ ] gyms 컬렉션에 헬스장 문서 생성
  ```
  gyms/{placeId}
  ├── name
  ├── address
  ├── placeId
  ├── registeredBy
  ├── hours/
  │   ├── mon: {open, close}
  │   └── ...
  ├── equipments: []
  └── createdAt
  ```

---

## Phase 4: 운동/기구 목록 (12/14~16)

### 4.1 기구 데이터 준비
- [ ] 기구 목록 JSON 작성
  ```json
  {
    "id": "bench_press",
    "name": "벤치프레스",
    "category": "프리웨이트",
    "targetMuscle": "가슴",
    "description": "가슴 운동의 기본..."
  }
  ```
- [ ] Firestore equipments 컬렉션에 업로드
- [ ] 또는 앱 내 로컬 데이터로 저장

### 4.2 운동 데이터 준비
- [ ] 운동 목록 JSON 작성
  ```json
  {
    "id": "flat_bench_press",
    "name": "플랫 벤치프레스",
    "equipmentId": "bench_press",
    "category": "가슴",
    "description": "가슴 중앙부 발달에 효과적...",
    "tips": "팔꿈치 각도 45도 유지",
    "youtubeUrl": "https://youtube.com/..."
  }
  ```
- [ ] Firestore exercises 컬렉션에 업로드

### 4.3 기구 목록 화면
- [ ] RecyclerView로 기구 목록 표시
- [ ] 카테고리별 필터/탭
- [ ] 내 헬스장 기구만 필터링 옵션
- [ ] 기구 아이템 클릭 → 운동 목록으로 이동

### 4.4 운동 목록 화면
- [ ] 선택한 기구의 운동 목록 표시
- [ ] 부위별 필터 (가슴/등/어깨/하체/팔)

### 4.5 운동 상세 화면
- [ ] 운동 이름
- [ ] 타겟 부위
- [ ] 운동 설명
- [ ] 주의사항/팁
- [ ] 유튜브 영상 (WebView 또는 썸네일+링크)
- [ ] 루틴에 추가 버튼

---

## Phase 5: 루틴 생성 및 관리 (12/17~19)

### 5.1 루틴 생성 화면
- [ ] 루틴 이름 입력
- [ ] 부위 선택 (가슴/등/어깨/하체/팔/전신)
- [ ] 운동 추가 버튼 → 운동 선택 화면
- [ ] 추가된 운동 리스트
  - [ ] 운동 이름
  - [ ] 세트 수 입력
  - [ ] 횟수 입력
  - [ ] 삭제 버튼
  - [ ] 순서 변경 (드래그)
- [ ] 저장 버튼

### 5.2 운동 선택 화면 (루틴용)
- [ ] 내 헬스장 기구 기반 운동만 표시
- [ ] 부위별 필터
- [ ] 운동 선택 → 루틴에 추가

### 5.3 루틴 목록 화면
- [ ] 내 루틴 리스트 (RecyclerView)
- [ ] 루틴 아이템
  - [ ] 이름
  - [ ] 부위
  - [ ] 운동 개수
- [ ] 루틴 클릭 → 상세/실행 화면
- [ ] 수정/삭제 옵션

### 5.4 루틴 상세/실행 화면
- [ ] 루틴 정보 표시
- [ ] 운동 리스트 (세트/횟수 포함)
- [ ] 각 운동 클릭 → 유튜브 영상 보기
- [ ] 수정 버튼
- [ ] 삭제 버튼

### 5.5 Firestore 저장
- [ ] routines 컬렉션
  ```
  routines/{oderId}/{routineId}
  ├── name
  ├── category
  ├── exercises: [
  │   {exerciseId, sets, reps}
  │ ]
  └── createdAt
  ```

---

## Phase 6: 홈 화면 및 마무리 (12/20~21)

### 6.1 홈 화면 구현
- [ ] 오늘 헬스장 운영 정보
  - [ ] 운영 여부 (열림/닫힘)
  - [ ] 운영 시간 표시
- [ ] 내 헬스장 이름 표시
- [ ] 내 루틴 바로가기 (최근 루틴 2~3개)
- [ ] 빠른 메뉴
  - [ ] 운동 목록
  - [ ] 루틴 관리
  - [ ] 헬스장 설정

### 6.2 Navigation 연결
- [ ] BottomNavigation 또는 Drawer 구현
- [ ] 홈 / 운동 / 루틴 / 설정 탭
- [ ] 화면 간 이동 테스트

### 6.3 설정 화면
- [ ] 내 헬스장 변경
- [ ] 로그아웃
- [ ] 앱 정보

### 6.4 테스트 및 버그 수정
- [ ] 전체 플로우 테스트
- [ ] 에러 처리 확인
- [ ] UI/UX 개선
- [ ] 엣지 케이스 처리

### 6.5 배포 준비
- [ ] 앱 아이콘 설정
- [ ] 앱 이름 최종 결정
- [ ] release 빌드 생성
- [ ] Firebase App Distribution 설정
- [ ] 테스트 링크 생성

---

## Firestore 보안 규칙

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    
    // 유저 데이터: 본인만 읽기/쓰기
    match /users/{userId} {
      allow read, write: if request.auth.uid == userId;
    }
    
    // 헬스장: 로그인 유저 읽기 가능, 등록자만 수정
    match /gyms/{gymId} {
      allow read: if request.auth != null;
      allow create: if request.auth != null;
      allow update, delete: if request.auth.uid == resource.data.registeredBy;
    }
    
    // 운동/기구: 모든 로그인 유저 읽기 가능
    match /exercises/{exerciseId} {
      allow read: if request.auth != null;
    }
    
    match /equipments/{equipmentId} {
      allow read: if request.auth != null;
    }
    
    // 루틴: 본인 것만 읽기/쓰기
    match /routines/{userId}/{routineId} {
      allow read, write: if request.auth.uid == userId;
    }
  }
}
```

---

## 기구 및 운동 초기 데이터

### 기구 카테고리
- 프리웨이트: 바벨, 덤벨, EZ바, 벤치
- 머신: 체스트프레스, 랫풀다운, 레그프레스, 레그컬, 레그익스텐션, 케이블머신
- 유산소: 트레드밀, 사이클, 로잉머신

### 부위별 주요 운동
- **가슴**: 벤치프레스, 인클라인 벤치프레스, 덤벨 플라이, 체스트프레스, 케이블 크로스오버
- **등**: 랫풀다운, 시티드로우, 바벨로우, 덤벨로우, 풀업
- **어깨**: 숄더프레스, 사이드 레터럴 레이즈, 프론트 레이즈, 페이스풀
- **하체**: 스쿼트, 레그프레스, 레그컬, 레그익스텐션, 런지
- **팔**: 바벨컬, 덤벨컬, 트라이셉 푸시다운, 스컬크러셔

---

## 참고 링크

- [Firebase Android 문서](https://firebase.google.com/docs/android/setup)
- [카카오맵 SDK 문서](https://apis.map.kakao.com/android/)
- [카카오 로컬 API (장소 검색)](https://developers.kakao.com/docs/latest/ko/local/dev-guide)

---

## 진행 상황

| Phase | 상태 | 완료일 |
|-------|------|--------|
| Phase 1: 프로젝트 세팅 | ⬜ 대기 | |
| Phase 2: 로그인/회원가입 | ⬜ 대기 | |
| Phase 3: 헬스장 검색/등록 | ⬜ 대기 | |
| Phase 4: 운동/기구 목록 | ⬜ 대기 | |
| Phase 5: 루틴 생성/관리 | ⬜ 대기 | |
| Phase 6: 홈 화면/마무리 | ⬜ 대기 | |
