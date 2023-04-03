pipeline {
  agent any
  
  stages {
    stage('拉取代码') {
      steps {
        git branch: 'dev_pipeline', url: 'https://github.com/myuser/myrepo.git'
      }
    }
    
    stage('Unity构建') {
      steps {
        echo 'Hello World3'
      }
    }
  }
}