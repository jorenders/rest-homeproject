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
    stage('Report Unittesting') {
      parallel {
        stage('Report') {
          steps {
            junit 'target/surefire-reports/**/*.xml'
          }
        }
        stage('Report Coverage') {
          steps {
            $class: 'CloverPublisher',
                cloverReportDir: 'target/site',
                cloverReportFileName: 'clover.xml',
                healthyTarget: [methodCoverage: 70, conditionalCoverage: 80, statementCoverage: 80], // optional, default is: method=70, conditional=80, statement=80
                unhealthyTarget: [methodCoverage: 50, conditionalCoverage: 50, statementCoverage: 50], // optional, default is none
                failingTarget: [methodCoverage: 0, conditionalCoverage: 0, statementCoverage: 0]     // optional, default is none
          }
        }
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