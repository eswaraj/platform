package com.eswaraj.cache.redis.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.eswaraj.cache.LocationCache;
import com.eswaraj.core.exceptions.ApplicationException;
import com.google.gson.JsonObject;

@Component
public class LocationCacheRedisImpl extends BaseCacheRedisImpl implements LocationCache {

    @Autowired
    @Qualifier("locationStringRedisTemplate")
    private StringRedisTemplate locationStringRedisTemplate;

    @Override
    public void refreshLocationInfo(long locationId) throws ApplicationException {
        JsonObject outputJsonObject = stormCacheAppServices.getCompleteLocationInfo(locationId);
        String urlId = outputJsonObject.get("url").getAsString();
        String redisKey = appKeyService.getLocationKey(locationId);

        String locationInfo = outputJsonObject.toString();
        locationStringRedisTemplate.opsForValue().set(redisKey, locationInfo);
        if (!StringUtils.isEmpty(urlId)) {
            String urlKey = appKeyService.getLocationUrlKey(urlId);
            locationStringRedisTemplate.opsForValue().set(urlKey, String.valueOf(locationId));
        }
    }

    @Override
    public String getLocationInfoById(long locationId) throws ApplicationException {
        String redisKey = appKeyService.getLocationKey(locationId);
        return locationStringRedisTemplate.opsForValue().get(redisKey);
    }

    @Override
    public String getLocationInfoById(String locationId) throws ApplicationException {
        String redisKey = appKeyService.getLocationKey(locationId);
        return locationStringRedisTemplate.opsForValue().get(redisKey);
    }

    @Override
    public String getLocationInfoByUrlIdentifier(String urlIdentifier) throws ApplicationException {
        String urlKey = appKeyService.getLocationUrlKey(urlIdentifier);
        String locationId = locationStringRedisTemplate.opsForValue().get(urlKey);
        if (locationId == null) {
            return null;
        }
        return getLocationInfoById(locationId);
    }

    @Override
    public void setLocationPoliticalAdmins(Long locationId, Set<String> pbAdminIds) throws ApplicationException {
        logger.info("Adding Pb Admins {} for Location {}", pbAdminIds, locationId);
        String locationPoliticalAdminKey = appKeyService.getLocationPoliticalAdminKey(locationId.toString());
        locationStringRedisTemplate.delete(locationPoliticalAdminKey);
        if (pbAdminIds == null || pbAdminIds.isEmpty()) {
            logger.warn("No PB Admin Found for location {} ", locationId);
            return;
        }
        locationStringRedisTemplate.opsForSet().add(locationPoliticalAdminKey, pbAdminIds.toArray(new String[pbAdminIds.size()]));

    }

    @Override
    public Set<String> getLocationPoliticalAdmins(Long locationId) throws ApplicationException {
        String locationPoliticalAdminKey = appKeyService.getLocationPoliticalAdminKey(locationId.toString());
        return locationStringRedisTemplate.opsForSet().members(locationPoliticalAdminKey);
    }

}
