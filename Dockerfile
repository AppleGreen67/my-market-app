FROM gradle:8.7-jdk21-alpine AS build

WORKDIR /app

COPY gradle gradle
COPY build.gradle .
COPY gradlew .
COPY settings.gradle .
COPY src src

RUN chmod +x gradlew

RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine

COPY --from=build /app/build/libs/*.jar y6-market.jar

EXPOSE 8080

CMD ["java", "-jar", "y6-market.jar"]