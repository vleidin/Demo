**********************************************************************************************************
    D:\backup\java\SpringBoot\CDR_microservices\DispenseStatus_20211231_0900.zip
**********************************************************************************************************

0010. Changes
0015. APIGW Swagger
0020. Create "DispenseStatus" Spring Boot project
0030. Update "pom.xml"
0031. Update "pom.xml" for swagger suggested by Teja
0035. Update "/src/main/resources"
0040. Create new packages and helper classes
0050. Create entity classes
0055. DataAccessImpl.java 2020-04-01 Version
0080. Create "CDR_LIB" Classpatch Variable.
0090. Adapt microservice to run in Weblogic application server
0092. WebService project properties verification.
0093. SQL on DIT
0094. SQL on SIT
0095. How to build
0097. How to deploy in Weblogic application server

0099. How to run from Eclipse
0100. How to run from local Weblogic
0101. How to run from dit Weblogic
0102. How to run from sit Weblogic
0103. How to run from uat Weblogic
0104. How to run from pt  Weblogic
0105. How to run from prod Weblogic

0110. How to print all beans used by this application




===================================================================================================
                      0010. Changes
===================================================================================================


-----------------------------------------------
RestFul service Sequence diagram example:
-----------------------------------------------

current API version generates incorrect response layout:
{
    "apierror": {
        "status": "BAD_REQUEST",
        "timestamp": "27-03-2020 12:25:56",
        "message": "PTNTNotFoundException occured. ResponseGetRx{No RxNum found in CDR DB for =postLookupData [StoreNumber=null, RxNumber=null, Banner=null]}",
        "debugMessage": null,
        "subErrors": null
    }
}



BUT CORRECT LAYOUT SHOULD BE THIS:
----------------------------------------
400 should align to error model.
{
  "error": {
    "code": 400,
    "message": "Patient not found in CDR DB for LCL StoreNum = 1010, KROLL RxNum = 1029131, Banner = SDM" 
    "api": "PostLookupData",
    "id": "0000014de941853d-5fd81b",
    "source":weblogic
    "time": "Fri Jul 13 05:38:10 2018"
  }
}




--------------------------------------------------
Swagger error model
--------------------------------------------------
400 Patient not found
------------------------
{
  "error": {
    "api": "string                          Example: PostLookupData",
    "code": "int                            Example: 400 or 500",
    "id": "string                           Example: 6df376d5-5215-4f7a-8cf9-0ae9640ef5a6",
    "message": "string",
    "source": "string                       Example: weblogic",
    "time": "EEE MMM dd hh:mm:ss yyyy       Example: Thu Apr 02 02:45:17 2020"
  }
}


--------------------------------------------
500 CDR weblogic lookup service unavailable
--------------------------------------------
{
  "error": {
    "api": "string                          Example: PostLookupData",
    "code": "int                            Example: 400 or 500",
    "id": "string                           Example: 6df376d5-5215-4f7a-8cf9-0ae9640ef5a6",
    "message": "string",
    "source": "string                       Example: weblogic",
    "time": "EEE MMM dd hh:mm:ss yyyy       Example: Thu Apr 02 02:45:17 2020"
  }
}






--------------------------------------------------
Have to format "id" to correspond above layout
--------------------------------------------------
"timestamp": "Mon Mar 30 01:57:06 2020"
 "id": "009ea74a-9b78-40a5-9ef4-dc36253f9620",

expectation:
 "id": "0000014de941853d-5fd81b",



--------------
POST Request
--------------
{
  "postLookupData": {
    "banner": "LCL",
    "rxNumber": "1011247",
    "storeNumber": "0199"  -->  ON --> KrollRxNum
                                MB --> ParentRxNum
  }
}


 


===================================================================================================
                         0015. APIGW Swagger
===================================================================================================
https://api-dev1.loblaw.ca/postLookupData?swagger

 
APIGW error model:
----------------------
PatientError:
     type: object
      properties:
        error:
              type: object
              properties:

                code:
                    type: string
                    description: HTTP response error code
                    example: 400

                message:
                    type: string
                    description: Patient Not Found Exception
                    example: Patient Not Found Exception

                api:
                    type: string
                    description: API policy name
                    example: PostTransitionalData

                id:
                    type: string
                    description: Transaction ID of the request
                    example: 0000014de941853d-5fd81b

                time:
                     type: string
                     description: Transaction timestamp
                     example: 'Fri Jul 13 05:38:10 2018'

 


===================================================================================================
               0020. Create "DispenseStatus" Spring Boot project
===================================================================================================
Start your "Spring Tool Suite" and then

<File>
<New> ----> <Spring Starter Project>

Name .......:DispenseStatus    Packaging: War
Group ......:com.sdm.lookup         # This will correspond to the main package created in next step 
Artifact ...:DispenseStatus         # This will be Eclipse project name
Version ....:20200319
Description :DispenseStatus
Package ....:com.sdm.lookup

<Next>

?????????????????????????????????????????????????????????????????????
Perhaps no need to select below 2 check boxes .....
"New Spring Starter Project Dependencies" wizard appears
Select here the following two:

       SQL
         x Oracle Driver


       Web
         x Spring Web Services
         
<Next>
<Finish>




=====================================================================================================
                             0030. Update "pom.xml"
=====================================================================================================


        <properties>
		<java.version>1.8</java.version>
		<org.mapstruct.version>1.3.0.Final</org.mapstruct.version>
	</properties>

--------------------------------
Add additional <plugins>:
--------------------------------
	<build>
		<plugins>
			<plugin>
				  <groupId>org.springframework.boot</groupId>
				  <artifactId>spring-boot-maven-plugin</artifactId>
				  <configuration>
				   	      <mainClass>com.sdm.lookup.DispenseStatusApplication</mainClass>
				  </configuration>
			</plugin>
				
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-surefire-plugin</artifactId>
				<configuration>
					<testFailureIgnore>true</testFailureIgnore>
				</configuration>
			</plugin>
			
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<configuration>
					<source>${java.version}</source>
					<target>${java.version}</target>
					<annotationProcessorPaths>
						<path>
							<groupId>org.mapstruct</groupId>
							<artifactId>mapstruct-processor</artifactId>
							<version>${org.mapstruct.version}</version>
						</path>
					</annotationProcessorPaths>
				</configuration>
			</plugin>

		</plugins>
	</build>


--------------------------------
Add additional dependencies:
--------------------------------
	<dependencies>
		<dependency>
			<groupId>org.mapstruct</groupId>
			<artifactId>mapstruct</artifactId>
			<version>${org.mapstruct.version}</version>
		</dependency>
		
	    <!--
		This is to prevent the conflict between spring-boot-starter-data-rest dependency
		and swagger dependency described in SpringBoot_err.txt, see
		PluginRegistry did not exist
		
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-rest</artifactId>
		</dependency>
		-->		
		
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
		
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-tomcat</artifactId>
			<scope>provided</scope>
		</dependency>

		<dependency>
			<groupId>org.modelmapper</groupId>
			<artifactId>modelmapper</artifactId>
			<version>2.3.4</version>
		</dependency>
		
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-tomcat</artifactId>
			<scope>provided</scope>
		</dependency>

		<dependency>
			<groupId>javax.persistence</groupId>
			<artifactId>javax.persistence-api</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-jdbc</artifactId>
			<exclusions>
				<exclusion>
					<groupId>org.apache.tomcat</groupId>
					<artifactId>tomcat-jdbc</artifactId>
				</exclusion>
			</exclusions>
		</dependency>

		<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger2</artifactId>
			<version>2.8.0</version>
		</dependency>
		<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger-ui</artifactId>
			<version>2.8.0</version>
		</dependency>

		<dependency>
			<groupId>org.apache.httpcomponents</groupId>
			<artifactId>httpclient</artifactId>
		</dependency>

		<dependency>
			<groupId>org.apache.commons</groupId>
			<artifactId>commons-lang3</artifactId>
		</dependency>

	</dependencies>



  
================================================================================================
        0031. Update "pom.xml" for swagger suggested by Teja
================================================================================================
[‎3/‎27/‎2020 1:54 PM]  Teja Pavan Kumar Kondapalli (LCL-C):
  
   <dependency>
      <groupId>io.swagger</groupId>
      <artifactId>swagger-models</artifactId>
      <version>1.5.21</version>
    </dependency> 

   <dependency>
      <groupId>io.springfox</groupId>
      <artifactId>springfox-swagger2</artifactId>
      <version>2.1.9</version>
      <exclusions>
        <exclusion>
          <groupId>io.swagger</groupId>
          <artifactId>swagger-models</artifactId>
        </exclusion>
      </exclusions>
    </dependency> 





================================================================================================
                       0035. Update "/src/main/resources"
================================================================================================

 Directory of D:\sts-4.3.1.RELEASE\workspace4\DispenseStatus\src\main\resources

2020-03-21  11:20 AM    <DIR>          .
2020-03-21  11:20 AM    <DIR>          ..
2020-03-21  11:17 AM               287 application-dit.properties
2020-03-21  11:14 AM               677 application-local.properties
2020-03-21  11:12 AM               306 application-prod.properties
2020-03-21  11:12 AM               306 application-pt.properties
2020-03-21  11:12 AM               306 application-sit.properties
2020-03-21  11:12 AM               306 application-uat.properties
2020-03-21  09:15 AM                29 application.properties
2020-03-21  08:47 AM             1,750 logback_properties.xml
2020-03-21  11:20 AM    <DIR>          static
2020-03-21  11:20 AM    <DIR>          templates



-------------------------------------
Relace in logback_properties.xml
-------------------------------------
From: <logger name="com.loblaw.nctm">
  to: <logger name="com.sdm.lookup">



================================================================================================
            0040. Create new packages and helper classes
================================================================================================

com.sdm.lookup
--------------------------
              DispenseStatusApplication.java
              Swagger2Config.java
              ServletInitializer.java


--------------------------
com.sdm.lookup.controller
--------------------------
               Controller.java


---------------------------------
com.sdm.lookup.exceptionhandling
---------------------------------
               ApiError.java
               CPFMException.java
               EntityNotFoundException.java
               InvalidPPRFileDataException.java
               PPRFileExistsException.java
               RestExceptionHandler.java

               
               ErrorSelector.java            ---> my version
               PTNTNotFoundException.java    ---> my version deprecated

---------------------------------
com.sdm.lookup.entity
---------------------------------
               ErrorTemplate.java
               CDREnumerations.java
               postLookupData.java
               GetLookupResponse.java
               RequestGetRx.java
               ResponseGetRx.java


---------------------------------
com.sdm.lookup.service
---------------------------------
               BusinessLogic.java
               BusinessLogicImpl.java


---------------------------------
com.sdm.lookup.util
---------------------------------
               CommonUtil.java


---------------------------------
com.sdm.lookup.repository
---------------------------------
               DataAccess.java
               DataAccessImpl.java





================================================================================================
                      0050. Create entity classes
================================================================================================
---------------------------------
com.sdm.lookup.entity
---------------------------------
               RequestGetRx.java
               ResponseGetRx.java
               postLookupData.java
               GetLookupResponse.java


To generate serialVersionUID select "postLookupData" class name and press "CTRL + 1"

To generate Getters/Setters methods

Press "Alt+Shift, S" Then shoose the option you want to perform:
      Generate Getters and Setters...
      Generate toString()...




================================================================================================
                       0055. DataAccessImpl.java 2020-04-01 Version
================================================================================================
package com.sdm.lookup.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import org.springframework.dao.DataIntegrityViolationException; 
import org.springframework.dao.DuplicateKeyException; 
import org.springframework.dao.EmptyResultDataAccessException;

import com.sdm.lookup.entity.postLookupData;
import com.sdm.lookup.util.CommonUtil;
import com.sdm.lookup.entity.GetLookupResponse;
import com.sdm.lookup.entity.ResponseGetRx;


@Repository
public class DataAccessImpl implements DataAccess {
	   static Logger logger = LoggerFactory.getLogger(DataAccessImpl.class);
	   private static String RxNumSQL = null;
	   
	   static {
		    RxNumSQL = CommonUtil.readFile("getRxNumSQL.md");
//		    logger.info("3 SQL File content: " + RxNumSQL);
	   }
	   
	   
	   @Autowired
	   private JdbcTemplate jdbcTemplate;

//	   @Autowired
//	   private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	   private SimpleJdbcInsert simpleJdbcInsert;
	   

	   
	   @Override
	   public GetLookupResponse getSDMstoreData(postLookupData req) {
//		      GetLookupResponse mockTest = new GetLookupResponse("4925", "9000322", "SDM");
		      GetLookupResponse mockTest = new GetLookupResponse("4925", "RxNum_Not_Found", "SDM");
//		      GetLookupResponse mockTest = new GetLookupResponse("4925", "CDR weblogic lookup service unavailable", "SDM");
		      return mockTest;	
	   }
	   
	   
	   
/*	   
	   @Override
	   public GetLookupResponse getSDMstoreData(postLookupData req) {
		    try {
		        return jdbcTemplate.queryForObject(RxNumSQL, new Object[] {req.getStoreNumber(),
		        		                                                   req.getRxNumber()
		        		                                                  },  (rs, rowNum) ->
		                new GetLookupResponse (
		                                       rs.getString("StoreNumber"),
		                                       rs.getString("RxNumber"),
		                                       rs.getString("Banner")
		                                       )
		                );
		    } catch (EmptyResultDataAccessException e) {
		    	     GetLookupResponse badResp = new GetLookupResponse("9999", "RxNum_Not_Found", "999");
		    	     return badResp;

   	        } catch (Exception e) {
		    	     GetLookupResponse badResp = new GetLookupResponse("9999", "CDR_weblogic_lookup_service_unavailable", "999");
		    	     return badResp;
		    }	 
	   }
*/
	   
  
	   
	   
}



================================================================================================
                   0080. Create "CDR_LIB" Classpatch Variable.
================================================================================================
  <Windows>
   <Preferences>
      Java
      Build Path
      Classpatch Variable
      <New>
          Name  : CDR_LIB
          Patch : D:\oracle12
          OK, OK


------------------------------
Select Eclipse project
------------------------------
   <Project>
   <Properties>
   
   Java Build Path
   Select "Libraries"
   
   "Add Variable..."
   Select "CDR_LIB" variable
   OK

   Select "CDR_LIB" variable
   <Edit...>
   Edit classpath variable entry: CDR_LIB/ojdbc8-19.3.0.0.jar





================================================================================================
           0090. Adapt microservice to run in Weblogic application server
================================================================================================
https://o7planning.org/en/11901/deploying-spring-boot-application-on-oracle-weblogic-server
https://docs.spring.io/spring-boot/docs/current/reference/html/howto-traditional-deployment.html


Ensure that the embedded servlet container does not interfere with the servlet container to which the war file is deployed.
To do so, you need to mark the embedded servlet container dependency as being provided.
Marking the embedded servlet container dependency as provided produces an executable war file with 
the provided dependencies packaged in a lib-provided directory.
This means that, in addition to being deployable to a servlet container, 
you can also run your application by using java -jar on the command line.


-------------------------------------------------------------
1) Disable swagger and comment swagger in pom.xml.
-------------------------------------------------------------


---------------------------------------------------------------
2) Create "...\src\main\webapp\WEB-INF\weblogic.xml" :
---------------------------------------------------------------
If you use slf4j Logback, you also need to tell WebLogic to prefer the packaged version
rather than the version that was pre-installed with the server.
You can do so by adding "...\src\main\webapp\WEB-INF\weblogic.xml" file with the following contents:
Also have to update context-root = DispenseStatus
----------------------------------------------------------------
<?xml version="1.0" encoding="UTF-8"?>
<wls:weblogic-web-app xmlns:wls="http://xmlns.oracle.com/weblogic/weblogic-web-app" 
                      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
     xsi:schemaLocation="http://xmlns.oracle.com/weblogic/weblogic-web-app
                         http://xmlns.oracle.com/weblogic/weblogic-web-app/1.4/weblogic-web-app.xsd">

	<wls:context-root>DispenseStatus</wls:context-root>
	<wls:container-descriptor>
		<wls:prefer-web-inf-classes>false</wls:prefer-web-inf-classes>
		<wls:prefer-application-packages>
			<wls:package-name>com.fasterxml.*</wls:package-name>
			<wls:package-name>javax.annotation.security.*</wls:package-name>
			<wls:package-name>org.slf4j.*</wls:package-name>
			<wls:package-name>org.slf4j.impl.*</wls:package-name>
			<wls:package-name>org.springframework.*</wls:package-name>
			<wls:package-name>io.springfox.*</wls:package-name>
                        <wls:package-name>com.google.*</wls:package-name> 
			
			<!-- <wls:package-name>javax.annotation.*</wls:package-name>
			<wls:package-name>javax.annotation.sql.*</wls:package-name>
			<wls:package-name>javax.validation.*</wls:package-name>
			<wls:package-name>javax.validation.bootstrap.*</wls:package-name>
			<wls:package-name>javax.validation.constraints.*</wls:package-name>
			<wls:package-name>javax.validation.constraintvalidation.*</wls:package-name>
			<wls:package-name>javax.validation.executable.*</wls:package-name>
			<wls:package-name>javax.validation.groups.*</wls:package-name>
			<wls:package-name>javax.validation.metadata.*</wls:package-name>
			<wls:package-name>javax.validation.spi.*</wls:package-name>
			<wls:package-name>org.aopalliance.aop.*</wls:package-name>
			<wls:package-name>org.aopalliance.intercept.*</wls:package-name>
			<wls:package-name>org.apache.commons.*</wls:package-name>
			<wls:package-name>org.hibernate.validator.*</wls:package-name>
			<wls:package-name>org.jboss.logging.*</wls:package-name> -->
		</wls:prefer-application-packages>
	</wls:container-descriptor>

	<!-- <prefer-application-packages> <wls:package-name>com.fasterxml.classmate.*</wls:package-name> 
		<wls:package-name>javax.annotation.*</wls:package-name> <wls:package-name>javax.annotation.security.*</wls:package-name> 
		<wls:package-name>javax.annotation.sql.*</wls:package-name> <wls:package-name>javax.validation.*</wls:package-name> 
		<wls:package-name>javax.validation.bootstrap.*</wls:package-name> <wls:package-name>javax.validation.constraints.*</wls:package-name> 
		<wls:package-name>javax.validation.constraintvalidation.*</wls:package-name> 
		<wls:package-name>javax.validation.executable.*</wls:package-name> <wls:package-name>javax.validation.groups.*</wls:package-name> 
		<wls:package-name>javax.validation.metadata.*</wls:package-name> <wls:package-name>javax.validation.spi.*</wls:package-name> 
		<wls:package-name>org.aopalliance.aop.*</wls:package-name> <wls:package-name>org.aopalliance.intercept.*</wls:package-name> 
		<wls:package-name>org.apache.commons.*</wls:package-name> <wls:package-name>org.hibernate.validator.*</wls:package-name> 
		<wls:package-name>org.jboss.logging.*</wls:package-name> </prefer-application-packages> -->
</wls:weblogic-web-app>


-------------------------------------------------------------------------
3) Create "...\src\main\webapp\WEB-INF\dispatcherServlet-servlet.xml" :
-------------------------------------------------------------------------
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans 
	                    http://www.springframework.org/schema/beans/spring-beans.xsd">
</beans>



================================================================================================
                 0092. WebService project properties verification.
================================================================================================

<Project>
<Properties>
      ========================
      <Project Facets>
      ========================
      Dynamic Web Module.:4.0
      Java...............:1.8
      JavaScript.........:1.0


      ========================
      <Web Project Settings>
      ========================
      Context root.......:DispenseStatus


                          Context root
                          ------------
 http://10.108.76.43:3202/DispenseStatus/lkp/getRx
                                      -----------
                                      Controller.java



================================================================================================
                            0093. SQL on DIT
================================================================================================

SELECT T.STORENUM, 
       T.TXNNUM,
       v.cddescr 
  FROM TX     T,
       TXSTAT X,
       txfillstattyp V 
 WHERE T.TXKEY = X.TXKEY 
   AND x.txfillstattypkey = v.txfillstattypkey 
-- AND T.STORENUM = ?   AND T.TXNNUM = ? 
 fetch first 15 rows only;

STORENUM TXNNUM		CDDESCR
2807	12668395	Complete
1414	12667237	Waiting for pickup
7589	12624494	Cancelled
2807	12624493	Deferred
2807	12624492	Complete
2804	10922615	Complete
2807	12624308	Complete
2807	12624191	Complete
2807	12655118	Complete
2807	12651016	Complete
2807	12654017	Complete
2807	12654016	Complete
2807	12647434	Complete
2807	12624178	Complete
2807	12624184	Complete


http://localhost:8080/lkp/getDispenseStatus
http://10.108.76.43:3202/DispenseStatus/lkp/getRx 
--------------
POST Request
--------------
{
  "postLookupData": {
    "banner": "LCL",
    "rxNumber": "1024116",
    "storeNumber": "0199"
  }
}



------------
Response
------------
{"lookupResponse": {
   "storeNumber": "2826",
   "rxNumber": "9185931",
   "banner": "SDM"
}}



================================================================================================
                       0094. SQL on SIT
================================================================================================
select distinct
       k.lclstorenum,
       k.krollrxnum,

       k.hwstorenum    StoreNumber,
       k.hwrxnum       RxNumber,
       
       'SDM'           Banner

  from cdradmin.krolltohwmap k,
       cdradmin.rx           r

where k.hwrxnum = r.rxnum
  and k.hwstorenum = r.storenum
fetch first 20 rows only;


LCLSTORENUM	KROLLRXNUM	STORENUMBER	RXNUMBER	BANNER
1043		1005271		3153		9000020		SDM
1043		1005290		3153		9000038		SDM
1043		1005291		3153		9000039		SDM
1043		1005301		3153		9000048		SDM
1043		1005315		3153		9000058		SDM
1043		1005321		3153		9000064		SDM
1043		1005323		3153		9000066		SDM
1043		1005337		3153		9000078		SDM
1043		1005384		3153		9000117		SDM


--------------
POST Request
--------------
{
  "postLookupData": {
    "banner": "LCL",
    "rxNumber": "1005271",
    "storeNumber": "1043"
  }
}


-------------
Response
-------------
{
  "lookupResponse": {
    "banner": "SDM",
    "storeNumber": "9000020",
    "rxNumber": "3153"
  }
}



http://localhost:8080/swagger-ui.html
http://localhost:8080/lkp/getDispenseStatus

http://10.108.76.46:7001/console/login/LoginForm.jsp
     username: weblogic
     password: weblogic123

http://10.108.76.46:3202/DispenseStatus/swagger-ui.html
http://10.108.76.46:3202/DispenseStatus/lkp/getRx






================================================================================================
                           0095. How to build
================================================================================================
Disconnect from eVPN, then

Select "DispenseStatus" application, mouse right click, then
      Run As ---> 4 Maven build

      Goals..........: clean install
      Profiles.......:

      Make sure only one follwing check box have been checked:
                           - X Skip Tests
      <Apply>
      <Run>

Enter the following JVM system parameter in your Eclipse:
   -Dspring.profiles.active=local -Dspring.config.location=file:C:/wrk33/CrxRep/          -Dspring.datasource.jndi-name=jdbc/cdr
   -Dspring.profiles.active=dit   -Dspring.config.location=file:/app/weblogic/CDR/config/ -Dspring.datasource.jndi-name=jdbc/cdr



--------------------------------------------------------------
Check the following in log file once application is started:
--------------------------------------------------------------
2020-04-27 13:01:09,300 INFO org.springframework.boot.SpringApplication [main] The following profiles are active: local




-------
ERROR1:
-------
Failed to execute goal org.apache.maven.plugins:maven-surefire-plugin:2.22.2:test (default-test) on project CDRDrugSched2NCTM: Execution default-test of goal org.apache.maven.plugins:maven-surefire-plugin:2.22.2:test failed: Plugin org.apache.maven.plugins:maven-surefire-plugin:2.22.2 or one of its dependencies could not be resolved: The following artifacts could not be resolved: org.apache.maven.surefire:maven-surefire-common:jar:2.22.2, org.codehaus.plexus:plexus-utils:jar:1.1: Cannot access central (https://repo.maven.apache.org/maven2) in offline mode and the artifact org.apache.maven.surefire:maven-surefire-common:jar:2.22.2 has not been downloaded from it before. -> [Help 1]

--------------
WORKAROUND1
--------------
  - Disconnect from eVPN
  - Goals: clean install
  - Uncheck "Offline" check box to allow to download missing JAR(s)


-------
ERROR2:
-------
The system cannot find the following path specified in 	pom.xml	/CDRDrugSched2NCTM line 1
C:\sts-4.3.1.RELEASE\workspace2\CDRDrugSched2NCTM\target\m2e-wtp\web-resources\META-INF\MANIFEST.MF 


--------------
WORKAROUND2
--------------
Just re-run   Maven ---> Update Project...


scp /tmp/CDRDrugSched2NCTM-0.0.2-SNAPSHOT.war  vleidin@10.108.76.43:/app/weblogic/nctm


==================================================================================================
             0097. How to deploy in Weblogic application server
==================================================================================================
Have to select the following radio button while installing war file in Weblogic application server
----------------------------------------------------------------------------------------------------
x  Install this deployment as an application



Go To the "Deployments" and select newly deployed "DispenseStatus-20200319"
Select "Testing" TAB and see:
	http://192.168.0.10:7001/DispenseStatus
    http://localhost:7001/DispenseStatus/swagger-ui.html



-----------------------------
Deploying on Weblogic DIT
-----------------------------
ssh -l vleidin laheesbdv254.ngco.com  # passw: Luba_008  <Windows10 passw>
ssh -l vleidin 10.108.76.43           # passw: Luba_008  <Windows10 passw>

scp /tmp/DispenseStatus-20200323.war vleidin@10.108.76.43:/app/weblogic/CDR/deployment/Drop59/
cd /app/weblogic/CDR/deployment/Drop59/




================================================================================================
                          0099. How to run from Eclipse
================================================================================================


----------------------------------------------
http://localhost:8080/lkp/getDispenseStatus
----------------------------------------------
https://stackoverflow.com/questions/31134333/this-application-has-no-explicit-mapping-for-error

Whitelabel Error Page
This application has no explicit mapping for /error, so you are seeing this as a fallback.

Fri Dec 31 16:45:15 EST 2021
There was an unexpected error (type=Not Found, status=404).
No message available
----------------------------------------------

This page isn’t working If the problem continues, contact the site owner.
HTTP ERROR 405


http://localhost:8080/swagger-ui.html
----------
Request
----------
{
  "postLookupData": {
    "banner": "string",
    "storeNumber": "string",
    "txNumber": "string"
  }
}


----------
Response
----------
{
  "dispenseStatus": "string"
}


----------
Error1
----------
{
  "error": {
    "api": "string                          Example: PostLookupData",
    "code": "int                            Example: 400 or 500",
    "id": "string                           Example: 6df376d5-5215-4f7a-8cf9-0ae9640ef5a6",
    "message": "string",
    "source": "string                       Example: weblogic",
    "time": "EEE MMM dd hh:mm:ss yyyy       Example: Thu Apr 02 02:45:17 2020"
  }
}






================================================================================================
                  0100. How to run from local Weblogic
================================================================================================



--------------
POST Request
--------------
{
  "postLookupData": {
    "banner": "LCL",
    "rxNumber": "9431291",
    "storeNumber": "0199"
  }
}



-------------------------
From standAlone Swagger 
-------------------------
       http://localhost:8080/swagger-ui.html
       http://localhost:8080/lkp/getRx

curl -X POST "http://localhost:8080/lkp/getRx" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"postLookupData\": { \"banner\": \"LCL\", \"rxNumber\": \"9431291\", \"storeNumber\": \"0199\" }}"



-------------------------
From local Weblogic:
-------------------------
       http://localhost:7001/DispenseStatus/swagger-ui.html
       http://localhost:7001/DispenseStatus/lkp/getRx


curl -X POST "http://localhost:7001/DispenseStatus/lkp/getRx" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"postLookupData\": { \"banner\": \"LCL\", \"rxNumber\": \"9431291\", \"storeNumber\": \"0199\" }}"




========================================================================================================
                      0101. How to run from dit Weblogic
========================================================================================================
http://10.108.76.43:7001/console/login/LoginForm.jsp
     username: weblogic
     password: weblogic123


http://10.108.76.43:3202/DispenseStatus/swagger-ui.html
http://localhost:8080/v2/api-docs

http://10.108.76.43:3202/DispenseStatus/lkp/getRx
http://10.108.76.43:3203/DispenseStatus/lkp/getRx


Enter the following StartUp parameters in "Arguments" text box while starting Weblogic managed server:
   -Dspring.profiles.active=dit -Dspring.config.location=file:/app/weblogic/CDR/config/ -Dspring.datasource.jndi-name=jdbc/cdr



Enter the following StartUp parameters in "Arguments" text box while starting Dev_ManagedServer_2 managed server in DIT:
------------------------
Dev_ManagedServer_2
------------------------
-agentlib:am_ibm_16=/opt/IBM/ITM/aix533/yj/j2eedc/7.1.1.0.5/runtime/wls12.dev_domain.DEV_Machine_1.DEV_ManagedServer_3/dc.env.properties -Xbootclasspath/p:/opt/IBM/ITM/aix533/yj/j2eedc/7.1.1.0.5/toolkit/lib/bcm-bootstrap.jar:/opt/IBM/ITM/aix533/yj/j2eedc/7.1.1.0.5/itcamdc/lib/ppe.probe-bootstrap.jar:/opt/IBM/ITM/aix533/yj/j2eedc/7.1.1.0.5/itcamdc/lib/ext/wls/weblogicBcm.jar -DConfigDirectory=/app/weblogic/CDR/config -Xmx4096m -verbose:gc -Xverbosegclog:/opt/IBM/ITM/aix533/yj/j2eedc/7.1.1.0.5/runtime/wls12.dev_domain.DEV_Machine_1.DEV_ManagedServer_3/gc.log -Dcom.sun.management.jmxremote -Dsun.lang.ClassLoader.allowArraySyntax=true -Dcom.ibm.tivoli.jiti.injector.IProbeInjectorManager=com.ibm.tivoli.itcam.toolkit.ai.bcm.bootstrap.ProbeInjectorManager -DCCLOG_COMMON_DIR=/opt/IBM/ITM/aix533/yj/j2eedc/7.1.1.0.5/logs -Dweblogic.webservice.verbose=true -DCDR_MANAGED_SERVER=jdbc/cdr02 -javaagent:/app/weblogic/Oracle/Middleware/Oracle_Home/user_projects/domains/dev_domain/wlsdm_agent/wlsdm_agent.jar -Dwlsdm.agent.logger.level=INFO -XX:+UnlockCommercialFeatures -XX:+FlightRecorder -XX:FlightRecorderOptions=repository=WLSDM/temp/JFR -XX:+DisableExplicitGC  -Dspring.profiles.active=dit  -Dspring.config.location=file:/app/weblogic/CDR/config/  -Dspring.datasource.jndi-name=jdbc/cdr




Enter the following StartUp parameters in "Arguments" text box while starting Dev_ManagedServer_3 managed server in DIT:
------------------------
Dev_ManagedServer_3
------------------------
-agentlib:am_ibm_16=/opt/IBM/ITM/aix533/yj/j2eedc/7.1.1.0.5/runtime/wls12.dev_domain.DEV_Machine_1.DEV_ManagedServer_3/dc.env.properties -Xbootclasspath/p:/opt/IBM/ITM/aix533/yj/j2eedc/7.1.1.0.5/toolkit/lib/bcm-bootstrap.jar:/opt/IBM/ITM/aix533/yj/j2eedc/7.1.1.0.5/itcamdc/lib/ppe.probe-bootstrap.jar:/opt/IBM/ITM/aix533/yj/j2eedc/7.1.1.0.5/itcamdc/lib/ext/wls/weblogicBcm.jar -DConfigDirectory=/app/weblogic/CDR/config -Xmx4096m -verbose:gc -Xverbosegclog:/opt/IBM/ITM/aix533/yj/j2eedc/7.1.1.0.5/runtime/wls12.dev_domain.DEV_Machine_1.DEV_ManagedServer_3/gc.log -Dcom.sun.management.jmxremote -Dsun.lang.ClassLoader.allowArraySyntax=true -Dcom.ibm.tivoli.jiti.injector.IProbeInjectorManager=com.ibm.tivoli.itcam.toolkit.ai.bcm.bootstrap.ProbeInjectorManager -DCCLOG_COMMON_DIR=/opt/IBM/ITM/aix533/yj/j2eedc/7.1.1.0.5/logs -Dweblogic.webservice.verbose=true  -Dspring.profiles.active=dit -Dspring.config.location=file:/app/weblogic/CDR/config/ -Dspring.datasource.jndi-name=jdbc/cdr  -Xms1024m -Xmx1024m -DCDR_MANAGED_SERVER=jdbc/cdr03 -javaagent:/app/weblogic/Oracle/Middleware/Oracle_Home/user_projects/domains/dev_domain/wlsdm_agent/wlsdm_agent.jar -Dwlsdm.agent.logger.level=INFO -XX:+UnlockCommercialFeatures -XX:+FlightRecorder -XX:FlightRecorderOptions=repository=WLSDM/temp/JFR -XX:+DisableExplicitGC




/app/weblogic/CDR/deployment/Drop59/DispenseStatus-20200427_1345.war



???????????????????????????????????????????????????
-Dspring.profiles.active=local -Dspring.config.location=file:C:/wrk33/CrxRep/         -Dspring.datasource.jndi-name=jdbc/nctm02 

-Dspring.config.location="file:///Users/home/jdbc.properties"
file:/C:/wrk33/app.properties


T04.xml for Windows:
    <spring:property name="hwngdir" value="file:///${file.root}/rx_hwng/" />
    <spring:property name="privateCNFdir" value="file:///${file.root}/rx_hwng/private_cnf/" />


T04.xml for Linux:
    <spring:property name="hwngdir" value="file://${file.root}/rx_hwng/" />
    <spring:property name="privateCNFdir" value="file://${file.root}/rx_hwng/private_cnf/" />
???????????????????????????????????????????????????




==================================================================================================
                      0102. How to run from sit Weblogic
==================================================================================================
http://10.108.76.46:7001/console/login/LoginForm.jsp
     username: weblogic
     password: weblogic123

http://10.108.76.46:3202/DispenseStatus/swagger-ui.html
http://10.108.76.46:3202/DispenseStatus/lkp/getRx



laheesbst254:/app/weblogic12c/CDR/deployment/Drop59/DispenseStatus-20200427_1345.war

Enter the following StartUp parameters in "Arguments" text box while starting Weblogic managed server:
   -Dspring.profiles.active=sit -Dspring.config.location=file:/app/weblogic12c/CDR/config -Dspring.datasource.jndi-name=jdbc/cdr


Enter the following StartUp parameters in "Arguments" text box while starting SIT1_ManagedServer_1 managed server in SIT:
--------------------------------------
SIT1_ManagedServer_1   jdbc/cdr01
--------------------------------------
-DConfigDirectory=/app/weblogic12c/CDR/config -Xmx4096m -DCDR_MANAGED_SERVER=jdbc/cdr01 -Dspring.profiles.active=sit -Dspring.config.location=file:/app/weblogic12c/CDR/config -Dspring.datasource.jndi-name=jdbc/cdr01





Enter the following StartUp parameters in "Arguments" text box while starting SIT1_ManagedServer_2 managed server in SIT:
--------------------------------------
SIT1_ManagedServer_2   jdbc/cdr02
--------------------------------------
-DConfigDirectory=/app/weblogic12c/CDR/config -Xmx4096m -DCDR_MANAGED_SERVER=jdbc/cdr02 -Dspring.profiles.active=sit -Dspring.config.location=file:/app/weblogic12c/CDR/config -Dspring.datasource.jndi-name=jdbc/cdr02



!!! VERY IMPORTANT !!! Make sure you do not duplicate the following parameters which may be used by some other applications:
                       -Dspring.profiles.active
                       -Dspring.config.location
                       -Dspring.datasource.jndi-name



=============================================================================================================
                            0103. How to run from uat Weblogic
=============================================================================================================
   -Dspring.profiles.active=uat -Dspring.config.location=file:/app/weblogic/CDR/config/ -Dspring.datasource.jndi-name=jdbc/cdr





============================================================================================================
                           0104. How to run from pt  Weblogic
============================================================================================================
http://lakresbpt05ha:7001/console/login/LoginForm.jsp
     username: weblogic
     password: weblogic123

   

Enter the following StartUp parameters in "Arguments" text box while starting following two MS_3 and MS_4 managed servers in PreProd:
---------------------
MS_3   jdbc/cdr03
---------------------
-Dspring.profiles.active=pt -Dspring.config.location=file:/app/weblogic/CDR/config/ -Dspring.datasource.jndi-name=jdbc/cdr03



Enter the following StartUp parameters in "Arguments" text box while starting MS_4 managed server in PreProd:
---------------------
MS_4   jdbc/cdr04
---------------------
-Dspring.profiles.active=pt -Dspring.config.location=file:/app/weblogic/CDR/config/ -Dspring.datasource.jndi-name=jdbc/cdr04


!!! VERY IMPORTANT !!! Make sure you do not duplicate the following parameters which may be used by some other applications:
                       -Dspring.profiles.active
                       -Dspring.config.location
                       -Dspring.datasource.jndi-name



cp /u/vleidin/DispenseStatus-20200428_0900.war  /app/weblogic/CDR/deployment/Drop59




============================================================================================================
                       0105. How to run from prod Weblogic
============================================================================================================
--------------------
To disable swagger
--------------------
Just comment @EnableSwagger2 annotation as presented below:
. . . . . . 
@Configuration
// @EnableSwagger2
public class Swagger2Config {
. . . . . . 


--------------------------------------------------
http://laheesbpr05ha.ngco.com:7001/console
--------------------------------------------------
     username: nttuser1
     password: W3lc0m3!




Append the following StartUp parameters in "Arguments" text box for both PR_ManagedServer_3 and PR_ManagedServer_4
-----------------------------------
PR_ManagedServer_3   jdbc/cdr03
-----------------------------------
-Dspring.profiles.active=prod  -Dspring.config.location=file:/app/weblogic/CDR/config/  -Dspring.datasource.jndi-name=jdbc/cdr03



-----------------------------------
PR_ManagedServer_4   jdbc/cdr04
-----------------------------------
-Dspring.profiles.active=prod  -Dspring.config.location=file:/app/weblogic/CDR/config/  -Dspring.datasource.jndi-name=jdbc/cdr04




!!! VERY IMPORTANT !!! Make sure you do not duplicate the following parameters which may be used by some other applications:
                       -Dspring.profiles.active
                       -Dspring.config.location
                       -Dspring.datasource.jndi-name



cp /u/vleidin/DispenseStatus-20200428_0900.war  /app/weblogic/CDR/deployment/Drop59





====================================================================================================
             0110. How to print all beans used by this application
====================================================================================================
https://spring.io/guides/gs/spring-boot/


package com.sdm.lookup;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DispenseStatusApplication {
	public static void main(String[] args) {
		SpringApplication.run(DispenseStatusApplication.class, args);
	}

	
	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			System.out.println("This application uses following beans:");
			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for (String beanName : beanNames) {
				 System.out.println(beanName);
			}
		};
	}
}



This application uses following beans:


_relProvider
alpsController
alpsConverter
alpsJsonHttpMessageConverter
annotatedEventHandlerInvoker
annotationRelProvider
applicationTaskExecutor
associationLinks
auditableBeanWrapperFactory
backendIdConverterRegistry
backendIdHandlerMethodArgumentResolver
baseUri
basicErrorController
beanNameHandlerMapping
beanNameViewResolver
businessLogicImpl
DispenseStatusApplication
characterEncodingFilter
commandLineRunner
controller
conventionErrorViewResolver
dataSource
dataSourceInitializerPostProcessor
defaultConversionService
defaultMessageConverters
defaultRelProvider
defaultServletHandlerMapping
defaultValidator
defaultViewResolver
delegatingEntityLinks
dispatcherServlet
dispatcherServletRegistration
eTagArgumentResolver
entityLinks
entityLinksPluginRegistry
enumTranslator
error
errorAttributes
errorPageCustomizer
errorPageRegistrarBeanPostProcessor
excerptProjector
formContentFilter
halJacksonHttpMessageConverter
halLinkDisocoverer
halMessageConverterSupportedMediaTypeCustomizer
handlerExceptionResolver
handlerFunctionAdapter
hikariPoolDataSourceMetadataProvider
httpHeadersPreparer
httpRequestHandlerAdapter
hypermediaRepresentionModelProcessorConfigurator
hypermediaRestTemplateBeanPostProcessor
hypermediaWebMvcConfigurer
hypermediaWebMvcConverters
jacksonGeoModule
jacksonHttpMessageConverter
jacksonObjectMapper
jacksonObjectMapperBuilder
jdbcTemplate
jpaHelper
jsonComponentModule
jsonSchemaConverter
linkCollector
linkDiscovererRegistry
linkDiscoverers
localeCharsetMappingsCustomizer
mappingJackson2HttpMessageConverter
mbeanExporter
mbeanServer
messageConverters
messageResolver
metadataConfiguration
methodValidationPostProcessor
multipartConfigElement
multipartResolver
mvcContentNegotiationManager
mvcConversionService
mvcHandlerMappingIntrospector
mvcPathMatcher
mvcResourceUrlProvider
mvcUriComponentsContributor
mvcUrlPathHelper
mvcValidator
mvcViewResolver
namedParameterJdbcTemplate
objectNamingStrategy
org.springframework.aop.config.internalAutoProxyCreator
org.springframework.boot.autoconfigure.AutoConfigurationPackages
org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration
org.springframework.boot.autoconfigure.aop.AopAutoConfiguration
org.springframework.boot.autoconfigure.aop.AopAutoConfiguration$ClassProxyingConfiguration
org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration
org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration
org.springframework.boot.autoconfigure.dao.PersistenceExceptionTranslationAutoConfiguration
org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration
org.springframework.boot.autoconfigure.hateoas.HypermediaAutoConfiguration
org.springframework.boot.autoconfigure.hateoas.HypermediaHttpMessageConverterConfiguration
org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration
org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration$StringHttpMessageConverterConfiguration
org.springframework.boot.autoconfigure.http.JacksonHttpMessageConvertersConfiguration
org.springframework.boot.autoconfigure.http.JacksonHttpMessageConvertersConfiguration$MappingJackson2HttpMessageConverterConfiguration
org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration
org.springframework.boot.autoconfigure.internalCachingMetadataReaderFactory
org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration$Jackson2ObjectMapperBuilderCustomizerConfiguration
org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration$JacksonObjectMapperBuilderConfiguration
org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration$JacksonObjectMapperConfiguration
org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration$ParameterNamesModuleConfiguration
org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration$PooledDataSourceConfiguration
org.springframework.boot.autoconfigure.jdbc.DataSourceConfiguration$Hikari
org.springframework.boot.autoconfigure.jdbc.DataSourceInitializationConfiguration
org.springframework.boot.autoconfigure.jdbc.DataSourceInitializerInvoker
org.springframework.boot.autoconfigure.jdbc.DataSourceJmxConfiguration
org.springframework.boot.autoconfigure.jdbc.DataSourceJmxConfiguration$Hikari
org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration$DataSourceTransactionManagerConfiguration
org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration
org.springframework.boot.autoconfigure.jdbc.JdbcTemplateConfiguration
org.springframework.boot.autoconfigure.jdbc.NamedParameterJdbcTemplateConfiguration
org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadataProvidersConfiguration
org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadataProvidersConfiguration$HikariPoolDataSourceMetadataProviderConfiguration
org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration
org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration
org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration
org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration
org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration
org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration$EnableTransactionManagementConfiguration
org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration$EnableTransactionManagementConfiguration$CglibAutoProxyConfiguration
org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration$TransactionTemplateConfiguration
org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration
org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration
org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration
org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration$TomcatWebServerFactoryCustomizerConfiguration
org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration
org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration$DispatcherServletConfiguration
org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration$DispatcherServletRegistrationConfiguration
org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration
org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration
org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration
org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryConfiguration$EmbeddedTomcat
org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration$EnableWebMvcConfiguration
org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration$WebMvcAutoConfigurationAdapter
org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration$DefaultErrorViewResolverConfiguration
org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration$WhitelabelErrorViewConfiguration
org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration
org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration$TomcatWebSocketConfiguration
org.springframework.boot.context.internalConfigurationPropertiesBinder
org.springframework.boot.context.internalConfigurationPropertiesBinderFactory
org.springframework.boot.context.properties.ConfigurationBeanFactoryMetadata
org.springframework.boot.context.properties.ConfigurationPropertiesBeanDefinitionValidator
org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor
org.springframework.context.annotation.internalAutowiredAnnotationProcessor
org.springframework.context.annotation.internalCommonAnnotationProcessor
org.springframework.context.annotation.internalConfigurationAnnotationProcessor
org.springframework.context.event.internalEventListenerFactory
org.springframework.context.event.internalEventListenerProcessor
org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration
org.springframework.data.web.config.SpringDataJacksonConfiguration
org.springframework.hateoas.config.HateoasConfiguration
org.springframework.hateoas.config.RestTemplateHateoasConfiguration
org.springframework.hateoas.config.WebMvcEntityLinksConfiguration
org.springframework.hateoas.config.WebMvcHateoasConfiguration
org.springframework.hateoas.mediatype.hal.HalMediaTypeConfiguration
org.springframework.transaction.annotation.ProxyTransactionManagementConfiguration
org.springframework.transaction.config.internalTransactionAdvisor
org.springframework.transaction.config.internalTransactionalEventListenerFactory
pageableResolver
pagedResourcesAssembler
pagedResourcesAssemblerArgumentResolver
parameterNamesModule
persistenceExceptionTranslationPostProcessor
persistentEntities
persistentEntityArgumentResolver
platformTransactionManagerCustomizers
preserveErrorControllerTargetClassPostProcessor
profileController
profileResourceProcessor
projectionDefinitionRegistrar
propertySourcesPlaceholderConfigurer
relProviderPluginRegistry
repoRequestArgumentResolver
repositories
repositoryController
repositoryEntityController
repositoryExporterHandlerAdapter
repositoryInvokerFactory
repositoryPropertyReferenceController
repositoryRelProvider
repositoryRestConfiguration
repositoryRestExceptionHandler
repositorySchemaController
repositorySearchController
representationModelProcessorInvoker
requestContextFilter
requestMappingHandlerAdapter
requestMappingHandlerMapping
resourceHandlerMapping
resourceMappings
resourceMetadataHandlerMethodArgumentResolver
restExceptionHandler
restHandlerMapping
restTemplateBuilder
routerFunctionMapping
selfLinkProvider
server-org.springframework.boot.autoconfigure.web.ServerProperties
serverHttpRequestMethodArgumentResolver
servletWebServerFactoryCustomizer
simpleControllerHandlerAdapter
sortResolver
spring.data.rest-org.springframework.boot.autoconfigure.data.rest.RepositoryRestProperties
spring.datasource-org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
spring.hateoas-org.springframework.boot.autoconfigure.hateoas.HateoasProperties
spring.http-org.springframework.boot.autoconfigure.http.HttpProperties
spring.info-org.springframework.boot.autoconfigure.info.ProjectInfoProperties
spring.jackson-org.springframework.boot.autoconfigure.jackson.JacksonProperties
spring.jdbc-org.springframework.boot.autoconfigure.jdbc.JdbcProperties
spring.mvc-org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties
spring.resources-org.springframework.boot.autoconfigure.web.ResourceProperties
spring.security.oauth2.resourceserver-org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties
spring.servlet.multipart-org.springframework.boot.autoconfigure.web.servlet.MultipartProperties
spring.task.execution-org.springframework.boot.autoconfigure.task.TaskExecutionProperties
spring.task.scheduling-org.springframework.boot.autoconfigure.task.TaskSchedulingProperties
spring.transaction-org.springframework.boot.autoconfigure.transaction.TransactionProperties
springApplicationAdminRegistrar
springBootRepositoryRestConfigurer
standardJacksonObjectMapperBuilderCustomizer
stringHttpMessageConverter
taskExecutorBuilder
taskSchedulerBuilder
tomcatServletWebServerFactory
tomcatServletWebServerFactoryCustomizer
tomcatWebServerFactoryCustomizer
transactionAttributeSource
transactionInterceptor
transactionManager
transactionTemplate
uriListHttpMessageConverter
validatingRepositoryEventListener
viewControllerHandlerMapping
viewResolver
webMvcEntityLinks
webMvcLinkBuilderFactory
webServerFactoryCustomizerBeanPostProcessor
websocketServletWebServerCustomizer
welcomePageHandlerMapping





??????????????????????????????????????????????????????
??????????????????????????????????????????????????????
??????????????????????????????????????????????????????

2020-04-01 16:57:18,220 INFO  [http-nio-8080-exec-3] org.springframework.web.servlet.FrameworkServlet: Initializing Servlet 'dispatcherServlet'
2020-04-01 16:57:18,225 INFO  [http-nio-8080-exec-3] org.springframework.web.servlet.FrameworkServlet: Completed initialization in 5 ms
2020-04-01 16:57:31,473 INFO  [http-nio-8080-exec-7] com.sdm.lookup.controller.Controller: Controller: getRxRecord
2020-04-01 16:57:31,473 INFO  [http-nio-8080-exec-7] com.sdm.lookup.service.BusinessLogicImpl: Searching in CDR for :  in CDR DB for StoreNum = string, RxNum=string, Banner=string

ResponseGetRx{Patient not found= in CDR DB for StoreNum: string, RxNum: string, Banner: string}
ResponseGetRx{Patient not found= in CDR DB for StoreNum: string, RxNum: string, Banner: string}
ResponseGetRx{Patient not found= in CDR DB for StoreNum: string, RxNum: string, Banner: string}
ResponseGetRx{Patient not found= in CDR DB for StoreNum: string, RxNum: string, Banner: string}
ResponseGetRx{Patient not found= in CDR DB for StoreNum: string, RxNum: string, Banner: string}

2020-04-01 16:57:31,484 ERROR [http-nio-8080-exec-7] com.sdm.lookup.exceptionhandling.RestExceptionHandler: Runtime exception occured 
com.sdm.lookup.exceptionhandling.ErrorSelector: ResponseGetRx{Patient not found= in CDR DB for StoreNum = string, RxNum=string, Banner=string}
	at com.sdm.lookup.service.BusinessLogicImpl.getRxRecord(BusinessLogicImpl.java:46)
	at com.sdm.lookup.controller.Controller.getRxRecord(Controller.java:48)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:190)
	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:138)