# bbebig auth-server

## 필요 버전

- Node.js 20^
- Yarn 1.22.22

## Project Setup

### Requirement

1. v20 이상의 Node.js를 다운로드 합니다. (v20.18.x를 권장합니다.)
2. 의존성 설치를 위한 패키지 매니저 Yarn을 다운로드 합니다.
3. Container 구성을 위해 docker를 설치합니다.

### Start Server

1. 로컬 환경에서 해당 프로젝트의 REPO를 클론 한 뒤 해당 폴더로 이동합니다.

```bash
git clone https://github.com/bbebig/auth-server.git
cd src/backend/auth-server
```

2. 프로젝트 의존성을 설치하기 위해 아래 명령어를 실행합니다.

```bash
yarn install
```

3. 로컬 개발 서버를 실행하기 위해 아래 명령어를 실행합니다.

```bash
yarn dev
```

4. 프로덕션 서버를 실행하기 위해 아래 명령어를 실행합니다.

```bash
yarn build && yarn start
```

5. 도커 컨테이너를 실행하기 위해 아래 명령어를 실행합니다.

```bash
docker compose up -d
```
