# 1단계: 빌드
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

COPY ../demo1/gradlew settings.gradle build.gradle ./
COPY ../demo1/gradle ./gradle
RUN chmod +x gradlew

COPY ../demo1/src ./src

RUN ./gradlew clean bootJar --no-daemon

# 2단계: 실행
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
