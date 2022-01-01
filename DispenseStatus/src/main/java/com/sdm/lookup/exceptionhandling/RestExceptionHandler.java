package com.sdm.lookup.exceptionhandling;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.io.IOError;
import java.sql.SQLRecoverableException;
import java.sql.SQLTransientConnectionException;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
/* @Slf4j */
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger logger = LogManager.getLogger(RestExceptionHandler.class);

	/**
	 * Handle MissingServletRequestParameterException. Triggered when a 'required'
	 * request parameter is missing.
	 *
	 * @param ex      MissingServletRequestParameterException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String error = ex.getParameterName() + " parameter is missing";
		logger.error("Exception occured ", ex);
		return buildResponseEntity(new error(BAD_REQUEST, error, ex));
	}

	/**
	 * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is
	 * invalid as well.
	 *
	 * @param ex      HttpMediaTypeNotSupportedException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		StringBuilder builder = new StringBuilder();
		builder.append(ex.getContentType());
		builder.append(" media type is not supported. Supported media types are ");
		ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
		logger.error("Exception occured ", ex);
		return buildResponseEntity(
				new error(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), ex));
	}

	/**
	 * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid
	 * validation.
	 *
	 * @param ex      the MethodArgumentNotValidException that is thrown when @Valid
	 *                validation fails
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		error apiError = new error(BAD_REQUEST);
		apiError.setMessage("Validation error");
		apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
		apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
		logger.error("Exception occured ", ex);
		return buildResponseEntity(apiError);
	}

	/**
	 * Handles javax.validation.ConstraintViolationException. Thrown when @Validated
	 * fails.
	 *
	 * @param ex the ConstraintViolationException
	 * @return the ApiError object
	 */
	@ExceptionHandler(javax.validation.ConstraintViolationException.class)
	protected ResponseEntity<Object> handleConstraintViolation(javax.validation.ConstraintViolationException ex) {
		error apiError = new error(BAD_REQUEST);
		apiError.setMessage("Validation error");
		apiError.addValidationErrors(ex.getConstraintViolations());
		logger.error("Exception occured ", ex);
		return buildResponseEntity(apiError);
	}

	@ExceptionHandler({ RuntimeException.class })
	public ResponseEntity<Object> handleRunTimeException(RuntimeException ex) {
		logger.error("Runtime exception occured ", ex);
		error apiError=null;
		if(ex.getCause() instanceof SQLRecoverableException || ex.getCause() instanceof  java.net.SocketException || ex.getCause() instanceof SQLTransientConnectionException) {
			 apiError = new error(org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE);
				apiError.setMessage("Network  error occured :" + ex.getMessage());
		}		
		else {
			 apiError = new error(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
				apiError.setMessage("Internal server error occured :" + ex.getMessage());
		}
		return buildResponseEntity(apiError);
	}

	
	
	
/*	
	@ExceptionHandler({ PTNTNotFoundException.class })
	public ResponseEntity<Object> handleCPFMException(PTNTNotFoundException ex) {
		   logger.error("Runtime exception occured ", ex);
		   error apiError = new error(org.springframework.http.HttpStatus.BAD_REQUEST);
		   apiError.setMessage("PTNTNotFoundException occured. " + ex.getMessage());
		   return buildResponseEntity(apiError);
	}
*/
	
	@ExceptionHandler({ ErrorSelector.class })
	public ResponseEntity<Object> handleException(ErrorSelector ex) {
		   error apiError = null;
		   String msg = ex.getMessage().replace("=", "");
		   msg = StringUtils.substringBetween(msg, "{", "}");
//		   System.out.println(msg); 

		   if (StringUtils.startsWith(msg, "RxNum not found"))  {
			   apiError = new error(org.springframework.http.HttpStatus.BAD_REQUEST);           // HTTP_RESPONSE_CODE = 400
		   } else if (StringUtils.startsWith(msg, "Incorrect Banner")) {
			   apiError = new error(org.springframework.http.HttpStatus.BAD_REQUEST);           // HTTP_RESPONSE_CODE = 400
		   } else if (StringUtils.startsWith(msg, "Incorrect RxNum")) {
			   apiError = new error(org.springframework.http.HttpStatus.BAD_REQUEST);           // HTTP_RESPONSE_CODE = 400
		   } else if (StringUtils.startsWith(msg, "Incorrect StoreNum")) {
			   apiError = new error(org.springframework.http.HttpStatus.BAD_REQUEST);           // HTTP_RESPONSE_CODE = 400
		   } else {
			   apiError = new error(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR); // HTTP_RESPONSE_CODE = 500
		   }
		   
		   logger.error("Runtime exception occured ", ex);
		   apiError.setMessage("" + msg);
		   return buildResponseEntity(apiError);
	}
	
	
	
	
	@ExceptionHandler({Exception.class })
	public ResponseEntity<Object> handleException(Exception ex) {
		logger.error("Exception occured ", ex);
		error apiError=null;
		if(ex instanceof SQLRecoverableException) {
			 apiError = new error(org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE);
				apiError.setMessage("Network  error occured :" + ex.getMessage());
		}else {
			 apiError = new error(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
				apiError.setMessage("Internal server error occured :" + ex.getMessage());
		}
				
		
		return buildResponseEntity(apiError);
	}

	/**
	 * Handles EntityNotFoundException. Created to encapsulate errors with more
	 * detail than javax.persistence.EntityNotFoundException.
	 *
	 * @param ex the EntityNotFoundException
	 * @return the ApiError object
	 */

	/*
	 * One thing to keep in mind here is to match the exceptions declared
	 * with @ExceptionHandler with the exception used as the argument of the method.
	 * If these don’t match, the compiler will not complain – no reason it should,
	 * and Spring will not complain either.
	 */

	@ExceptionHandler(EntityNotFoundException.class)
	protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
		logger.error("EntityNotFoundException exception occured ", ex);
		error apiError = new error(HttpStatus.OK);
		apiError.setMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}
	
	/*@ExceptionHandler(IllegalArgumentException.class)
	protected ResponseEntity<Object> handleEntityNotFound(IllegalArgumentException ex) {
		logger.error("IllegalArgumentException exception occured ", ex);
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR);
		apiError.setMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}
	
	@ExceptionHandler(NullPointerException.class)
	protected ResponseEntity<Object> handleEntityNotFound(NullPointerException ex) {
		logger.error("Null Pointer exception occured ", ex);
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR);
		apiError.setMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}*/

	@ExceptionHandler(SQLRecoverableException.class)
	protected ResponseEntity<Object> handleDataBaseNetworkError(SQLRecoverableException ex){
		logger.error("Network Error Occured ", ex);
		error apiError = new error(HttpStatus.SERVICE_UNAVAILABLE);
		apiError.setMessage(ex.getMessage());
		return buildResponseEntity(apiError);
		
		
	}
	
	@ExceptionHandler(IOError.class)
	protected ResponseEntity<Object> handleDataBaseNetworkError(IOError ex){
		logger.error("Network Error Occured ", ex);
		error apiError = new error(HttpStatus.SERVICE_UNAVAILABLE);
		apiError.setMessage(ex.getMessage());
		return buildResponseEntity(apiError);
		
		
	}
	/**
	 * Handle HttpMessageNotReadableException. Happens when request JSON is
	 * malformed.
	 *
	 * @param ex      HttpMessageNotReadableException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ServletWebRequest servletWebRequest = (ServletWebRequest) request;
		logger.error("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());
		String error = "Malformed JSON request";
		logger.error("Exception occured ", ex);
		return buildResponseEntity(new error(HttpStatus.BAD_REQUEST, error, ex));
	}

	/**
	 * Handle HttpMessageNotWritableException.
	 *
	 * @param ex      HttpMessageNotWritableException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String error = "Error writing JSON output";
		logger.error("Exception occured ", ex);
		return buildResponseEntity(new error(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
	}

	/**
	 * Handle NoHandlerFoundException.
	 *
	 * @param ex
	 * @param headers
	 * @param status
	 * @param request
	 * @return
	 */
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		error apiError = new error(BAD_REQUEST);
		logger.error("Exception occured ", ex);
		apiError.setMessage(
				String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));
		apiError.setDebugMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}

	
	 

	/**
	 * Handle DataIntegrityViolationException, inspects the cause for different DB
	 * causes.
	 *
	 * @param ex the DataIntegrityViolationException
	 * @return the ApiError object
	 */
	/*
	 * @ExceptionHandler(DataIntegrityViolationException.class) protected
	 * ResponseEntity<Object>
	 * handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest
	 * request) { if (ex.getCause() instanceof ConstraintViolationException) {
	 * return buildResponseEntity(new ApiError(HttpStatus.CONFLICT,
	 * "Database error", ex.getCause())); } return buildResponseEntity(new
	 * ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex)); }
	 */

	/**
	 * Handle Exception, handle generic Exception.class
	 *
	 * @param ex the Exception
	 * @return the ApiError object
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			WebRequest request) {
		error apiError = new error(BAD_REQUEST);
		apiError.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
				ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
		apiError.setDebugMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}

	private ResponseEntity<Object> buildResponseEntity(error apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

}
