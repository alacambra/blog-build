def applicationName = "blog";

pipeline{
    agent {
        label 'maven'
    }

    stages{
            stage('build-site') {
                    steps{
                         docker { image 'alacambra/jbake' }
                    }
            }
    }
}