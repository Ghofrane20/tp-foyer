# Étape 1 : Construction du projet avec Maven
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# Étape 2 : Exécution de l'application
FROM eclipse-temurin:17-jre-jammy

RUN groupadd -r spring && useradd -r -g spring spring
USER spring

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8089

# L'application utilisera les variables d'environnement au runtime
ENTRYPOINT ["java", "-jar", "app.jar"]