pipeline {
    agent any
    enviroment {
        APP_NAME = 'reggie'
        APP_VERSION = '1.0-SNAPSHOT'
        PACKAGE_NAME = 'com.reggie.takeout'
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Test') {
            steps {

            }
        }

        stage('Deploy') {
            steps {

            }
        }
    }

    post {
        faliure {
            echo 'Pipeline Failure :('
        }
    }
}