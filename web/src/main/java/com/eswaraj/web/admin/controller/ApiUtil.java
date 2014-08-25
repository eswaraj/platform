package com.eswaraj.web.admin.controller;

import java.net.URI;

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
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;

@Component
public class ApiUtil {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;
    private HttpClientBuilder httpClientBuilder;

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

    public String getResponseFrom(String urlPath) throws ApplicationException {
        try {
            logger.info("Getting Results from " + urlPath);
            URI uri = new URIBuilder().setScheme("http").setHost("dev.api.eswaraj.com").setPath(urlPath).build();
            HttpGet httpget = new HttpGet(uri);
            logger.info("Getting Results from " + httpget.getURI());
            HttpResponse httpResponse = getHttpClient().execute(httpget);
            return EntityUtils.toString(httpResponse.getEntity());
        } catch (Exception ex) {
            throw new ApplicationException(ex);
        }
    }
}
