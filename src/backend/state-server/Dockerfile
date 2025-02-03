# build stage
FROM gradle:7.6.4-jdk17 AS build

WORKDIR /apps
COPY . .

WORKDIR /apps/state-server

RUN gradle clean generateProto
RUN gradle bootJar -x test

# runtime stage
FROM openjdk:17-jdk-slim
WORKDIR /apps

COPY --from=build /apps/state-server/build/libs/*.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]