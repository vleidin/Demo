package com.sdm.lookup.entity;

public class GetLookupResponse {
 	   
	   private String dispenseStatus;
	   private boolean isError;

	   public GetLookupResponse(String dispenseStat, boolean isErr) {
		   dispenseStatus = dispenseStat;
		   isError = isErr;
	   }

	public String getDispenseStatus() {
		return dispenseStatus;
	}

	public void setDispenseStatus(String dispenseStatus) {
		this.dispenseStatus = dispenseStatus;
	}

	public boolean isError() {
		return isError;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}
	   
   	  

}
