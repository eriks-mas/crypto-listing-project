pipeline {
    agent any

    environment {
        MAVEN_OPTS = '-Xms256m -Xmx1g'
        // Use system Java and Node paths
        JAVA_HOME = '/opt/homebrew/opt/openjdk@21'
        PATH = "/opt/homebrew/opt/openjdk@21/bin:/opt/homebrew/bin:$PATH"
    }

    stages {
        stage('Environment Check') {
            steps {
                script {
                    echo "Checking environment..."
                    sh 'java -version'
                    sh 'mvn -version'
                    sh 'node -v'
                    sh 'npm -v'
                    sh 'pwd && ls -la'
                }
            }
        }
        
        stage('Backend Tests') {
            steps {
                script {
                    echo "Running backend tests..."
                    sh 'mvn -B -pl backend -am clean verify'
                }
            }
        }
        
        stage('Frontend Install & Build') {
            steps {
                dir('frontend') {
                    script {
                        echo "Installing frontend dependencies..."
                        sh 'npm install'
                        echo "Building frontend..."
                        sh 'npm run build'
                    }
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
            script {
                echo 'Publishing test results...'
                // Check for backend test results
                def surefireReports = 'backend/target/surefire-reports/*.xml'
                def failsafeReports = 'backend/target/failsafe-reports/*.xml'
                
                try {
                    if (fileExists('backend/target/surefire-reports/') && 
                        sh(script: "ls backend/target/surefire-reports/*.xml 2>/dev/null | wc -l", returnStdout: true).trim() != '0') {
                        echo 'Publishing unit test results...'
                        junit surefireReports
                    } else {
                        echo 'No unit test results found'
                    }
                    
                    if (fileExists('backend/target/failsafe-reports/') && 
                        sh(script: "ls backend/target/failsafe-reports/*.xml 2>/dev/null | wc -l", returnStdout: true).trim() != '0') {
                        echo 'Publishing integration test results...'
                        junit failsafeReports
                    } else {
                        echo 'No integration test results found'
                    }
                } catch (Exception e) {
                    echo "Error publishing test results: ${e.getMessage()}"
                }
            }
        }
        success {
            echo 'Build completed successfully! Backend tests passed and frontend built.'
        }
        failure {
            echo 'Build failed. Check console output for details.'
        }
    }
}
