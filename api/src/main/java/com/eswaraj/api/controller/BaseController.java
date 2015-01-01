package com.eswaraj.api.controller;

import java.util.Collection;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppKeyService;
import com.eswaraj.web.dto.ErrorMessageDto;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class BaseController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    protected AppKeyService appKeyService;


    protected JsonArray convertToJsonArray(Collection<String> values) {
        JsonArray jsonArray = new JsonArray();
        JsonParser jsonParser = new JsonParser();
        for (String oneResult : values) {
            if (oneResult == null) {
                continue;
            }
            jsonArray.add(jsonParser.parse(oneResult));
        }
        return jsonArray;
    }

    protected int getIntParameter(HttpServletRequest httpServletRequest, String parameter, int defaultValue) {
        String paramValue = httpServletRequest.getParameter(parameter);
        if (paramValue == null) {
            return defaultValue;
        }
        return Integer.parseInt(paramValue);
    }

    protected long getLongParameter(HttpServletRequest httpServletRequest, String parameter, long defaultValue) {
        String paramValue = httpServletRequest.getParameter(parameter);
        if (paramValue == null) {
            return defaultValue;
        }
        return Long.parseLong(paramValue);
    }

    protected boolean getBooleanParameter(HttpServletRequest httpServletRequest, String parameter, boolean defaultValue) {
        String paramValue = httpServletRequest.getParameter(parameter);
        if (paramValue == null) {
            return defaultValue;
        }
        boolean returnValue = defaultValue;
        try {
            returnValue = Boolean.parseBoolean(paramValue);
        } catch (Exception ex) {
            logger.warn("Unable to parser {} as boolean", paramValue);
        }
        return returnValue;
    }

    protected Double getDoubleParameter(HttpServletRequest httpServletRequest, String parameter, Double defaultValue) {
        String paramValue = httpServletRequest.getParameter(parameter);
        if (paramValue == null) {
            return defaultValue;
        }
        Double returnValue = defaultValue;
        try {
            returnValue = Double.parseDouble(paramValue);
        } catch (Exception ex) {
            logger.warn("Unable to parser {} as Double", paramValue);
        }
        return returnValue;
    }

    protected String getFileName(String submittedFileName) {
        return UUID.randomUUID().toString() + submittedFileName.substring(submittedFileName.lastIndexOf("."));
    }

    @ExceptionHandler
    @ResponseBody
    public ErrorMessageDto onApplicationException(ApplicationException applicationException) {
        logger.error("Application Exception : ", applicationException);
        ErrorMessageDto errorMessageDto = new ErrorMessageDto();
        errorMessageDto.setMessage(applicationException.getMessage());
        return errorMessageDto;
    }

    @ExceptionHandler
    @ResponseBody
    public ErrorMessageDto onException(Exception applicationException) {
        logger.error("Application Exception : ", applicationException);
        ErrorMessageDto errorMessageDto = new ErrorMessageDto();
        errorMessageDto.setMessage(applicationException.getMessage());
        return errorMessageDto;
    }

}
