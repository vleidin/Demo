package com.sdm.lookup.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sdm.lookup.entity.GetLookupResponse;
import com.sdm.lookup.entity.postLookupData;



@Repository
public class DataAccessImpl implements DataAccess {
	private static final Logger logger = LogManager.getLogger(DataAccessImpl.class);
	   private static String TxStatusSQL = "SELECT v.cddescr FROM TX T,TXSTAT X,txfillstattyp V WHERE T.TXKEY = X.TXKEY AND x.txfillstattypkey = v.txfillstattypkey AND T.STORENUM = ?   AND T.TXNNUM = ? ";
	   
	   
	   @Autowired
	   private JdbcTemplate jdbcTemplate;


	   
	   @Override
	   public GetLookupResponse getSDMstoreData(postLookupData req) {
		    try {
		    	logger.info("get dispense status ");
		        return jdbcTemplate.queryForObject(TxStatusSQL, new Object[] {req.getStoreNumber(),
		        		                                                   req.getTxNumber()
		        		                                                  },  (rs, rowNum) ->
		                new GetLookupResponse (
		                                       rs.getString("cddescr"),false
		                                       )
		                );
		    } catch (EmptyResultDataAccessException e) {
		    	logger.error("get dispense status "+e);
		    	     GetLookupResponse badResp = new GetLookupResponse("TxNum not found", true);
		    	     return badResp;

   	        } catch (Exception e) {
   	        	logger.error("get dispense status "+e);
		    	     GetLookupResponse badResp = new GetLookupResponse("CDR weblogic lookup service unavailable",true);
		    	     return badResp;
		    }	 
	   }

	   
  
	   
	   
}