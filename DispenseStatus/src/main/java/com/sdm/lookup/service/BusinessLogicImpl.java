package com.sdm.lookup.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdm.lookup.entity.CDREnumerations.Banner;
import com.sdm.lookup.entity.GetLookupResponse;
import com.sdm.lookup.entity.ResponseGetDispenseStatus;
import com.sdm.lookup.entity.postLookupData;
import com.sdm.lookup.exceptionhandling.ErrorSelector;
import com.sdm.lookup.repository.DataAccess;
import com.sdm.lookup.util.CommonUtil;


@Service
public class BusinessLogicImpl implements BusinessLogic {
	private static final Logger logger = LogManager.getLogger(BusinessLogicImpl.class);
	   
	   @Autowired
	   DataAccess da;
		
	   @Override
	   public ResponseGetDispenseStatus getDispenseStatus(postLookupData req) throws ErrorSelector {
			  String msg = CommonUtil.formatMessage("Searching : %s", req.toString());
			  String errMsg;
			  logger.info(msg);
			  
			  CheckInpPayload(req);
			  GetLookupResponse tmpResp = null;
			  ResponseGetDispenseStatus response = new ResponseGetDispenseStatus();
    	      tmpResp = da.getSDMstoreData(req);

    	      if (!tmpResp.isError()) {
    	    	     response.setDispenseStatus(tmpResp.getDispenseStatus());
    	      } else {
    	    	     errMsg = tmpResp.getDispenseStatus();		  
			         throw new ErrorSelector(ResponseGetDispenseStatus.class, errMsg, req.toString());
    	      }
			  return response;	
	   }

	   
	   private void CheckInpPayload(postLookupData req) throws ErrorSelector {
		       boolean badPayload = false;
		       String errMsg = null;
		       String banner = req.getBanner() != null ? req.getBanner() : null;
		       
		       if ( ! CommonUtil.FoundInEnum(Banner.class, banner)) {
		    	      errMsg = "Incorrect Banner in the request payload ";
		    	      badPayload = true;
		 	   }
		       
		       if ( ! (StringUtils.isNumeric(req.getTxNumber())) ||
		    		  (req.getTxNumber().length() < 4)  ) {
		    	      errMsg = "Incorrect TxNum in the request payload ";
		    	      badPayload = true;
	           }
		       
		       if ( ! (StringUtils.isNumeric(req.getStoreNumber())) ||
		    		  (req.getStoreNumber().length() < 4)  ) {
		    	      errMsg = "Incorrect StoreNum in the request payload ";
		    	      badPayload = true;
	           }

		       
		       if (badPayload) {
	 		      logger.error(errMsg + req.toString());
	 	  	      throw new ErrorSelector(ResponseGetDispenseStatus.class, errMsg, req.toString());
		       }
	   }
}
