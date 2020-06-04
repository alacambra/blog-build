pipeline{
    agent {
        label 'nodejs'
    }
  
    environment {
        ENV_DECLARED_VAR = "INIT"
    }

    stages{
        stage('write the env in groovy block') {
            steps{
                echo 'write the env in groovy block'
                script {
                    print "printing original env var: " + ENV_DECLARED_VAR
                    print "bindings " + binding.variables
                    ENV_DECLARED_VAR = ENV_DECLARED_VAR + "_UPDATE"
                    env.ENV_DECLARED_VAR = ENV_DECLARED_VAR + "_UPDATE2"
                    print "printing manipulated env var: " + ENV_DECLARED_VAR
                    print "bindings " + binding.variables
                }
            }
        }
        stage('read the env in groovy block') {
            steps  {
                script {
                    print "bindings " + binding.variables

                    echo 'read the env in groovy block' 
                    
                    try{
                        print 'use env prefix env.ENV_DECLARED_VAR:' + env.ENV_DECLARED_VAR
                    }catch(err){
                        print "Error because env is not known in groovy context " + err
                    }
                    
                    //Updated
                    print 'do not use env prefix ENV_DECLARED_VAR:' + ENV_DECLARED_VAR
                    print "bindings " + binding.variables

                }
            }
        }
        stage('read the env in shell script (sh)') {
            steps  {
                echo 'read the env in shell script (sh)' 
                
                //Not updated
                sh "echo 'use env prefix \${env.ENV_DECLARED_VAR}:' ${env.ENV_DECLARED_VAR}"
                
                //Updated
                sh "echo 'do not use env prefix \${ENV_DECLARED_VAR}:' ${ENV_DECLARED_VAR}"

                //Not updated
                sh "echo 'escape var symbol and do not use env prefix \\\${ENV_DECLARED_VAR}:' \${ENV_DECLARED_VAR}"
            }
        }

        stage('read the env in sh script (sh script)') {
            steps  {
                echo 'read the env in sh script (sh script)' 
                
                //Not updated
                sh script: "echo 'use env prefix \${env.ENV_DECLARED_VAR}:' ${env.ENV_DECLARED_VAR}"
                
                //Updated
                sh script: "echo 'do not use env prefix \${ENV_DECLARED_VAR}:' ${ENV_DECLARED_VAR}"

                //Not updated
                sh script: "echo 'escape var symbol and do not use env prefix \\\${ENV_DECLARED_VAR}:' \${ENV_DECLARED_VAR}"

            }
        }
        
        stage('read the env in groovy block using shell script') {
            steps  {
                echo 'read the env in groovy block using shell script'
                script {
                    // Not updated
                    sh "echo 'shell into script block \\\${ENV_DECLARED_VAR}: '\${ENV_DECLARED_VAR}"

                    // Not updated
                    sh "echo 'use env prefix \${env.ENV_DECLARED_VAR}:' ${env.ENV_DECLARED_VAR}"

                    //Updated
                    sh "echo 'do not use env prefix \${ENV_DECLARED_VAR}:' ${ENV_DECLARED_VAR}"

                    //Triggers an error
                    try {
                        sh "echo 'escape var symbol and use env prefix \\\${env.ENV_DECLARED_VAR}:' \${env.ENV_DECLARED_VAR}"
                    } catch (err){
                        print "That happens because the bash context does not know the env prefix, used by groovy en replacing: " + err 
                    }
                    sh "echo 'escape var symbol and do not use env prefix \\\${ENV_DECLARED_VAR}:' \${ENV_DECLARED_VAR}"
                }
            }
        }
    }
}