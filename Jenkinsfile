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
                discordSend description: "Jenkins Build", footer: "Footer Text", link: ("https://ci.alysaa.net/job/GeyserAdminTools/job/master/lastSuccessfulBuild/artifact/target/GeyserAdminTools-1.0-SNAPSHOT.jar"), result: currentBuild.currentResult, title: GeyserAdminTools, webhookURL: ("https://discord.com/api/webhooks/829602972098887720/kscr0LGNfA6cyYEtg0Gkfzu0gD4jmun6x-p3xPW2_xhH3BmOQD6ytc7jFx1j6cuTqlRq")
                  }

                }
        }
}
