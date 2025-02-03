# build stage
FROM gradle:7.6.4-jdk17 AS build

WORKDIR /apps
COPY . .

RUN ./gradlew :apigateway-server:clean :apigateway-server:generateProto
RUN ./gradlew :apigateway-server:bootJar -x test

# (디버깅용 ls 명령)
RUN ls -al /apps/apigateway-server/build/libs/

# runtime stage
FROM openjdk:17-jdk-slim

WORKDIR /apps
COPY --from=build /apps/apigateway-server/build/libs/*.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]