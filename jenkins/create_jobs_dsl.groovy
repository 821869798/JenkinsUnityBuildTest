pipelineJob('trunk-Pipeline') {
    // Define job properties
    description('trunk-Pipeline')
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