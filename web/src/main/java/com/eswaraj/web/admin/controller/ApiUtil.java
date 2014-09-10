package com.eswaraj.web.admin.controller;

import java.net.URI;
import java.util.HashMap;
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
        Map<String, String> addedParams = getPagingInfo(httpServletRequest);
        return getResponseFrom(httpServletRequest, urlPath, addedParams);
    }

    public String getLocationCategoryComplaints(HttpServletRequest httpServletRequest, Long locationId, Long categoryId) throws ApplicationException {
        String urlPath = "/api/v0/complaint/location/" + locationId + "/" + categoryId;
        Map<String, String> addedParams = getPagingInfo(httpServletRequest);
        return getResponseFrom(httpServletRequest, urlPath, addedParams);
    }

    public String getLocationComplaints(HttpServletRequest httpServletRequest, Long locationId, Long pageSize) throws ApplicationException {
        String urlPath = "/api/v0/complaint/location/" + locationId;
        Map<String, String> addedParams = new HashMap<>();
        addedParams.put("start", "0");
        addedParams.put("end", String.valueOf(pageSize));
        return getResponseFrom(httpServletRequest, urlPath, addedParams);
    }

    public String getLocationCategoryComplaints(HttpServletRequest httpServletRequest, Long locationId, Long categoryId, Long pageSize) throws ApplicationException {
        String urlPath = "/api/v0/complaint/location/" + locationId + "/" + categoryId;
        Map<String, String> addedParams = new HashMap<>();
        addedParams.put("start", "0");
        addedParams.put("end", String.valueOf(pageSize));
        return getResponseFrom(httpServletRequest, urlPath, addedParams);
    }

    private Map<String, String> getPagingInfo(HttpServletRequest httpServletRequest) {
        String currentPage = httpServletRequest.getParameter("page");
        Long start = 0L;
        Long end = 10L;
        if (currentPage != null) {
            Long page = Long.parseLong(currentPage);
            start = (page - 1) * 10;
            end = start + end;
        }
        Map<String, String> addedParams = new HashMap<>();
        addedParams.put("start", String.valueOf(start));
        addedParams.put("end", String.valueOf(end));
        return addedParams;
    }
    public String getAllCategopries(HttpServletRequest httpServletRequest) throws ApplicationException {
        return getAllCategopries(httpServletRequest, false);
    }
    public String getAllCategopries(HttpServletRequest httpServletRequest, boolean getGlobalCount) throws ApplicationException {
        Map<String, String> extraParams = new HashMap<String, String>();
        if (getGlobalCount) {
            extraParams.put("counter", "global");
        }
        String urlPath = "/api/v0/categories";
        return getResponseFrom(httpServletRequest, urlPath, extraParams);
    }

    public String getAllCategopries(HttpServletRequest httpServletRequest, Long locationId, boolean getGlobalCount) throws ApplicationException {
        Map<String, String> extraParams = new HashMap<String, String>();
        if (getGlobalCount) {
            extraParams.put("counter", "global");
        }
        extraParams.put("locationId", String.valueOf(locationId));
        String urlPath = "/api/v0/categories";
        return getResponseFrom(httpServletRequest, urlPath, extraParams);
    }

    public String getLocation(HttpServletRequest httpServletRequest, Long locationId) throws ApplicationException {
        String urlPath = "/api/v0/location/" + locationId + "/info";
        return getResponseFrom(httpServletRequest, urlPath);
    }
    public String getResponseFrom(HttpServletRequest httpServletRequest, String urlPath) throws ApplicationException {
        return getResponseFrom(httpServletRequest, urlPath, null);
    }

    public String getResponseFrom(HttpServletRequest httpServletRequest, String urlPath, Map<String, String> addedParameters) throws ApplicationException {
        try {
            logger.info("Getting Results from " + urlPath);
            URIBuilder uriBuilder = new URIBuilder().setScheme("http").setHost("dev.api.eswaraj.com").setPath(urlPath);
            Map<String, String[]> parameters = httpServletRequest.getParameterMap();
            for(Entry<String, String[]> oneParameterEntry : parameters.entrySet()){
                for (String oneValue : oneParameterEntry.getValue()) {
                    uriBuilder.addParameter(oneParameterEntry.getKey(), oneValue);
                }
            }
            if (addedParameters != null) {
                for (Entry<String, String> oneParameterEntry : addedParameters.entrySet()) {
                    uriBuilder.addParameter(oneParameterEntry.getKey(), oneParameterEntry.getValue());
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
