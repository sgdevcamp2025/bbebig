## BBeBig Client

### 최소 요구 사항

- Node.js 20.x 이상
- Yarn 4.x 이상

### 폴더 구조

```bash
src/
  ├── __test__ # 테스트 파일
  ├── apis # API 관련 파일
  ├── assets # 정적 파일
  ├── components # 컴포넌트
  ├── constants # 상수
  ├── hooks # 훅
  ├── libs # 라이브러리
  ├── pages # 페이지
  ├── routes # 라우트
  ├── store # 상태관리
  ├── styles # 스타일
  ├── types # 타입
  └── utils # 유틸리티
```

### 로컬환경 스크립트

```bash
# 개발 서버 실행
yarn dev

# 빌드
yarn build

# 빌드 파일 실행
yarn start
```

### 도커 환경 설정

```bash
# 도커 컨테이너 실행
docker compose up -d
```
