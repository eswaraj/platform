package com.eswaraj.core.service.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.UrlShortenService;
import com.eswaraj.core.util.HttpUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class BitlyUrlShortenServiceImpl implements UrlShortenService {

    @Value("${bitly_access_token}")
    private String bitlyAccessToekn;

    @Autowired
    private HttpUtil httpUtil;

    private JsonParser jsonParser = new JsonParser();

    private String bitLyUrl = "https://api-ssl.bitly.com/";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String getShortUrl(String longUrl) throws ApplicationException {
        try {
            String shortenServiceUrl = bitLyUrl + "v3/shorten?access_token=" + bitlyAccessToekn + "&longUrl=" + longUrl + "&domain=j.mp&format=json";
            String jsonResponse = httpUtil.getResponse(shortenServiceUrl);
            logger.info("Response : " + jsonResponse);
            JsonObject jsonObject = jsonParser.parse(jsonResponse).getAsJsonObject();
            String shortUrl = jsonObject.get("data").getAsJsonObject().get("url").getAsString();; 
            logger.info("Short Url : "+shortUrl);
            return shortUrl;
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

}
