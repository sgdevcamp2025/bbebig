# 데이터베이스 스키마를 연결하는 방법

1. env에 올바른 데이터베이스 정보를 입력해주세요.

   > DATABASE_URL="mysql://root:1234@localhost:3306/bbebig"

2. 데이터베이스 정보를 입력하면 자동으로 스키마가 연결됩니다.

3. 스키마가 연결되면 데이터베이스에 접속하고 정보를 불러올 수 있습니다.

```bash
npx prisma db pull
```

4. 스키마를 기반으로 Prisma Client를 생성합니다.

```bash
npx prisma generate
```
