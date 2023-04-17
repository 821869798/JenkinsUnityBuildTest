pipeline {
    agent any
    stages {
        stage('拉取脚本工程') {
            steps {
                git branch: 'dev_pipeline', url: 'https://github.com/821869798/JenkinsUnityAutoBuild.git'
            }
        }
        stage('使用dsl创建jobs') {
            steps {
                jobDsl sandbox: true, targets: 'jenkins/create_jobs_dsl.groovy'
            }
        }
    }
}
