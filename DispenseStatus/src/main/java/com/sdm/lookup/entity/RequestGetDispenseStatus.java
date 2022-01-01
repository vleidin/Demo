package com.sdm.lookup.entity;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.validator.constraints.Length;


public class RequestGetDispenseStatus implements Serializable {
	   private static final long serialVersionUID = 8923300901346463502L;
	   
	   private postLookupData pstLkpData;
	   
	   
	   public postLookupData getpostLookupData() {
			  return pstLkpData;
	   }
	   
	   public void setpostLookupData(postLookupData pstLkpDt) {
		      pstLkpData = pstLkpDt;
	   }
}
