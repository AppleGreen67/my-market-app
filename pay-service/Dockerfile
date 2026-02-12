FROM gradle:8.7-jdk21-alpine AS build

WORKDIR /app

COPY gradle gradle
COPY build.gradle .
COPY gradlew .
COPY settings.gradle .

COPY pay-service/build.gradle ./pay-service/build.gradle
COPY pay-service/src ./pay-service/src

RUN chmod +x gradlew

RUN gradle :pay-service:bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine

COPY --from=build /app/pay-service/build/libs/*.jar y7-pay.jar

EXPOSE 8081

CMD ["java", "-jar", "y7-pay.jar"]