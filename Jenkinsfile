pipeline {
    agent any

    tools {
        jdk 'jdk-21'
        nodejs 'node-20'
    }

    environment {
        MAVEN_OPTS = '-Xms256m -Xmx1g'
    }

    stages {
        stage('Backend Tests') {
            steps {
                sh 'mvn -B -pl backend -am clean verify'
            }
        }
        stage('Frontend Install') {
            steps {
                dir('frontend') {
                    sh 'npm install'
                }
            }
        }
        stage('Frontend Build') {
            steps {
                dir('frontend') {
                    sh 'npm run build'
                }
            }
        }
        stage('Quality Gate Placeholder') {
            steps {
                echo 'Integrate SonarQube analysis in this stage when server is available.'
            }
        }
    }

    post {
        always {
            junit 'backend/target/surefire-reports/*.xml'
            junit 'backend/target/failsafe-reports/*.xml'
        }
    }
}
