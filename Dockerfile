# Étape de build
FROM eclipse-temurin:17-jdk-jammy as builder
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw .
COPY pom.xml .
RUN ./mvnw dependency:go-offline -B

COPY src/ src
RUN ./mvnw clean install -DskipTests

# Étape d'exécution
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/todo-backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]