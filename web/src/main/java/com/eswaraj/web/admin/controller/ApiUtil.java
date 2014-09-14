package com.eswaraj.web.admin.controller;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.web.controller.beans.ComplaintBean;
import com.eswaraj.web.dto.RegisterFacebookAccountWebRequest;
import com.eswaraj.web.dto.UserDto;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

@Component
public class ApiUtil {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;
    private HttpClientBuilder httpClientBuilder;

    @Value("${api_host}")
    private String apiHost;
    @Value("${eswaraj_facebook_app_id}")
    private String facebookAppId;

    private Gson gson = new Gson();

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

    public List<ComplaintBean> getLocationComplaints(HttpServletRequest httpServletRequest, Long locationId) throws ApplicationException {
        String urlPath = "/api/v0/complaint/location/" + locationId;
        Map<String, String> addedParams = getPagingInfo(httpServletRequest);
        String locationComplaints = getResponseFrom(httpServletRequest, urlPath, addedParams);
        List<ComplaintBean> list = gson.fromJson(locationComplaints, new TypeToken<List<ComplaintBean>>() {}.getType());
        return list;
    }

    public List<ComplaintBean> getLocationCategoryComplaints(HttpServletRequest httpServletRequest, Long locationId, Long categoryId) throws ApplicationException {
        String urlPath = "/api/v0/complaint/location/" + locationId + "/" + categoryId;
        Map<String, String> addedParams = getPagingInfo(httpServletRequest);
        String locationComplaints = getResponseFrom(httpServletRequest, urlPath, addedParams);
        List<ComplaintBean> list = gson.fromJson(locationComplaints, new TypeToken<List<ComplaintBean>>() {}.getType());
        return list;
    }

    public List<ComplaintBean> getLocationComplaints(HttpServletRequest httpServletRequest, Long locationId, Long pageSize) throws ApplicationException {
        String urlPath = "/api/v0/complaint/location/" + locationId;
        Map<String, String> addedParams = new HashMap<>();
        addedParams.put("start", "0");
        addedParams.put("end", String.valueOf(pageSize));
        String locationComplaints = getResponseFrom(httpServletRequest, urlPath, addedParams);
        List<ComplaintBean> list = gson.fromJson(locationComplaints, new TypeToken<List<ComplaintBean>>() {}.getType());
        return list;
    }

    public List<ComplaintBean> getLocationCategoryComplaints(HttpServletRequest httpServletRequest, Long locationId, Long categoryId, Long pageSize) throws ApplicationException {
        String urlPath = "/api/v0/complaint/location/" + locationId + "/" + categoryId;
        Map<String, String> addedParams = new HashMap<>();
        addedParams.put("start", "0");
        addedParams.put("end", String.valueOf(pageSize));
        String locationComplaints = getResponseFrom(httpServletRequest, urlPath, addedParams);
        List<ComplaintBean> list = gson.fromJson(locationComplaints, new TypeToken<List<ComplaintBean>>() {}.getType());
        return list;
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

    public UserDto saveFacebookUser(HttpServletRequest httpServletRequest, Connection<Facebook> facebookConnection) throws ApplicationException {
        String urlPath = "/api/v0/web/user/facebook";
        RegisterFacebookAccountWebRequest registerFacebookAccountWebRequest = new RegisterFacebookAccountWebRequest();
        ConnectionData facebookConnectionData = facebookConnection.createData();
        if(facebookConnectionData.getExpireTime() != null){
            registerFacebookAccountWebRequest.setExpireTime(new Date(facebookConnectionData.getExpireTime()));    
        }
        registerFacebookAccountWebRequest.setFacebookAppId(facebookAppId);
        registerFacebookAccountWebRequest.setToken(facebookConnectionData.getAccessToken());
        String requestPayload = gson.toJson(registerFacebookAccountWebRequest);
        String response = postRequest(httpServletRequest, urlPath, requestPayload);
        return gson.fromJson(response, UserDto.class);
    }

    public String postRequest(HttpServletRequest httpServletRequest, String urlPath, String postData) throws ApplicationException {
        try {
            logger.info("Posting Request to {}", urlPath);
            URIBuilder uriBuilder = new URIBuilder().setScheme("http").setHost(apiHost).setPath(urlPath);
            Map<String, String[]> parameters = httpServletRequest.getParameterMap();
            for (Entry<String, String[]> oneParameterEntry : parameters.entrySet()) {
                for (String oneValue : oneParameterEntry.getValue()) {
                    uriBuilder.addParameter(oneParameterEntry.getKey(), oneValue);
                }
            }
            // TODO add here params and authentication paramaters

            URI uri = uriBuilder.build();
            HttpPost httppost = new HttpPost(uri);

            StringEntity httpEntity = new StringEntity(postData);
            httpEntity.setContentType("application/json");
            httppost.setEntity(httpEntity);

            logger.info("Posting request {} to {}", postData, httppost.getURI());
            HttpResponse httpResponse = getHttpClient().execute(httppost);
            String response = EntityUtils.toString(httpResponse.getEntity());
            logger.info("Response = {}", response);
            return response;
        } catch (Exception ex) {
            throw new ApplicationException(ex);
        }
    }

    public String getResponseFrom(HttpServletRequest httpServletRequest, String urlPath, Map<String, String> addedParameters) throws ApplicationException {
        try {
            logger.info("Getting Results from " + urlPath);
            URIBuilder uriBuilder = new URIBuilder().setScheme("http").setHost(apiHost).setPath(urlPath);
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
