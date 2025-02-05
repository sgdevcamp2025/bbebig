# build stage
FROM gradle:7.6.4-jdk17 AS build

WORKDIR /apps
COPY . .

WORKDIR /apps/signaling-server

RUN ./gradlew clean generateProto
RUN ./gradlew build -x test

# runtime stage
FROM openjdk:17-jdk-slim
WORKDIR /apps

COPY --from=build /apps/signaling-server/build/libs/*SNAPSHOT.jar app.jar

EXPOSE 9070
CMD ["java", "-jar", "app.jar"]