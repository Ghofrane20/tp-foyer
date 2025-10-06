pipeline {
agent any

```
tools {
    maven 'M2_HOME'     // Nom de Maven dans Jenkins (Manage Jenkins > Global Tool Configuration)
    jdk 'JDK11'         // Nom du JDK configuré dans Jenkins
}

environment {
    GIT_REPO_URL = 'https://github.com/Ghofrane20/tp-foyer.git'
    SONARQUBE_SERVER = 'SonarQube'                // Nom du serveur SonarQube configuré dans Jenkins
    SONARQUBE_TOKEN = credentials('sonar-token')  // ID du token SonarQube dans Credentials
}

stages {
    stage('Pull from Git') {
        steps {
            echo '📥 Clonage du projet depuis GitHub...'
            git branch: 'main', url: "${GIT_REPO_URL}"
        }
    }

    stage('Clean Project') {
        steps {
            echo '🧹 Nettoyage du projet Maven...'
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
            echo '🔍 Analyse de la qualité du code avec SonarQube...'
            withSonarQubeEnv("${SONARQUBE_SERVER}") {
                sh """
                    mvn sonar:sonar \
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
            sh 'mvn package -DskipTests'
        }
    }

    stage('Archive Artifact') {
        steps {
            echo '🗃️ Archivage du JAR généré...'
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
        }
    }
}

post {
    always {
        echo '🔄 Exécution terminée (succès ou échec).'
    }
    success {
        echo '✅ Pipeline exécuté avec succès !'
    }
    failure {
        echo '❌ Le pipeline a échoué. Consulte les logs Jenkins.'
    }
}
```

}
