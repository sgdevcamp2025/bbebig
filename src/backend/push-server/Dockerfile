# build stage
FROM gradle:7.6.4-jdk17 AS build

WORKDIR /apps
COPY . .
COPY ../common-module .

RUN ./gradlew clean generateProto
RUN ./gradlew build -x test

# runtime stage
FROM openjdk:17-jdk-slim

WORKDIR /apps
COPY --from=build /apps/build/libs/*.jar app.jar

EXPOSE 9060
CMD ["java", "-jar", "app.jar"]