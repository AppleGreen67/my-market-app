FROM gradle:8.7-jdk21-alpine AS build

WORKDIR /app

COPY gradle gradle
COPY build.gradle .
COPY gradlew .
COPY settings.gradle .

COPY market/build.gradle ./market/build.gradle
COPY market/src ./market/src
COPY pay-service/build.gradle ./pay-service/build.gradle
COPY pay-service/src ./pay-service/src

# COPY src src

RUN chmod +x gradlew

# RUN ./gradlew bootJar --no-daemon
RUN gradle :market:bootJar --no-daemon


FROM eclipse-temurin:21-jre-alpine

COPY --from=build /app/market/build/libs/*.jar y6-market.jar

EXPOSE 8080

CMD ["java", "-jar", "y6-market.jar"]