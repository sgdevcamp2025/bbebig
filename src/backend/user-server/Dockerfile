# build stage
FROM gradle:7.6.4-jdk17 AS build

WORKDIR /apps
COPY . .
RUN ./gradlew clean build --no-daemon


# runtime stage
FROM openjdk:17-jdk-slim

WORKDIR /apps
COPY --from=build /apps/build/libs/*.jar app.jar

EXPOSE 9010
CMD ["java", "-jar", "app.jar"]