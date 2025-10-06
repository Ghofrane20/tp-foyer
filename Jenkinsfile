pipeline {
    agent any

    tools {
        maven 'M2_HOME'    // Nom exact de Maven dans Jenkins
        jdk 'JAVA_HOME'    // Nom exact du JDK dans Jenkins
    }

    stages {
        stage('Checkout') {
            steps {
                // Récupération du projet depuis GitHub
                git branch: 'main',
                    url: 'https://github.com/Ghofrane20/tp-foyer.git'
            }
        }

        stage('Build') {
            steps {
                // Nettoyage et compilation du projet
                sh 'mvn clean compile'
            }
        }

        stage('SonarQube Analysis') {
            environment {
                // "sonar-token" doit être l'ID du credential créé dans Jenkins
                SONARQUBE_TOKEN = credentials('sonar-token')
            }
            steps {
                // "SonarQube" doit être le nom du serveur configuré dans Jenkins
                withSonarQubeEnv('SonarQube') {
                    sh """
                        mvn sonar:sonar \
                        -Dsonar.projectKey=tp-foyer \
                        -Dsonar.host.url=http://192.168.33.10:9000 \
                        -Dsonar.login=${SONARQUBE_TOKEN}
                    """
                }
            }
        }
    }
}
