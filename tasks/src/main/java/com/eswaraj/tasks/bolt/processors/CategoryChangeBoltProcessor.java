package com.eswaraj.tasks.bolt.processors;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import backtype.storm.tuple.Tuple;

import com.eswaraj.core.service.AppKeyService;
import com.eswaraj.core.service.AppService;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;
import com.eswaraj.web.dto.CategoryWithChildCategoryDto;
import com.google.gson.Gson;

@Component
public class CategoryChangeBoltProcessor extends AbstractBoltProcessor {

    @Autowired
    private AppService appService;
    @Autowired
    private AppKeyService appKeyService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private Gson gson = new Gson();



    @Override
    public Result processTuple(Tuple input) {
        try{
            List<CategoryWithChildCategoryDto> categories = appService.getAllCategories();
            String redisKey = appKeyService.getAllCategoriesKey();
            String allCategories = gson.toJson(categories);
            //logInfo("Writing Key {} to redis with Value as {}", redisKey, allCategories);
            stringRedisTemplate.opsForValue().set(redisKey, allCategories);
            return Result.Success;
        }catch(Exception ex){

        }
        return Result.Failed;
    }
}
