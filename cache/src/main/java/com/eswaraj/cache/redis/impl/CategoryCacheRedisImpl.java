package com.eswaraj.cache.redis.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.eswaraj.cache.CategoryCache;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.web.dto.CategoryWithChildCategoryDto;

@Component
public class CategoryCacheRedisImpl extends BaseCacheRedisImpl implements CategoryCache {

    @Autowired
    @Qualifier("complaintStringRedisTemplate")
    private StringRedisTemplate complaintStringRedisTemplate;

    @Override
    public void refreshAllCategories() throws ApplicationException {
        List<CategoryWithChildCategoryDto> categories = appService.getAllCategories();
        String redisKey = appKeyService.getAllCategoriesKey();
        String allCategories = gson.toJson(categories);
        logger.info("Writing Key {} to redis with Value as {}", redisKey, allCategories);
        complaintStringRedisTemplate.opsForValue().set(redisKey, allCategories);

    }

    @Override
    public String getAllCategories() throws ApplicationException {
        String allCategoriesKey = appKeyService.getAllCategoriesKey();
        String allCategories = complaintStringRedisTemplate.opsForValue().get(allCategoriesKey);
        return allCategories;
    }

}
