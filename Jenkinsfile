pipeline {
	    agent any
	    stages {
	           stage('---Test---') {
	               steps {
	                     echo 'Testing..'
	               }
	           }

	           stage('---package--') {
	               steps {
	                     bat "mvn -f SpringBootRestApiExample/pom.xml -Dmaven.test.failure.ignore=true clean package"
	                     echo 'Packaging..'
	               }
	           }

	           stage('---Publish--') {
	               steps {
			     nexusPublisher nexusInstanceId: 'localnexus3', nexusRepositoryId: 'maven-releases', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: '$WORKDIR']], mavenCoordinate: [artifactId: 'Drop59', groupId: 'CDR_central', packaging: 'jar', version: '$FILENAME+$VERSION']]]
	               }
	           }
	    }

   	    post { 
	         always { 
			echo 'Cleaning workspace...'
	                cleanWs()
	         }
	    }
} 
