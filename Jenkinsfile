def applicationName = "blog";

pipeline{
    agent {
        label 'jbake'
    }

    stages{
        stage('build-blog') {
            steps{
                sh script: "jbake -b jbake-blog/"
            }
        }
        stage('copy-blog') {
            steps{
                sh script: "cp -Rf jbake-blog/output s2i-nginx/files/ "
            }
        }
        stage('s2i build'){
            steps{
                script{
                    openshift.withCluster(){
                        openshift.withProject(){
                            def build = openshift.selector("bc", applicationName);
                            def startedBuild = build.startBuild("--from-file=\"./s2i-nginx/files\"");
                            startedBuild.logs('-f');
                            echo "${applicationName} build status: ${startedBuild.object().status}";
                        }
                    }
                }
            }
        }
    }
}