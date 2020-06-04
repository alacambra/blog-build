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
                sh "echo 'use env prefix \${env.ENV_DECLARED_VAR}: ' ${env.ENV_DECLARED_VAR}"
                sh "echo 'do not use env prefix \${ENV_DECLARED_VAR}:' ${ENV_DECLARED_VAR}"
                sh "echo 'escape var symbol and do not use env prefix \\\${ENV_DECLARED_VAR}: ' \${ENV_DECLARED_VAR}"
            }
        }
        stage('read the env in groovy block using shell script') {
            steps  {
                echo 'read the env in groovy block using shell script'
                script {
                    sh "echo 'shell into script block \${ENV_DECLARED_VAR}: '${ENV_DECLARED_VAR}"
                    sh "echo 'use env prefix \${env.ENV_DECLARED_VAR}:' ${env.ENV_DECLARED_VAR}"
                    sh "echo 'do not use env prefix \${ENV_DECLARED_VAR}: ' ${ENV_DECLARED_VAR}"
                    sh "echo 'escape var symbol and do not use env prefix \\\${ENV_DECLARED_VAR}: ' \${ENV_DECLARED_VAR}"
                }
            }
        }
    }
}