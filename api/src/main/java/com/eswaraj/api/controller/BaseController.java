package com.eswaraj.api.controller;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.eswaraj.core.service.AppKeyService;
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

}
