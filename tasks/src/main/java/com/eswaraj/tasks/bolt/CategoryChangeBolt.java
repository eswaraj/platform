package com.eswaraj.tasks.bolt;

import java.util.List;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Tuple;

import com.eswaraj.core.service.AppKeyService;
import com.eswaraj.core.service.impl.AppKeyServiceImpl;
import com.eswaraj.tasks.topology.EswarajBaseBolt;
import com.eswaraj.web.dto.CategoryWithChildCategoryDto;
import com.google.gson.Gson;

public class CategoryChangeBolt extends EswarajBaseBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private AppKeyService appKeyService;
    private Gson gson;

    @Override
    public void onPrepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        appKeyService = new AppKeyServiceImpl();
        gson = new Gson();
    }

	@Override
    public Result processTuple(Tuple input) {
		try{
            List<CategoryWithChildCategoryDto> categories = getApplicationService().getAllCategories();
            String redisKey = appKeyService.getAllCategoriesKey();
            String allCategories = gson.toJson(categories);
            logInfo("Writing Key {} to redis with Value as {}", redisKey, allCategories);
            writeToMemoryStoreSet(redisKey, allCategories);
            return Result.Success;
		}catch(Exception ex){

        }
        return Result.Failed;
	}

}
