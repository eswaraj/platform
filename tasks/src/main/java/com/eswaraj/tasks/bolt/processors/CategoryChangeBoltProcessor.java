package com.eswaraj.tasks.bolt.processors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backtype.storm.tuple.Tuple;

import com.eswaraj.cache.CategoryCache;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;

@Component
public class CategoryChangeBoltProcessor extends AbstractBoltProcessor {

    @Autowired
    private CategoryCache categoryCache;

    @Override
    public Result processTuple(Tuple input) {
        try{
            categoryCache.refreshAllCategories();
            return Result.Success;
        }catch(Exception ex){
            logError("Unable to refresh Categories ", ex);
        }
        return Result.Failed;
    }
}
