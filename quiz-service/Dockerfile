FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN ./gradlew bootJar

EXPOSE 8082

CMD ["java", "-jar", "build/libs/quiz-service-0.0.1-SNAPSHOT.jar"]