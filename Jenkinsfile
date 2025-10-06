pipeline {
    agent any

    tools {
        maven 'M2_HOME'
        jdk 'JAVA_HOME'
    }

    environment {
        // SonarQube
        SONARQUBE_TOKEN = credentials('sonar-token')

        // Docker
        DOCKER_IMAGE_NAME = "ghofranejomni/tp-foyer"
        DOCKER_IMAGE_TAG  = "latest"

        // MySQL
        MYSQL_CONTAINER_NAME = "mysql-dev"
        MYSQL_ROOT_PASSWORD  = "rootpass"
        MYSQL_DATABASE       = "tp_foyer_db"
        MYSQL_USER           = "tp_user"
        MYSQL_PASSWORD       = "tp_pass"
        MYSQL_PORT           = "3306"
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Récupération du code source depuis GitHub...'
                git branch: 'main',
                    url: 'https://github.com/Ghofrane20/tp-foyer.git'
            }
        }

        stage('Start MySQL') {
            steps {
                script {
                    echo 'Démarrage du conteneur MySQL...'
                    // Supprime le conteneur s'il existe déjà
                    sh "docker rm -f ${MYSQL_CONTAINER_NAME} || true"
                    // Lance MySQL
                    sh """
                        docker run --name ${MYSQL_CONTAINER_NAME} \
                        -e MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD} \
                        -e MYSQL_DATABASE=${MYSQL_DATABASE} \
                        -e MYSQL_USER=${MYSQL_USER} \
                        -e MYSQL_PASSWORD=${MYSQL_PASSWORD} \
                        -p ${MYSQL_PORT}:3306 -d mysql:8
                    """
                    // Attente que MySQL soit prêt
                    sh '''
                        echo "Attente de MySQL..."
                        RETRIES=10
                        until docker exec ${MYSQL_CONTAINER_NAME} mysqladmin ping -h "localhost" --silent; do
                            echo "MySQL non prêt, attente 5 secondes..."
                            sleep 5
                            ((RETRIES--))
                            if [ $RETRIES -le 0 ]; then
                                echo "❌ MySQL n'a pas démarré à temps !"
                                exit 1
                            fi
                        done
                        echo "✅ MySQL prêt !"
                    '''
                }
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
            // Nettoyage du conteneur MySQL
            sh "docker rm -f ${MYSQL_CONTAINER_NAME} || true"
        }
        failure {
            echo '❌ Échec du pipeline. Consultez les logs Jenkins pour les détails.'
            // Nettoyage du conteneur MySQL même en cas d'échec
            sh "docker rm -f ${MYSQL_CONTAINER_NAME} || true"
        }
    }
}
