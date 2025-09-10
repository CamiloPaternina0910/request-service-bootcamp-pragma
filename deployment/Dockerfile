FROM gradle:8.7-jdk17 AS build

WORKDIR /app

COPY gradle/ gradle/
COPY gradlew gradlew
COPY gradlew.bat gradlew.bat
COPY gradle/wrapper/ gradle/wrapper/
COPY build.gradle settings.gradle main.gradle gradle.properties ./

COPY applications/ ./applications/
COPY domain/ ./domain/
COPY domain/ ./domain/
COPY infrastructure/ ./infrastructure/

RUN apt-get update && apt-get install -y dos2unix && dos2unix ./gradlew
RUN chmod +x ./gradlew
RUN ./gradlew :app-service:build --no-daemon -x test -x validateStructure

# ============================
# Etapa 2: Runtime
# ============================
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/applications/app-service/build/libs/*.jar app-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app-service.jar"]
