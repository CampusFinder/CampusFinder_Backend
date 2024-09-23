# 빌드 단계
FROM openjdk:17-alpine as build
WORKDIR /app
COPY . .
RUN ./gradlew clean build

# 실행 단계
FROM openjdk:17-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
