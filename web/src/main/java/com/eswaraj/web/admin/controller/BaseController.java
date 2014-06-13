package com.eswaraj.web.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.validator.exception.ValidationException;
import com.eswaraj.web.dto.ErrorMessageDto;

public class BaseController {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	 @ExceptionHandler(ApplicationException.class)
	 @ResponseBody
	  public ErrorMessageDto applicationError(ApplicationException applicationException) {
		 logger.error("Application Exception : ", applicationException);
		 ErrorMessageDto errorMessageDto = new ErrorMessageDto();
		 errorMessageDto.setMessage(applicationException.getMessage());
	    return errorMessageDto;
	  }
	 @ExceptionHandler(ValidationException.class)
	 @ResponseBody
	  public ErrorMessageDto validationError(ValidationException validationException) {
		 logger.error("Validation Exception : ", validationException);
		 ErrorMessageDto errorMessageDto = new ErrorMessageDto();
		 errorMessageDto.setMessage(validationException.getMessage());
	    return errorMessageDto;
	  }
}
