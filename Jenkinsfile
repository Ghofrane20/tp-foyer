pipeline {
    agent any

    environment {
        GIT_REPO_URL = 'https://github.com/Ghofrane20/tp-foyer.git'
        SONARQUBE_SERVER = 'SonarQube'
        SONARQUBE_TOKEN = credentials('sonar-token')
        M2_HOME = tool 'Maven'
        JAVA_HOME = tool 'JDK11'
        PATH = "${JAVA_HOME}/bin:${M2_HOME}/bin:${env.PATH}"
    }

    stages {
        stage('Checkout Code') {
            steps {
                echo '📥 Clonage du projet depuis GitHub...'
                git branch: 'main', url: "${GIT_REPO_URL}"
            }
        }

        stage('Clean Project') {
            steps {
                echo '🧹 Nettoyage du projet...'
                sh "${M2_HOME}/bin/mvn clean"
            }
        }

        stage('Compile Project') {
            steps {
                echo '⚙️ Compilation du projet...'
                sh "${M2_HOME}/bin/mvn compile"
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo '🔍 Analyse du code avec SonarQube...'
                withSonarQubeEnv("${SONARQUBE_SERVER}") {
                    sh """
                        ${M2_HOME}/bin/mvn sonar:sonar \
                        -Dsonar.projectKey=tp-foyer \
                        -Dsonar.host.url=${SONAR_HOST_URL} \
                        -Dsonar.login=${SONARQUBE_TOKEN}
                    """
                }
            }
        }

        stage('Build JAR') {
            steps {
                echo '📦 Génération du fichier JAR...'
                sh "${M2_HOME}/bin/mvn package -DskipTests"
            }
        }

        stage('Archive Artifact') {
            steps {
                echo '🗃️ Archivage du fichier JAR...'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline exécuté avec succès !'
        }
        failure {
            echo '❌ Le pipeline a échoué. Vérifiez les logs Jenkins.'
        }
    }
}
