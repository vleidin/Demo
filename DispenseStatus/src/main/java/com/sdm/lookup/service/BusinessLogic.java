package com.sdm.lookup.service;

import com.sdm.lookup.entity.ResponseGetDispenseStatus;
import com.sdm.lookup.entity.postLookupData;


public interface BusinessLogic {

	   public ResponseGetDispenseStatus getDispenseStatus(postLookupData req);
}
