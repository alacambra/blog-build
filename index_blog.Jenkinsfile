def applicationName = "solr-indexing";

pipeline{
    agent {
        label 'maven'
    }

    stages{
        stage('build-solr-indexing') {
            steps{
                sh script: "cd ${applicationName} && mvn clean package"
            }
        }
        stage('index-entries') {
            steps{
                sh script: "cd ${applicationName} && mvn exec:java -Dexec.mainClass=tech.lacambra.blog.solr_indexing.Indexer -Dexec.args=\"../jbake-blog/content/blog/\""
            }
        }
    }
}