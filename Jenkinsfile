pipeline {
    agent any

    tools {
        maven 'Maven'     // Nom de l'installation Maven configurée dans Jenkins (Manage Jenkins > Global Tool Configuration)
        jdk 'JDK11'       // Nom de l'installation JDK configurée dans Jenkins
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
                // "sonar-token" doit être l'ID du credential que tu as créé dans Jenkins
                SONARQUBE_TOKEN = credentials('sonar-token')
            }
            steps {
                // "SonarQube" doit être le nom du serveur configuré dans Jenkins (Manage Jenkins > Configure System > SonarQube servers)
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
