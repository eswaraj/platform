package com.next.eswaraj.web.login.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.validator.exception.ValidationException;
import com.eswaraj.web.dto.ErrorMessageDto;
import com.eswaraj.web.dto.UserDto;
import com.next.eswaraj.web.session.SessionUtil;

public class BaseController {


	protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${static_content_host}")
    private String staticContentHost;

    @Autowired
    protected SessionUtil sessionUtil;

	@ExceptionHandler(ApplicationException.class)
	@ResponseBody
	public ErrorMessageDto applicationError(
			ApplicationException applicationException) {
		logger.error("Application Exception : ", applicationException);
		ErrorMessageDto errorMessageDto = new ErrorMessageDto();
		errorMessageDto.setMessage(applicationException.getMessage());
		return errorMessageDto;
	}

	@ExceptionHandler(ValidationException.class)
	@ResponseBody
	public ErrorMessageDto validationError(
			ValidationException validationException) {
		logger.error("Validation Exception : ", validationException);
		ErrorMessageDto errorMessageDto = new ErrorMessageDto();
		errorMessageDto.setMessage(validationException.getMessage());
		return errorMessageDto;
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ErrorMessageDto AnyOtherError(Exception exception) {
		logger.error("Internal Exception : ", exception);
		ErrorMessageDto errorMessageDto = new ErrorMessageDto();
		errorMessageDto.setMessage(exception.getMessage());
		return errorMessageDto;
	}

    protected void addGenericValues(ModelAndView mv, HttpServletRequest httpServletRequest) {
        mv.getModel().put("staticHost", staticContentHost);
        UserDto loggeInUser = sessionUtil.getLoggedInUserFromSession(httpServletRequest);
        mv.getModel().put("user", loggeInUser);
        if (loggeInUser == null) {
            mv.getModel().put("loggedIn", false);
        } else {
            mv.getModel().put("loggedIn", true);
        }
        mv.getModel().put("currentUrl", getCurrentUrl(httpServletRequest));
    }

    private String getCurrentUrl(HttpServletRequest httpServletRequest){
        return httpServletRequest.getRequestURI() + "?" + httpServletRequest.getQueryString();
    }

	protected String getFileName(String submittedFileName) {
		return UUID.randomUUID().toString()
				+ submittedFileName.substring(submittedFileName
						.lastIndexOf("."));
	}



}
