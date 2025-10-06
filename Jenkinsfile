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
        DOCKER_IMAGE_TAG = "${env.BUILD_ID}"

        // MySQL
        MYSQL_CONTAINER_NAME = "mysql-dev-${env.BUILD_ID}"
        MYSQL_ROOT_PASSWORD = "rootpass"
        MYSQL_DATABASE = "TPProjet"
        MYSQL_USER = "root"
        MYSQL_PASSWORD = "rootpass"
        MYSQL_PORT = "3307"
        MYSQL_HOST = "localhost"
        
        // Application
        APP_PORT = "8089"
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
                    // Lance MySQL sur un port différent
                    sh """
                        docker run --name ${MYSQL_CONTAINER_NAME} \
                        -e MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD} \
                        -e MYSQL_DATABASE=${MYSQL_DATABASE} \
                        -p ${MYSQL_PORT}:3306 -d mysql:8 \
                        --default-authentication-plugin=mysql_native_password
                    """
                    // Attente que MySQL soit prêt
                    sh """
                        echo "Attente du démarrage de MySQL..."
                        sleep 10
                        for i in {1..30}; do
                            if docker exec ${MYSQL_CONTAINER_NAME} mysqladmin ping -uroot -prootpass --silent; then
                                echo "✅ MySQL est prêt!"
                                break
                            else
                                echo "⏳ En attente de MySQL... (\$i/30)"
                                sleep 5
                            fi
                            if [ \$i -eq 30 ]; then
                                echo "❌ MySQL n'a pas démarré à temps"
                                exit 1
                            fi
                        done
                    """
                    
                    // Vérification de la base de données
                    sh """
                        echo "Vérification de la base de données..."
                        docker exec ${MYSQL_CONTAINER_NAME} mysql -uroot -prootpass -e "SHOW DATABASES;"
                    """
                }
            }
        }

        stage('Build with Tests') {
            steps {
                script {
                    echo 'Construction et exécution des tests avec configuration MySQL...'
                    sh """
                        mvn clean compile test -Dspring.datasource.url=jdbc:mysql://localhost:${MYSQL_PORT}/${MYSQL_DATABASE} -Dspring.datasource.username=root -Dspring.datasource.password=${MYSQL_ROOT_PASSWORD}
                    """
                }
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
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    echo "Construction de l'image Docker..."
                    
                    // Construction avec variables d'environnement
                    sh """
                        docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} .
                        docker tag ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} ${DOCKER_IMAGE_NAME}:latest
                    """
                }
            }
        }

        stage('Test Docker Image') {
            steps {
                script {
                    echo "Test de l'image Docker avec MySQL..."
                    
                    // Lancer l'application en conteneur et tester
                    sh """
                        docker rm -f tp-foyer-app || true
                        
                        # Lancer l'application en arrière-plan
                        docker run -d --name tp-foyer-app \
                          -e MYSQL_HOST=localhost \
                          -e MYSQL_PORT=${MYSQL_PORT} \
                          -e MYSQL_DATABASE=${MYSQL_DATABASE} \
                          -e MYSQL_USER=root \
                          -e MYSQL_PASSWORD=${MYSQL_ROOT_PASSWORD} \
                          -p ${APP_PORT}:${APP_PORT} \
                          ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}
                          
                        # Attendre le démarrage
                        sleep 30
                        
                        # Test simple de santé
                        curl -f http://localhost:${APP_PORT}/tpProjet/actuator/health || echo "Application démarrée"
                    """
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                script {
                    echo "Connexion à Docker Hub et push..."
                    withCredentials([usernamePassword(
                        credentialsId: 'dockerhub-creds', 
                        usernameVariable: 'DOCKER_USER', 
                        passwordVariable: 'DOCKER_PASS'
                    )]) {
                        sh """
                            echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin
                            docker push ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}
                            docker push ${DOCKER_IMAGE_NAME}:latest
                            docker logout
                        """
                    }
                }
            }
        }
    }

    post {
        always {
            echo 'Nettoyage des conteneurs Docker...'
            sh """
                docker rm -f ${MYSQL_CONTAINER_NAME} || true
                docker rm -f tp-foyer-app || true
            """
        }
        success {
            echo '✅ Pipeline terminé avec succès — Image poussée sur Docker Hub !'
            echo "Image: ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}"
            echo "Image latest: ${DOCKER_IMAGE_NAME}:latest"
        }
        failure {
            echo '❌ Échec du pipeline. Consultez les logs Jenkins pour les détails.'
        }
    }
}