def applicationName = "blog";

pipeline{
    agent {
        label 'maven'
    }

    stages{
        stage('clear-all-build') {
            steps{
                sh script: "rm -rf /opt/jbake-structure/*"
            }
        }
        stage('copy-blog') {
            steps{
                sh script: "cp -Rf jbake-structure/* /opt/jbake-structure/"
            }
        }
        stage('build-blog') {
            steps{
                sh script: "jbake -b /opt/jbake-structure/"
            }
        }
    }
}