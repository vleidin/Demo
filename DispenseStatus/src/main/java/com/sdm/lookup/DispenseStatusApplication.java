package com.sdm.lookup;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Arrays;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;


@SpringBootApplication
//@PropertySource("file:/app/${weblogic.server}/CDR/config/DispenseStatus/application.properties")
public class DispenseStatusApplication {
	private static final Logger logger = LogManager.getLogger(DispenseStatusApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(DispenseStatusApplication.class, args);
	}
	
	@Bean
    CommandLineRunner printVersion(ApplicationContext ctx) {
	        return args -> {
	            System.out.println("DispenseStatus_20211231_0900.zip Started");
	            logger.warn("DispenseStatus_20211231_0900.zip Started");

/*	            
	            System.out.println("This application uses following beans:");
				String[] beanNames = ctx.getBeanDefinitionNames();
				Arrays.sort(beanNames);
				for (String beanName : beanNames) {
					 System.out.println(beanName);
				}
*/				
	        };
	}
}
