FROM gradle:8.7-jdk21-alpine AS build

WORKDIR /app

COPY gradle gradle
COPY build.gradle .
COPY gradlew .
COPY settings.gradle .

COPY market/build.gradle ./market/build.gradle
COPY market/src ./market/src

RUN chmod +x gradlew

RUN gradle :market:bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine

COPY --from=build /app/market/build/libs/*.jar y7-market.jar

EXPOSE 8080

CMD ["java", "-jar", "y7-market.jar"]