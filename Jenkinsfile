pipeline {
  agent any
  stages {
    stage('Check-Out') {
      steps {
        git(url: 'https://github.com/jorenders/rest-homeproject', poll: true)
      }
    }
    stage('Unit Tests') {
      steps {
        bat 'echo %PATH%'
        bat 'mvn clean install test -Dmaven.test.failure.ignore=true'
      }
    }
    stage('Report') {
      steps {
        junit 'surefire-reports/**/*.xml'
      }
    }
    stage('Archive Artifacts') {
      steps {
        archiveArtifacts 'target/*.jar,target/*.tar'
      }
    }
  }
}