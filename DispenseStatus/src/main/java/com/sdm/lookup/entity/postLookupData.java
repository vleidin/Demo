package com.sdm.lookup.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.validator.constraints.Length;

public class postLookupData {

	   private String StoreNumber;
	   private String TxNumber;
	   private String Banner;

	   
   	   public String getStoreNumber() {
   		   	  return StoreNumber;
	   }
	   public void setStoreNumber(String storeNumber) {
		      StoreNumber = storeNumber;
	   }

	   
	   public String getTxNumber() {
		      return TxNumber;
	   }
	   public void setTxNumber(String txNumber) {
		      TxNumber = txNumber;
	   }

	   
	   public String getBanner() {
		      return Banner;
	   }
	   public void setBanner(String banner) {
		      Banner = banner;
	   }
	   
	   @Override
	   public String toString() {
		      return " for StoreNum: " + StoreNumber + ", TxNum: " + TxNumber + ", Banner: " + Banner;
	   }
	   
}
