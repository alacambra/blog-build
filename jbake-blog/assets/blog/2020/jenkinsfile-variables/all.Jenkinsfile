def someVar="a global var"

pipeline{
    agent {
        label 'maven-j13'
    }
  
    environment {
        ENV_DECLARED_VAR = "ENV_DECLARED_VAR-empty"
    }

    stages{
        stage('phase1') {
            steps{
                script {

                    echo "block 1"
                    def NOT_ENV_ON_STAGE="NOT_ENV_ON_STAGE-init_st1"
                    ENV_ON_STAGE="ENV_ON_STAGE-init_st1"
                    ENV_DECLARED_VAR="${ENV_DECLARED_VAR}-updated_st1"
                    echo "ENV_DECLARED_VAR=${ENV_DECLARED_VAR}"
                    echo "NOT_ENV_ON_STAGE=${NOT_ENV_ON_STAGE}"
                    echo "ENV_DECLARED_VAR=${ENV_DECLARED_VAR}"
                    print params;
                    print "getBinding().getVariables(): " + binding.variables
                    
;
                    print someVar
                }
                script {
                    echo "block 2"
                    
                    ENV_DECLARED_VAR="${ENV_DECLARED_VAR}-updated-st2"
                    NOT_ENV_ON_STAGE="${NOT_ENV_ON_STAGE}-init-updated_st2"
                    ENV_ON_STAGE="${ENV_ON_STAGE}-init-updated_st2"
                    
                    echo "ENV_DECLARED_VAR=${ENV_DECLARED_VAR}"
                    echo "NOT_ENV_ON_STAGE=${NOT_ENV_ON_STAGE}"
                    echo "ENV_ON_STAGE=${ENV_ON_STAGE}"
                    
                    print "using print now --> ENV_ON_STAGE=${ENV_ON_STAGE}"
                
                }

                sh script: "echo ${ENV_DECLARED_VAR}"
                sh script: "echo ${NOT_ENV_ON_STAGE}"
                sh script: "echo ${ENV_ON_STAGE}"                
                sh script: "export ENV_DECLARED_VAR=${env.ENV_DECLARED_VAR}-shell_updated && echo \${ENV_DECLARED_VAR}" 
                sh script: "echo \${ENV_DECLARED_VAR}"
            }
        }
        stage('phase2') {
            steps{
                script {
                    echo "block 1"
                    echo "ENV_DECLARED_VAR=${ENV_DECLARED_VAR}"
                    echo "NOT_ENV_ON_STAGE=${NOT_ENV_ON_STAGE}"
                    echo "ENV_DECLARED_VAR=${ENV_DECLARED_VAR}"
                    
                    print params
                    // print getBinding().getVariables()
                }
                
                // sh script: "echo 'printing with groovy in sh: ${getBinding().getVariables()}'"
                sh script: "export ENV_DECLARED_VAR=${env.ENV_DECLARED_VAR}-shell_updated && echo \${ENV_DECLARED_VAR}" 
                sh script: "echo 'print from shell env. \$ symbol is scaped so it is not interpreted by groovy but by shell '\${ENV_DECLARED_VAR}"
                sh script: "echo ${ENV_DECLARED_VAR}"
                sh script: "echo ${env.ENV_DECLARED_VAR}"
            }
        }
    }
}