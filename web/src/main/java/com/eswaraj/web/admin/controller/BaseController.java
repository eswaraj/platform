package com.eswaraj.web.admin.controller;

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
import com.eswaraj.core.service.AppService;
import com.eswaraj.domain.validator.exception.ValidationException;
import com.eswaraj.web.dto.ErrorMessageDto;
import com.google.gdata.util.common.base.StringUtil;

public class BaseController {

	@Autowired
	protected AppService appService;
	
    protected static final String REDIRECT_URL_PARAM_ID = "redirect_url";

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${static_content_host}")
    private String staticContentHost;

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

    protected void addGenericValues(ModelAndView mv) {
        mv.getModel().put("staticHost", staticContentHost);
    }

	protected String getFileName(String submittedFileName) {
		return UUID.randomUUID().toString()
				+ submittedFileName.substring(submittedFileName
						.lastIndexOf("."));
	}

    protected String getRedirectUrlForRedirectionAfterLogin(HttpServletRequest httpServletRequest) {
        String redirectUrlAfterLogin = getRedirectUrl(httpServletRequest);
        logger.info("redirectUrlAfterLogin from param = " + redirectUrlAfterLogin);
        if (StringUtil.isEmpty(redirectUrlAfterLogin)) {
            redirectUrlAfterLogin = httpServletRequest.getContextPath() + "/index.html";
            logger.info("redirectUrlAfterLogin default = " + redirectUrlAfterLogin);
        }
        return redirectUrlAfterLogin;
    }

    protected String getRedirectUrl(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getParameter(REDIRECT_URL_PARAM_ID);
    }

    protected void setRedirectUrlInSessiom(HttpServletRequest httpServletRequest, String redirectUrl) {
        httpServletRequest.getSession(true).setAttribute(REDIRECT_URL_PARAM_ID, redirectUrl);
    }

    protected String getAndRemoveRedirectUrlFromSession(HttpServletRequest httpServletRequest) {
        String redirectUrl = (String) httpServletRequest.getSession().getAttribute(REDIRECT_URL_PARAM_ID);
        if (StringUtil.isEmpty(redirectUrl)) {
            redirectUrl = httpServletRequest.getContextPath() + "/index.html";
        } else {
            httpServletRequest.getSession().removeAttribute(REDIRECT_URL_PARAM_ID);
        }

        return redirectUrl;
    }

}
