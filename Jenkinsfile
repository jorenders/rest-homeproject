pipeline {
  agent any
  stages {
    stage('Check-Out') {
      steps {
        git(url: 'https://github.com/jorenders', branch: 'rest-homeproject', poll: true)
      }
    }
  }
}