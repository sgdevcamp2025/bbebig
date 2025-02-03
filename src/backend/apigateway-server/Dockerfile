# build stage
FROM gradle:7.6.4-jdk17 AS build

WORKDIR /apps
COPY . .
COPY ../common-module .

RUN ./gradlew clean generateProto
RUN ./gradlew build -x test

RUN ls -al /apps/

# runtime stage
FROM openjdk:17-jdk-slim

WORKDIR /apps
RUN ls -al /apps/
COPY --from=build /apps/build/libs/*.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]