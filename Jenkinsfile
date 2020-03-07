pipeline {
	    agent any
	    stages {
	           stage('Maven_build') {
	               steps {
                             echo 'Maven build Started'
	                     bat "mvn -f SpringBootRestApiExample/pom.xml -Dmaven.test.failure.ignore=true clean package"
	               }
	           }

                   stage('SonarCube') {
	               steps {
	                     echo 'SonarCube_Started'
                             echo(env.getEnvironment().collect({environmentVariable ->  "${environmentVariable.key} = ${environmentVariable.value}"}).join("\n"))
	               }
	           }

	           stage('Publish_To_Nexus') {
	               steps {
                             echo 'Publish To Nexus Started'
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
