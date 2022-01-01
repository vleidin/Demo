package com.sdm.lookup.entity;

import java.io.Serializable;


public class ResponseGetDispenseStatus implements Serializable {
	   private static final long serialVersionUID = -6113608274237831437L;
	
	   private String dispenseStatus;
	   

	
	public String getDispenseStatus() {
		return dispenseStatus;
	}

	public void setDispenseStatus(String dispenseStatus) {
		this.dispenseStatus = dispenseStatus;
	}
	   
	   

}
