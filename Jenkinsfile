def applicationName = "blog";

pipeline{
    agent {
        docker { image 'alacambra/jbake' }
    }

    stages{
            stage('build-site') {
                    steps{
                         sh 'jbake -h'
                    }
            }
    }
}