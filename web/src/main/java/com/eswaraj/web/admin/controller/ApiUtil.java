package com.eswaraj.web.admin.controller;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
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
import com.eswaraj.web.dto.ComplaintStatusChangeByPoliticalAdminRequestDto;
import com.eswaraj.web.dto.ComplaintViewdByPoliticalAdminRequestDto;
import com.eswaraj.web.dto.PoliticalPositionDto;
import com.eswaraj.web.dto.RegisterFacebookAccountWebRequest;
import com.eswaraj.web.dto.SavePoliticalAdminStaffRequestDto;
import com.eswaraj.web.dto.UpdateUserRequestWebDto;
import com.eswaraj.web.dto.UserDto;
import com.eswaraj.web.dto.comment.CommentSaveRequestDto;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Component
public class ApiUtil {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;
    private HttpClientBuilder httpClientBuilder;
	

    @Value("${api_host}")
    private String apiHost;
    @Value("${eswaraj_facebook_app_id}")
    private String facebookAppId;

    JsonDeserializer<Date> deser = new JsonDeserializer<Date>() {
        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return json == null ? null : new Date(json.getAsLong());
        }
    };
    JsonSerializer<Date> ser = new JsonSerializer<Date>() {

        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            return src == null ? null : new JsonPrimitive(src.getTime());
        }
    };

    private Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, ser).registerTypeAdapter(Date.class, deser).create();

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
        addedParams.put("count", String.valueOf(pageSize));
        String locationComplaints = getResponseFrom(httpServletRequest, urlPath, addedParams);
        List<ComplaintBean> list = gson.fromJson(locationComplaints, new TypeToken<List<ComplaintBean>>() {}.getType());
        return list;
    }

    public List<ComplaintBean> getLocationCategoryComplaints(HttpServletRequest httpServletRequest, Long locationId, Long categoryId, Long pageSize) throws ApplicationException {
        String urlPath = "/api/v0/complaint/location/" + locationId + "/" + categoryId;
        Map<String, String> addedParams = new HashMap<>();
        addedParams.put("start", "0");
        addedParams.put("count", String.valueOf(pageSize));
        String locationComplaints = getResponseFrom(httpServletRequest, urlPath, addedParams);
        List<ComplaintBean> list = gson.fromJson(locationComplaints, new TypeToken<List<ComplaintBean>>() {}.getType());
        return list;
    }

    public List<PoliticalPositionDto> getPersonPoliticalPositions(HttpServletRequest httpServletRequest, Long personId, boolean activeOnly) throws ApplicationException {
        String locationComplaints = getPersonPoliticalPositionsString(httpServletRequest, personId, activeOnly);
        List<PoliticalPositionDto> list = gson.fromJson(locationComplaints, new TypeToken<List<PoliticalPositionDto>>() {
        }.getType());
        return list;
    }

    public String getPersonPoliticalPositionsString(HttpServletRequest httpServletRequest, Long personId, boolean activeOnly) throws ApplicationException {
        String urlPath = "/api/v0/person/politicalpositions/" + personId;
        Map<String, String> addedParams = new HashMap<>();
        addedParams.put("active_only", String.valueOf(activeOnly));
        String locationComplaints = getResponseFrom(httpServletRequest, urlPath, addedParams);
        return locationComplaints;
    }

    public String getLocationCountersFor365Days(HttpServletRequest httpServletRequest, Long locationId) throws ApplicationException {
        String urlPath = "/api/v0/location/" + locationId+"/complaintcounts/last365";
        String locationComplaints = getResponseFrom(httpServletRequest, urlPath);
        return locationComplaints;
    }

    public String getLocationAnalytics(HttpServletRequest httpServletRequest, Long locationId, Long pageSize) throws ApplicationException {
        String urlPath = "/api/v0/complaint/location/" + locationId;
        Map<String, String> addedParams = new HashMap<>();
        addedParams.put("start", "0");
        addedParams.put("count", String.valueOf(pageSize));
        String locationAnalytics = getResponseFrom(httpServletRequest, urlPath, addedParams);
        return locationAnalytics;
    }

    public String getLocationCastegoryAnalytics(HttpServletRequest httpServletRequest, Long locationId, Long categoryId, Long pageSize) throws ApplicationException {
        String urlPath = "/api/v0/complaint/location/" + locationId + "/" + categoryId;
        Map<String, String> addedParams = new HashMap<>();
        addedParams.put("start", "0");
        addedParams.put("count", String.valueOf(pageSize));
        String locationCategoryAnalytics = getResponseFrom(httpServletRequest, urlPath, addedParams);
        return locationCategoryAnalytics;
    }

    public String getPoliticalAdminStaff(HttpServletRequest httpServletRequest, Long politicalAdminId) throws ApplicationException {
        String urlPath = "/api/v0/leader/staff/" + politicalAdminId;
        String locationCategoryAnalytics = getResponseFrom(httpServletRequest, urlPath);
        return locationCategoryAnalytics;
    }

    public String getPoliticalAdminComplaints(HttpServletRequest httpServletRequest, Long politicalAdminId) throws ApplicationException {
        String urlPath = "/api/v0/complaint/politicaladmin/" + politicalAdminId;
        Map<String, String> params = getPagingInfo(httpServletRequest);
        String complaints = getResponseFrom(httpServletRequest, urlPath, params);
        return complaints;
    }

    public String updateComplaintViewStatus(HttpServletRequest httpServletRequest, ComplaintViewdByPoliticalAdminRequestDto complaintViewdByPoliticalAdminRequestDto) throws ApplicationException {
        String urlPath = "/api/v0/complaint/politicaladmin/view";
        String complaints = postRequest(httpServletRequest, urlPath, gson.toJson(complaintViewdByPoliticalAdminRequestDto));
        return complaints;
    }

    public String updateComplaintStatus(HttpServletRequest httpServletRequest, ComplaintStatusChangeByPoliticalAdminRequestDto complaintStatusChangeByPoliticalAdminRequestDto)
            throws ApplicationException {
        String urlPath = "/api/v0/complaint/politicaladmin/status";
        String complaints = postRequest(httpServletRequest, urlPath, gson.toJson(complaintStatusChangeByPoliticalAdminRequestDto));
        return complaints;
    }

    public String mergeComplaints(HttpServletRequest httpServletRequest, List<Long> complaintIds) throws ApplicationException {
        String urlPath = "/api/v0/complaint/politicaladmin/merge";
        String complaints = postRequest(httpServletRequest, urlPath, gson.toJson(complaintIds));
        return complaints;
    }

    public String commentOn(HttpServletRequest httpServletRequest, CommentSaveRequestDto commentRequestDto) throws ApplicationException {
        String urlPath = "/api/v0/complaint/politicaladmin/comment";
        String complaints = postRequest(httpServletRequest, urlPath, gson.toJson(commentRequestDto));
        return complaints;
    }

    public String deletePoliticalAdminStaff(HttpServletRequest httpServletRequest, Long politicalAdminStaffId) throws ApplicationException {
        String urlPath = "/api/v0/leader/staff/" + politicalAdminStaffId;
        String locationCategoryAnalytics = delete(httpServletRequest, urlPath);
        return locationCategoryAnalytics;
    }

    public String savePoliticalAdminStaff(HttpServletRequest httpServletRequest, SavePoliticalAdminStaffRequestDto savePoliticalAdminStaffRequestDto) throws ApplicationException {
        String urlPath = "/api/v0/leader/staff";
        String saveResponse = postRequest(httpServletRequest, urlPath, gson.toJson(savePoliticalAdminStaffRequestDto));
        return saveResponse;
    }
    
    private Map<String, String> getPagingInfo(HttpServletRequest httpServletRequest) {
        String currentPage = httpServletRequest.getParameter("page");
        String itemCount = httpServletRequest.getParameter("count");
        Long start = 0L;
        Long count = 10L;
        if (itemCount != null) {
            count = Long.parseLong(itemCount);
        }
        if (currentPage != null) {
            Long page = Long.parseLong(currentPage);
            start = (page - 1) * count;
        }

        Map<String, String> addedParams = new HashMap<>();
        addedParams.put("start", String.valueOf(start));
        addedParams.put("count", String.valueOf(count));
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
        if (locationId != null) {
            extraParams.put("locationId", String.valueOf(locationId));
        }
        String urlPath = "/api/v0/categories";
        return getResponseFrom(httpServletRequest, urlPath, extraParams);
    }

    public String getLeaderInfo(HttpServletRequest httpServletRequest, String urlKey) throws ApplicationException {
        Map<String, String> extraParams = new HashMap<String, String>();
        extraParams.put("urlkey", urlKey);
        String urlPath = "/api/v0/leader";
        return getResponseFrom(httpServletRequest, urlPath, extraParams);
    }

    public String getLocation(HttpServletRequest httpServletRequest, Long locationId) throws ApplicationException {
        String urlPath = "/api/v0/location/" + locationId + "/info";
        return getResponseFrom(httpServletRequest, urlPath);
    }

    public String getLocationLeaders(HttpServletRequest httpServletRequest, Long locationId) throws ApplicationException {
        String urlPath = "/api/v0/location/" + locationId + "/leaders";
        return getResponseFrom(httpServletRequest, urlPath);
    }
    public String getResponseFrom(HttpServletRequest httpServletRequest, String urlPath) throws ApplicationException {
        return getResponseFrom(httpServletRequest, urlPath, null);
    }

    public String searchPersonByEmail(HttpServletRequest httpServletRequest) throws ApplicationException {
        String urlPath = "/api/v0/person/search/email";
        return getResponseFrom(httpServletRequest, urlPath);
    }

    public String searchPersonByName(HttpServletRequest httpServletRequest) throws ApplicationException {
        String urlPath = "/api/v0/person/search/name";
        return getResponseFrom(httpServletRequest, urlPath);
    }


    public UserDto saveFacebookUser(HttpServletRequest httpServletRequest, Connection<Facebook> facebookConnection) throws ApplicationException {
        String urlPath = "/api/v0/web/user/facebook";
        RegisterFacebookAccountWebRequest registerFacebookAccountWebRequest = new RegisterFacebookAccountWebRequest();
        ConnectionData facebookConnectionData = facebookConnection.createData();
        registerFacebookAccountWebRequest.setExpireTime(facebookConnectionData.getExpireTime());
        registerFacebookAccountWebRequest.setFacebookAppId(facebookAppId);
        registerFacebookAccountWebRequest.setToken(facebookConnectionData.getAccessToken());
        String requestPayload = gson.toJson(registerFacebookAccountWebRequest);
        String response = postRequest(httpServletRequest, urlPath, requestPayload);
        return gson.fromJson(response, UserDto.class);
    }

    public UserDto updateUserProfile(HttpServletRequest httpServletRequest, UpdateUserRequestWebDto updateUserRequestWebDto) throws ApplicationException {
        String urlPath = "/api/v0/web/user/profile";
        String requestPayload = gson.toJson(updateUserRequestWebDto);
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
            String response = EntityUtils.toString(httpResponse.getEntity());
            logger.info("Response : " + response);
            return response;
        } catch (Exception ex) {
            throw new ApplicationException(ex);
        }
    }

    public String delete(HttpServletRequest httpServletRequest, String urlPath) throws ApplicationException {
        return delete(httpServletRequest, urlPath, null);
    }
    public String delete(HttpServletRequest httpServletRequest, String urlPath, Map<String, String> addedParameters) throws ApplicationException {
        try {
            logger.info("Getting Results from " + urlPath);
            URIBuilder uriBuilder = new URIBuilder().setScheme("http").setHost(apiHost).setPath(urlPath);
            Map<String, String[]> parameters = httpServletRequest.getParameterMap();
            for (Entry<String, String[]> oneParameterEntry : parameters.entrySet()) {
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
            HttpDelete httpget = new HttpDelete(uri);

            logger.info("Deleting from " + httpget.getURI());
            HttpResponse httpResponse = getHttpClient().execute(httpget);
            return EntityUtils.toString(httpResponse.getEntity());
        } catch (Exception ex) {
            throw new ApplicationException(ex);
        }
    }

}
