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
                discordSend description: "**Build:** [${currentBuild.id}](${env.BUILD_URL})\n**Status:** [${currentBuild.currentResult}](${env.BUILD_URL})\n${changes}\n\n[**Artifacts on Jenkins**](https://ci.alysaa.net/job/GeyserAdminTools/job)", footer: 'Open Collaboration Jenkins', link: env.BUILD_URL, successful: currentBuild.resultIsBetterOrEqualTo('SUCCESS'), title: "${env.JOB_NAME} #${currentBuild.id}", webhookURL: "https://discord.com/api/webhooks/829602972098887720/kscr0LGNfA6cyYEtg0Gkfzu0gD4jmun6x-p3xPW2_xhH3BmOQD6ytc7jFx1j6cuTqlRq"
                  }

                }
        }
}
