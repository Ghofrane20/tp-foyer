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
        DOCKER_IMAGE_TAG  = "${env.BUILD_ID}"

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
                    echo "Démarrage du conteneur MySQL..."

                    // Nettoyage ancien conteneur et volume
                    sh "docker rm -f mysql-dev || true"
                    sh "docker volume rm mysql-data || true"
                    sh "docker volume create mysql-data"

                    // Lancement du conteneur MySQL avec ulimits augmentés
                    sh """
                    docker run --name mysql-dev \
                      -e MYSQL_ROOT_PASSWORD=rootpass \
                      -e MYSQL_DATABASE=TPProjet \
                      -p 3307:3306 \
                      -v mysql-data:/var/lib/mysql \
                      --ulimit nofile=262144:262144 \
                      -d mysql:8 \
                      --default-authentication-plugin=mysql_native_password
                    """

                    // Attendre quelques secondes pour le démarrage initial
                    echo "Attente initiale de 10 secondes..."
                    sleep 10

                    // Vérification des logs Docker
                    sh "docker logs mysql-dev"

                    // Attente que MySQL soit prêt avec timeout prolongé
                    def retries = 20
                    def ready = false
                    for (int i = 0; i < retries; i++) {
                        try {
                            def exitCode = sh(
                                script: "docker exec mysql-dev mysqladmin ping -uroot -prootpass --silent",
                                returnStatus: true
                            )
                            if (exitCode == 0) {
                                ready = true
                                echo "✅ MySQL prêt !"
                                break
                            }
                        } catch (Exception e) {
                            echo "⏳ En attente de MySQL (${i+1}/${retries})..."
                        }
                        sleep 5
                    }

                    if (!ready) {
                        echo "❌ MySQL n'a pas démarré après ${retries*5} secondes."
                        echo "Logs MySQL:"
                        sh "docker logs mysql-dev || true"
                        echo "État du conteneur:"
                        sh "docker ps -a | grep mysql-dev || true"
                        error("Impossible de continuer, MySQL n'est pas opérationnel")
                    }

                    // Vérification finale
                    sh "docker exec mysql-dev mysql -uroot -prootpass -e 'SHOW DATABASES;'"
                }
            }
        }

        stage('Build and Test') {
            steps {
                echo 'Construction et tests du projet...'
                sh 'mvn clean compile test'
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

        stage('Package Application') {
            steps {
                echo 'Packaging de l\'application...'
                sh 'mvn package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    echo "Construction de l'image Docker..."
                    sh """
                        docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} .
                        docker tag ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} ${DOCKER_IMAGE_NAME}:latest
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
            echo "Nettoyage des conteneurs et volumes Docker..."
            sh "docker rm -f mysql-dev || true"
            sh "docker volume rm mysql-data || true"
        }
        success {
            echo '✅ Pipeline terminé avec succès — Image poussée sur Docker Hub !'
            echo "Image: ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}"
            echo "Disponible sur: https://hub.docker.com/r/${DOCKER_IMAGE_NAME}"
        }
        failure {
            echo '❌ Échec du pipeline. Consultez les logs Jenkins pour les détails.'
        }
    }
}