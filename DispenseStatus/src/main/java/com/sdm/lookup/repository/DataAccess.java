package com.sdm.lookup.repository;

import com.sdm.lookup.entity.postLookupData;
import com.sdm.lookup.entity.GetLookupResponse;

public interface DataAccess {
	
	   public GetLookupResponse getSDMstoreData(postLookupData req);
}
