# 의존성 설치 스테이지
FROM node:20-alpine AS dependencies
WORKDIR /app

# 먼저 Yarn 관련 파일들을 복사
COPY .yarn ./.yarn
COPY package.json yarn.lock .yarnrc.yml ./

# Yarn Berry 설정
RUN corepack enable && corepack prepare yarn@4.6.0 --activate
RUN yarn install

# 빌드 스테이지
FROM node:20-alpine AS builder
WORKDIR /app
COPY . .
COPY --from=dependencies /app/node_modules ./node_modules
RUN corepack enable && corepack prepare yarn@4.6.0 --activate
RUN yarn build

# 실행 스테이지
FROM node:20-alpine AS runner
WORKDIR /app
COPY package.json yarn.lock ./
COPY --from=builder /app/build ./build
COPY --from=dependencies /app/node_modules ./node_modules

CMD ["yarn", "start"]