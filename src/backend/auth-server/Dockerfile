FROM node:20.18.1 AS builder

WORKDIR /app

COPY package.json yarn.lock ./
RUN yarn install --frozen-lockfile

COPY . .
COPY .env.production .

RUN yarn build

FROM node:20.18.1-alpine

WORKDIR /app

COPY --from=builder /app/package.json ./package.json
COPY --from=builder /app/yarn.lock ./yarn.lock
COPY --from=builder /app/dist ./dist
COPY --from=builder /app/prisma ./prisma

ENV NODE_ENV=production

RUN yarn install --production --frozen-lockfile

CMD ["yarn", "start"]
