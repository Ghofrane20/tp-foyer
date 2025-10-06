# Étape 1 : Construction du projet avec Maven
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier le fichier pom.xml et télécharger les dépendances en cache
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copier tout le code source et compiler le projet
COPY src ./src
RUN mvn clean package -DskipTests

# Étape 2 : Exécution de l'application
FROM eclipse-temurin:17-jdk-jammy

# Créer un dossier de travail dans l'image finale
WORKDIR /app

# Copier le JAR depuis le conteneur de build
COPY --from=builder /app/target/*.jar app.jar

# Exposer le port (adapter selon ton application)
EXPOSE 8080

# Commande de lancement
ENTRYPOINT ["java", "-jar", "app.jar"]
