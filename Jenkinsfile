pipeline {
	    agent any
	    stages {
	           stage('Maven build') {
	               steps {
                             echo 'Maven build Started'
	                     bat "mvn -f SpringBootRestApiExample/pom.xml -Dmaven.test.failure.ignore=true clean package"
	               }
	           }

                   stage('SonarCube') {
	               steps {
	                     echo 'SonarCube checking following code:'
                             echo 'Drop59: MedicationProfileService2020-01-16_02'
	               }
	           }

	           stage('Publish To Nexus') {
	               steps {
                             echo 'Publish To Nexus Started'
			     nexusPublisher nexusInstanceId: 'localnexus3', nexusRepositoryId: 'CDR_Central', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: 'SpringBootRestApiExample\\target\\SpringBootRestApiExample-1.0.0.jar']], mavenCoordinate: [artifactId: 'Drop59', groupId: 'Weblogic', packaging: 'jar', version: '$DeploymentFile']]]
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
