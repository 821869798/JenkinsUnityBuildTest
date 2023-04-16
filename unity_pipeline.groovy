pipeline {
  agent any

  parameters {
    string defaultValue: 'D:\\program\\my\\JenkinsUnityAutoBuild', description: '打包项目所在的目录', name: 'projectPath'
    string defaultValue: 'D:\\program\\my\\testout', description: '打包的输出目录', name: 'outputPath'
    extendedChoice defaultValue: '0', description: '选择打包平台', descriptionPropertyValue: 'Windows64,Android,iOS', multiSelectDelimiter: ',', name: 'buildPlatform', quoteValue: false, saveJSONParameterToFile: false, type: 'PT_SINGLE_SELECT', value: '0,1,2', visibleItemCount: 3
    extendedChoice defaultValue: '0', description: '选择打包模式，如果资源（lua代码也是资源，C#代码不是）没变动，可以选择之后的几项', descriptionPropertyValue: '全量打包,不打包AssetBundle，直接Build,只重新编译C#代码（不包括lua），不打AssetBundle和场景', multiSelectDelimiter: ',', name: 'buildMode', quoteValue: false, saveJSONParameterToFile: false, type: 'PT_SINGLE_SELECT', value: '0,1,2', visibleItemCount: 3
    booleanParam description: '开启unity的developmentbuild', name: 'enableUnityDevelopment'
    booleanParam description: 'Game的开发者模式,指代码的逻辑是开发者模式', name: 'enableGameDevelopment'
    extendedChoice defaultValue: '0', description: '版本控制软件', descriptionPropertyValue: 'Git(需要安装Git),SVN(需要安装SVN，并有SVN命令可用)', multiSelectDelimiter: ',', name: 'versionControl', quoteValue: false, saveJSONParameterToFile: false, type: 'PT_SINGLE_SELECT', value: '0,1', visibleItemCount: 5
    booleanParam description: '使用Git或者SVN更新项目', name: 'enableProjectUpdate'
  }

  stages {
    stage('更新工程'){
      steps {
        script {
          def isWindows = !isUnix()
          // 调用工程更新
          // 特别注意，加了params.的jenkins参数booleanParam才是有类型的bool，否则是string。extendedChoice得到的参数都是string
          if (params.enableProjectUpdate) {
            def cmdArg = ""
            if (params.versionControl == "0" ) {
              cmdArg = """
                git -C "${projectPath}" pull 
              """
            }
            else if (params.versionControl == "1" ) {
              cmdArg = """
                svn update "${projectPath}"
              """
            }
            if ( cmdArg != "" ) {
              echo "Start update project..."
              if (isWindows) {
                bat cmdArg
              } else {
                sh cmdArg
              }
              return
            }
          }

          echo "Skip Update Project!"

        }
      }
    }

    stage('准备构建参数') {
      steps {
        script {
          //判断打包平台,用于控制打包方法和输出目录
          def buildMethod = "AutoBuild.AutoBuildEntry."
          def finalOutputPath = outputPath

          switch (params.buildPlatform) {
            case "0":
              //windows
              buildMethod += "BuildWindows"
              finalOutputPath +=  "/Windows" 
              break
            case "1":
              //Android
              buildMethod += "BuildAndroid"
              finalOutputPath += "/Android" 
              break
            case "2":
              //iOS
              buildMethod += "BuildiOS"
              finalOutputPath += "/iOS" 
              break
            default :
              error("Build Platform not support!")
              break
          }
    
          //获取一些参数来自定义的构建名
          def buildDisplayName = currentBuild.displayName
          buildDisplayName = buildDisplayName.startsWith("#") ? buildDisplayName.substring(1) : buildDisplayName
          def formattedDate = new Date().format("yyyy-MM-dd")
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
          def unity_exe = "D:/software/install/Unity/2021.3.16f1/Editor/Unity.exe"
          def isWindows = !isUnix()
          if (isWindows) {
            bat """${unity_exe} ${env.unity_execute_arg}"""
          } else {
            sh """${unity_exe} ${env.unity_execute_arg}"""
          }
        }
      }
    }
  }
}
