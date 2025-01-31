FROM gradle:7.6.4-jdk17 AS builder
WORKDIR /app
COPY . .
RUN ./gradlew clean build -x test --no-daemon

FROM eclipse-temurin:17-jdk
WORKDIR /app
# 빌드 결과물(jar)만 복사
COPY --from=builder /app/apigateway-server/build/libs/apigateway-server.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]