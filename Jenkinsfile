pipeline {
    agent any

    options {
        skipDefaultCheckout(true)
        timestamps()
    }

    parameters {
        booleanParam(
            name: 'SEND_REPORT_EMAIL',
            defaultValue: true,
            description: 'Send the Extent report using the configured Jenkins credentials'
        )
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Test and report') {
            steps {
                script {
                    if (params.SEND_REPORT_EMAIL) {
                        withCredentials([
                            usernamePassword(
                                credentialsId: 'swaglabs-gmail-smtp',
                                usernameVariable: 'SWAGLABS_MAIL_FROM',
                                passwordVariable: 'SWAGLABS_MAIL_APP_PASSWORD'
                            ),
                            string(
                                credentialsId: 'swaglabs-report-recipient',
                                variable: 'SWAGLABS_MAIL_TO'
                            )
                        ]) {
                            sh '''
                                mvn -B clean test \
                                  -Dbrowser=chrome \
                                  -DsendReportEmail=true \
                                  -DextentReportPath="reports/TestReport-${BUILD_NUMBER}.html"
                            '''
                        }
                    } else {
                        sh '''
                            mvn -B clean test \
                              -Dbrowser=chrome \
                              -DsendReportEmail=false \
                              -DextentReportPath="reports/TestReport-${BUILD_NUMBER}.html"
                        '''
                    }
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts(
                artifacts: "reports/TestReport-${env.BUILD_NUMBER}.html,target/surefire-reports/TEST-*.xml",
                allowEmptyArchive: true
            )
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/TEST-*.xml'
        }
    }
}
