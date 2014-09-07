package com.eswaraj.web.admin.controller;

import java.net.URI;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;

@Component
public class ApiUtil {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;
    private HttpClientBuilder httpClientBuilder;

    @Value("api_host")
    private String apiHost;

    public ApiUtil() {
        poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        // Increase max total connection to 200
        poolingHttpClientConnectionManager.setMaxTotal(200);
        // Increase default max connection per route to 20
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(200);

        httpClientBuilder = HttpClients.custom().setConnectionManager(poolingHttpClientConnectionManager);

    }

    private HttpClient getHttpClient() {
        return httpClientBuilder.build();
    }

    public String getLocationComplaints(HttpServletRequest httpServletRequest, Long locationId) throws ApplicationException {
        String urlPath = "/api/v0/complaint/location/" + locationId;
        return getResponseFrom(httpServletRequest, urlPath);
    }

    public String getAllCategopries(HttpServletRequest httpServletRequest) throws ApplicationException {
        String urlPath = "/api/v0/categories";
        return getResponseFrom(httpServletRequest, urlPath);
    }
    public String getResponseFrom(HttpServletRequest httpServletRequest, String urlPath) throws ApplicationException {
        try {
            logger.info("Getting Results from " + urlPath);
            URIBuilder uriBuilder = new URIBuilder().setScheme("http").setHost("dev.api.eswaraj.com").setPath(urlPath);
            Map<String, String[]> parameters = httpServletRequest.getParameterMap();
            for(Entry<String, String[]> oneParameterEntry : parameters.entrySet()){
                for (String oneValue : oneParameterEntry.getValue()) {
                    uriBuilder.addParameter(oneParameterEntry.getKey(), oneValue);
                }
            }
            // TODO add hear params and authentication paramaters
            
            URI uri = uriBuilder.build();
            HttpGet httpget = new HttpGet(uri);
            
            logger.info("Getting Results from " + httpget.getURI());
            HttpResponse httpResponse = getHttpClient().execute(httpget);
            return EntityUtils.toString(httpResponse.getEntity());
        } catch (Exception ex) {
            throw new ApplicationException(ex);
        }
    }

}
