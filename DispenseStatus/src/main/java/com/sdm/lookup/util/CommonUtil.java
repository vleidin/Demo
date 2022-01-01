package com.sdm.lookup.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.EnumSet;
import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;


public class CommonUtil implements Serializable {
		private static final long serialVersionUID = 8346064357167951507L;
		private static final Logger logger = LogManager.getLogger(CommonUtil.class);
		
		public static String formatMessage(String message, String args ) {
	 	       return String.format(message, args);
	 	}
		
		
		
		public static String readFile(String fileName) {
		       Resource resource;
		    
		       byte[] byteStream = null;
	           String stringStream = null;
	        
		       resource = new ClassPathResource(fileName);
		       try {
		            InputStream inputStream = resource.getInputStream();
		            byteStream = FileCopyUtils.copyToByteArray(inputStream);
		            stringStream = new String(byteStream, StandardCharsets.UTF_8);
		        
//		            System.out.println("1 File content: " + stringStream);
//		            logger.info("2 File content: " + stringStream);
		       } catch (IOException e) {
		    	    logger.error("IOException", e);
		       }
		       return stringStream;
	    }
		
		
		public static <E extends Enum<E>> boolean FoundInEnum(Class<E> enumClass,  String searchValue) {
			   try {
			        return EnumSet.allOf(enumClass).contains(Enum.valueOf(enumClass, searchValue));
			   } catch (Exception e) {
			        return false;
			   }
	    }
}
