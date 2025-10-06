pipeline {
agent any

```
environment {
    // 🔧 Variables globales (à adapter à ton environnement Jenkins)
    GIT_REPO_URL = 'https://github.com/ton-utilisateur/ton-projet.git'
    SONARQUBE_SERVER = 'SonarQube'              // Nom du serveur configuré dans Jenkins (Manage Jenkins > Configure System)
    SONARQUBE_TOKEN = credentials('sonar-token') // ID du token SonarQube ajouté dans Credentials
    MAVEN_HOME = tool 'Maven'                   // Nom de l’installation Maven dans Jenkins
    JAVA_HOME = tool 'JDK11'                    // Nom de l’installation Java dans Jenkins
    PATH = "${JAVA_HOME}/bin:${MAVEN_HOME}/bin:${env.PATH}"
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
            sh 'mvn clean'
        }
    }

    stage('Compile Project') {
        steps {
            echo '⚙️ Compilation du projet...'
            sh 'mvn compile'
        }
    }

    stage('SonarQube Analysis') {
        steps {
            echo '🔍 Analyse du code avec SonarQube...'
            withSonarQubeEnv("${SONARQUBE_SERVER}") {
                sh """
                    mvn sonar:sonar \
                    -Dsonar.projectKey=mon-projet \
                    -Dsonar.host.url=${SONAR_HOST_URL} \
                    -Dsonar.login=${SONARQUBE_TOKEN}
                """
            }
        }
    }

    stage('Build JAR') {
        steps {
            echo '📦 Génération du fichier JAR...'
            sh 'mvn package -DskipTests'
        }
    }

    stage('Archive Artifact') {
        steps {
            echo '🗃️ Archivage du fichier JAR généré...'
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
```

}
