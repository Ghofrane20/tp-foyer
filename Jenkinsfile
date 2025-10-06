pipeline {
    agent any

    tools {
        // Vérifie que ces noms correspondent à tes installations Jenkins
        maven 'M2_HOME'
        jdk 'JAVA_HOME'
    }

    environment {
        // SonarQube
        SONARQUBE_TOKEN = credentials('sonar-token')

        // Docker
        DOCKER_IMAGE_NAME = "ghofranejomni/tp-foyer"
        DOCKER_IMAGE_TAG  = "latest"
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Récupération du code source depuis GitHub...'
                git branch: 'main',
                    url: 'https://github.com/Ghofrane20/tp-foyer.git'
            }
        }

        stage('Build') {
            steps {
                echo 'Nettoyage et compilation du projet...'
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                echo 'Exécution des tests unitaires...'
                sh 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo 'Analyse du code avec SonarQube...'
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

        stage('Quality Gate') {
            steps {
                echo 'Vérification du Quality Gate SonarQube...'
                timeout(time: 1, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Build and Push Docker Image') {
            steps {
                script {
                    echo "Construction de l'image Docker..."
                    sh "docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} ."

                    echo "Connexion à Docker Hub et push..."
                    
                    // 🔹 Utilisation du credential Docker Hub créé dans Jenkins
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh "echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin"
                        sh "docker push ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}"
                        sh "docker logout"
                    }
                }
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline terminé avec succès — Image poussée sur Docker Hub !'
        }
        failure {
            echo '❌ Échec du pipeline. Consultez les logs Jenkins pour les détails.'
        }
    }
}
