pipeline {
    agent any

    tools {
        maven 'M2_HOME'
    }

    options {
        // Timeout après allocation de l'agent
        timeout(time: 1, unit: 'SECONDS')
    }

    environment {
        APP_ENV = "DEV"
    }

    stages {
        stage('Code Checkout') {
            steps {
                // Récupération du code depuis la branche 'main'
                git branch: 'main',
                    url: 'https://github.com/Ghofrane20/tp-foyer.git'
            }
        }

        stage('Code Build') {
            steps {
                // Build Maven en ignorant les tests
                sh 'mvn install -Dmaven.test.skip=true'
            }
        }
    }

    post {
        always {
            echo "======always======"
        }
        success {
            echo "=====pipeline executed successfully ====="
        }
        failure {
            echo "======pipeline execution failed======"
        }
    }
}
