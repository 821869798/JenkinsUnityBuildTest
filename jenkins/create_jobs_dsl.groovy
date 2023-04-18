//定义choice的函数
def SimpleExtendedChoice(n, t, count, delimiter, v, dv, descValue, desc) {
    def choice = {
        name(n)
        type(t)
        value(v)
        defaultValue(dv)
        visibleItemCount(count)
        multiSelectDelimiter(delimiter)
        descriptionPropertyValue(descValue)
        description(desc)
        saveJSONParameterToFile(false)
        quoteValue(false)
        // The name of the parameter.
        projectName('')
        propertyFile('')
        groovyScript('')
        groovyScriptFile('')
        bindings('')
        groovyClasspath('')
        propertyKey('')
        defaultPropertyFile('')
        defaultGroovyScript('')
        defaultGroovyScriptFile('')
        defaultBindings('')
        defaultGroovyClasspath('')
        defaultPropertyKey('')
        descriptionPropertyFile('')
        descriptionGroovyScript('')
        descriptionGroovyScriptFile('')
        descriptionBindings('')
        descriptionGroovyClasspath('')
        descriptionPropertyKey('')
        javascriptFile('')
        javascript('')
    }
    choice
}

pipelineJob('trunk-Pipeline') {
    // Define job properties
    description('trunk-Pipeline')
    //job parameters
    parameters {
        stringParam('projectPath', 'D:\\program\\my\\JenkinsUnityAutoBuild', '打包项目所在的目录')
        stringParam('outputPath', 'D:\\program\\my\\testout', '打包的输出目录')
        extendedChoice SimpleExtendedChoice('buildPlatform','PT_SINGLE_SELECT',3,',','0,1,2','0','Windows64,Android,iOS','选择打包平台')
        extendedChoice SimpleExtendedChoice('buildMode','PT_SINGLE_SELECT',3,',','0,1,2','0','全量打包,不打包AssetBundle，直接Build,只重新编译C#代码（不包括lua），不打AssetBundle和场景','选择打包模式，如果资源（lua代码也是资源，C#代码不是）没变动，可以选择之后的几项')
        booleanParam('enableUnityDevelopment',false,'开启unity的developmentbuild')
        booleanParam('enableGameDevelopment',false,'Game的开发者模式,指代码的逻辑是开发者模式')
        extendedChoice SimpleExtendedChoice('versionControl','PT_SINGLE_SELECT',2,',','0,1','0','Git(需要安装Git),SVN(需要安装SVN，并有SVN命令可用)','版本控制软件')
        booleanParam('enableProjectUpdate',false,'使用Git或者SVN更新项目')
    }

    definition {
        cps {
            script(readFileFromWorkspace('jenkins/unity_pipeline.groovy'))
            sandbox()
        }
    }
}

listView('trunk-view') {
    //description('All unstable jobs for project A')
    filterBuildQueue()
    filterExecutors()
    jobs {
        //name('trunk-Pipeline')
        regex(/trunk-.+/)
    }
    columns {
        status()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
        buildButton()
    }
}
