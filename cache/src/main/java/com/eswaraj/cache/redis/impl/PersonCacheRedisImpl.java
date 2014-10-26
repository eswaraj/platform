package com.eswaraj.cache.redis.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.eswaraj.cache.PersonCache;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppKeyService;
import com.eswaraj.core.service.StormCacheAppServices;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PersonCacheRedisImpl implements PersonCache {

    @Autowired
    @Qualifier("personStringRedisTemplate")
    private StringRedisTemplate personStringRedisTemplate;

    @Autowired
    private AppKeyService appKeyService;

    @Autowired
    private StormCacheAppServices stormCacheAppServices;

    private JsonParser jsonParser = new JsonParser();

    @Override
    public void refreshPerson(long personId) throws ApplicationException {
        JsonObject personJsonObject = stormCacheAppServices.getPerson(personId);

        String personKey = appKeyService.getPersonKey(personId);
        personStringRedisTemplate.opsForValue().set(personKey, personJsonObject.toString());

    }

    @Override
    public String getPersonById(long personId) throws ApplicationException {
        String personKey = appKeyService.getPersonKey(personId);
        return personStringRedisTemplate.opsForValue().get(personKey);
    }

    @Override
    public List<String> getPersonsByIds(Collection<Long> personIds) throws ApplicationException {
        List<String> keys = new ArrayList<>(personIds.size());
        for (Long onePersonId : personIds) {
            keys.add(appKeyService.getPersonKey(onePersonId));
        }
        List<String> persons = personStringRedisTemplate.opsForValue().multiGet(keys);
        return null;
    }

}
