pipeline{
    agent {
        label 'nodejs'
    }
  
    environment {
        ENV_DECLARED_VAR = "initilaized on global environment block"
    }

    stages{
        stage('read the env in groovy block') {
            steps{
                echo 'read the env in groovy block'
                script {
                    print "printing env var: ${ENV_DECLARED_VAR}"
                    print "printing env var: " + ENV_DECLARED_VAR
                }
            }
        }

        stage('read the env in shell script') {
            steps  {
                echo 'read the env in shell script' 
                script {
                    try {
                        sh "echo 'shell into script block: '\${ENV_DECLARED_VAR}"
                    } catch (err){
                        print "That happens because you need the prefix 'env': " + err 
                    }
                }
                sh "echo 'use env prefix:' ${env.ENV_DECLARED_VAR}"
                sh "echo 'do not use env prefix:' ${ENV_DECLARED_VAR}"
                sh "echo 'escape var symbol and do not use env prefix:' \${ENV_DECLARED_VAR}"
            }
        }
    }
}