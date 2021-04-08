pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building..'
                sh 'mvn clean package'
            }
        }
        stage('Post') {
            steps {
                archiveArtifacts 'target/GeyserAdminTools-1.0-SNAPSHOT.jar'
                discordSend description: "**Build:**", footer: "GeyserAdminTools", link: env.BUILD_URL, result: currentBuild.currentResult, title: New build GeyserAdminTools, webhookURL: "https://discord.com/api/webhooks/829602972098887720/kscr0LGNfA6cyYEtg0Gkfzu0gD4jmun6x-p3xPW2_xhH3BmOQD6ytc7jFx1j6cuTqlRq"
                  }

                }
        }
}
