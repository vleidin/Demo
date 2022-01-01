package com.sdm.lookup.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

// import org.springframework.beans.factory.annotation.Qualifier;
// @Qualifier("BusinessLogic")

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdm.lookup.entity.ErrorTemplate;
import com.sdm.lookup.entity.RequestGetDispenseStatus;
import com.sdm.lookup.entity.ResponseGetDispenseStatus;
import com.sdm.lookup.service.BusinessLogic;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
@RequestMapping("/lkp")
public class Controller {
	private static final Logger logger = LogManager.getLogger(Controller.class);

//	@Value("${app.archive}")
//	private String archiveDir;
	
	@Autowired
	private BusinessLogic businessLogic;

	@PostMapping(path="/getDispenseStatus")
	@ApiResponses(value = {
                            @ApiResponse(code = 200, message = "Success", response = ResponseGetDispenseStatus.class),
                            @ApiResponse(code = 400, message = "Dispense Number not found", response = ErrorTemplate.class),
                            @ApiResponse(code = 500, message = "CDR Dispense Status service unavailable", response = ErrorTemplate.class)
                          }
	             )
	public ResponseEntity<ResponseGetDispenseStatus> getDispenseStatus(@RequestBody RequestGetDispenseStatus request) {
		   logger.info("Start getDispenseStatus call");
		   ResponseGetDispenseStatus response = businessLogic.getDispenseStatus(request.getpostLookupData());
		   logger.info("End getDispenseStatus call");
		   return new ResponseEntity<>(response, HttpStatus.OK);  // HTTP_RESPONSE_CODE = 200
	}
	
	
	

}
