pipeline {
  agent any
  stages {
    stage('Check-Out') {
      steps {
        git(url: 'https://github.com/jorenders/rest-homeproject', poll: true)
      }
    }
  }
}