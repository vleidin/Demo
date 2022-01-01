package com.sdm.lookup.entity;


import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/*
 * This class is used in "Controller.java" only to represent
 * swagger error response model 
 */
public class ErrorTemplate {
       private error err;
	   public error getError() {return err;}
}

@JsonPropertyOrder({ "code", "message", "api", "id", "source", "time" })
class error {
	  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="int                            Example: 400 or 500")
	  private String code;
	  
	  private String message;

	  
	  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="string                          Example: PostLookupData")
	  private String api;

	  
	  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="string                           Example: 6df376d5-5215-4f7a-8cf9-0ae9640ef5a6")
	  private String id;
	  
	  
	  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="string                       Example: weblogic")
	  private String source;
	  
	  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "EEE MMM dd hh:mm:ss yyyy       Example: Thu Apr 02 02:45:17 2020")
	  private LocalDateTime time;
	   
	  
	  
	  public String getCode() {return code;}
	  public String getMessage() {return message;}
	  public String getApi() {return api;}
	  public String getId() {return id;}
	  public String getSource() {return source;}

	  
	  @JsonIgnore
	  public LocalDateTime getTimestamp() {
		     return time;
	  }
	  
	  
}