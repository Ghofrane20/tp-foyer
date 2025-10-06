pipeline {
    agent any

    tools {
        // Noms exacts des outils configurés dans Jenkins
        maven 'M2_HOME'
        jdk 'JAVA_HOME'
    }

    environment {
        APP_ENV = "DEV"
        // SONARQUBE_TOKEN doit correspondre à l'ID du credential créé dans Jenkins
        SONARQUBE_TOKEN = credentials('sonar-token')
    }

    options {
        // Timeout global pour chaque étape (en cas de blocage)
        timeout(time: 10, unit: 'MINUTES')
    }

    stages {
        stage('Checkout') {
            steps {
                echo "=== Checkout de la branche main ==="
                git branch: 'main',
                    url: 'https://github.com/Ghofrane20/tp-foyer.git'
            }
        }

        stage('Build') {
            steps {
                echo "=== Build Maven (tests ignorés) ==="
                sh 'mvn clean install -Dmaven.test.skip=true'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo "=== Analyse SonarQube ==="
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

    post {
        always {
            echo "====== Pipeline terminé ======"
        }
        success {
            echo "===== Pipeline exécuté avec succès ====="
        }
        failure {
            echo "====== Échec du pipeline ======"
        }
    }
}
