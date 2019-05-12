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
        junit 'target/surefire-reports/**/*.xml'
      }
    }
    
    stage('Archive Artifacts') {
      steps {
        archiveArtifacts 'target/*.jar,target/*.tar'
      }
    }
    
    stage('Delete workspace') {
      steps {
        cleanWs(deleteDirs: true)
      }
    }
  }
  post {
    success {
      mail(subject: 'Jenkins build SUCCESS', from: 'desktop-renders@jenkins.com', to: 'jo.renders@gmail.com', body: 'Er is succesvolle build gerund')
    }

    unstable {
      mail(subject: 'Jenkins build UNSTABLE', from: 'desktop-renders@jenkins.com', to: 'jo.renders@gmail.com', body: 'Er is onstabiele build gerund')
    }

    failure {
      mail(subject: 'Jenkins build ERROR', from: 'desktop-renders@jenkins.com', to: 'jo.renders@gmail.com', body: 'Er is blocking error in de build')
    }
  }
}
