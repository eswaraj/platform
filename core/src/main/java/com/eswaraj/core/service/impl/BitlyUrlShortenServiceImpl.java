package com.eswaraj.core.service.impl;

import java.io.IOException;

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

    @Override
    public String getShortUrl(String longUrl) throws ApplicationException {
        try {
            String shortenServiceUrl = bitLyUrl + "v3/shorten?access_token=" + bitlyAccessToekn + "&longUrl=" + longUrl + "&domain=j.mp&format=json";
            String jsonResponse = httpUtil.getResponse(shortenServiceUrl);
            JsonObject jsonObject = jsonParser.parse(jsonResponse).getAsJsonObject();
            return jsonObject.get("data").getAsJsonObject().get("expand").getAsJsonArray().get(0).getAsJsonObject().get("short_url").getAsString();
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

}
