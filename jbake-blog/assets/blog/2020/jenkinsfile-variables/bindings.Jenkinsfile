pipeline{
    agent {
        label 'nodejs'
    }
  
    environment {
        ENV_DECLARED_VAR = "ENV_VAR"
    }

    stages{
        stage('Play with bindings: write') {
            steps{
                echo 'write the env in groovy block'
                script {
                    print ENV_DECLARED_VAR
                    print env.ENV_DECLARED_VAR
                    print "bindings " + binding.variables
                    def defVar = "defVar"
                    print "bindings " + binding.variables
                    aVar = "aVar"
                    print "bindings " + binding.variables

                    print env.ENV_DECLARED_VAR
                    binding.variables["t"] = "test"
                }
            }
        }

        stage('Play with bindings: read') {
            steps{
                script {
                    print "bindings " + binding.variables
                    print "bindings " + binding
                    print binding.variables[aVar]
                    print aVar
                    print binding
                }

                sh "echo $aVar"
            }
        }
        
        stage('Play with env') {
            environment {
                ENV_DECLARED_VAR = "CHANGED"
            }
            steps{
                script {
                    print "binding.properties: " + binding.properties
                    print "binding: " + binding
                    print aVar;
                    aVar = "changed var"
                    print aVar;
                    
                    //https://javadoc.jenkins.io/plugin/workflow-cps/org/jenkinsci/plugins/workflow/cps/EnvActionImpl.html
                    print "env: " + env
                    print "env.properties: " + env.properties
                    print "env.getEnvironment(): " + env.getEnvironment() 
                    print "env.getOverriddenEnvironment(): " + env.getOverriddenEnvironment() 
                    print 'env.getProperty("ENV_DECLARED_VAR"): ' + env.getProperty("ENV_DECLARED_VAR")
                    env.setProperty("ENV_DECLARED_VAR", "anotherValue")
                    print 'env.setProperty("ENV_DECLARED_VAR", "anotherValue"): ' + env.getProperty("ENV_DECLARED_VAR")
                    env.ENV_DECLARED_VAR = "overrriden";
                    print 'env.ENV_DECLARED_VAR="overrriden": ' + env.ENV_DECLARED_VAR + " - " +  env.getProperty("ENV_DECLARED_VAR")
                }
            }
        }


        stage('Reflexion') {
            steps{
                script {
                    print aVar;    
                    print 'env.ENV_DECLARED_VAR - env.getProperty("ENV_DECLARED_VAR"): ' + env.ENV_DECLARED_VAR + " - " +  env.getProperty("ENV_DECLARED_VAR")

                    for(m in this.getClass().getMethods()){
                        print "metod: " + m.getName();
                    }                

                    for(f in this.getClass().getDeclaredFields()){
                        print "declared field: " + f.getName();
                    }                
                }
            }
        }
    }
}