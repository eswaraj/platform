package com.eswaraj.api.controller;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.eswaraj.core.service.LocationKeyService;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class BaseController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    protected LocationKeyService locationKeyService;

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

}
