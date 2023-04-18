// 执行命令并且获取返回值
def CallCmd(cmdline) {
  def isWindows = !isUnix()
  if (isWindows) {
    def result = bat(returnStatus: true, script: cmdline)
    return result
    }else {
    def result = sh(returnStatus: true, script: cmdline)
    return result
  }
}

pipeline {
  agent any

  environment {
    Unity2021_Exe_PATH = 'C:/Program Files/Unity/Hub/Editor/2021.3.16f1/Editor/Unity.exe'
  }

  stages {
    stage('更新工程') {
      steps {
        script {
          // 调用工程更新
          // 特别注意，加了params.的jenkins参数booleanParam才是有类型的bool，否则是string。extendedChoice得到的参数都是string
          if (params.enableProjectUpdate) {
            def cmdArg = ''
            if (params.versionControl == '0') {
              cmdArg = """
                git -C "${projectPath}" pull
              """
            }
            else if (params.versionControl == '1') {
              cmdArg = """
                svn update "${projectPath}"
              """
            }
            if (cmdArg != '') {
              echo 'Start update project...'
              def exitCode = CallCmd(cmdArg)
              if (exitCode != 0) {
                error('update project failed!')
              }
              return
            }
          }

          echo 'Skip Update Project!'
        }
      }
    }

    stage('准备构建参数') {
      steps {
        script {
          //判断打包平台,用于控制打包方法和输出目录
          def buildMethod = 'AutoBuild.AutoBuildEntry.'
          def finalOutputPath = outputPath

          switch (params.buildPlatform) {
            case '0':
              //windows
              buildMethod += 'BuildWindows'
              finalOutputPath +=  '/Windows'
              break
            case '1':
              //Android
              buildMethod += 'BuildAndroid'
              finalOutputPath += '/Android'
              break
            case '2':
              //iOS
              buildMethod += 'BuildiOS'
              finalOutputPath += '/iOS'
              break
            default :
              error('Build Platform not support!')
              break
          }

          //获取一些参数来自定义的构建名
          def buildDisplayName = currentBuild.displayName
          buildDisplayName = buildDisplayName.startsWith('#') ? buildDisplayName.substring(1) : buildDisplayName
          def formattedDate = new Date().format('yyyy-MM-dd')
          def buildVersionName = "${JOB_NAME}_${buildDisplayName}_${formattedDate}"
          echo "buildVersionName:${buildVersionName}"

          //调用unity的命令行参数
          env.unity_execute_arg = ("-quit -batchmode -nographics -logfile -executeMethod ${buildMethod} \"buildPlatform|${buildPlatform}\" "
          + "\"outputPath|${finalOutputPath}\" \"buildVersionName|${buildVersionName}\" \"buildMode|${buildMode}\" "
          + "\"enableUnityDevelopment|${enableUnityDevelopment}\" \"enableGameDevelopment|${enableGameDevelopment}\" "
          )
        }
      }
    }

    stage('Unity构建') {
      steps {
        script {
          def cmdArg = "\"${Unity2021_Exe_PATH}\" ${env.unity_execute_arg}"
          def exitCode = CallCmd(cmdArg)
          if (exitCode != 0) {
            error('unity build target failed!')
          }
        }
      }
    }
  }
}
