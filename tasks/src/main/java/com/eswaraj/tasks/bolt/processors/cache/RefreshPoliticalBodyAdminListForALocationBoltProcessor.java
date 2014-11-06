package com.eswaraj.tasks.bolt.processors.cache;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backtype.storm.tuple.Tuple;

import com.eswaraj.cache.LocationCache;
import com.eswaraj.core.service.AppService;
import com.eswaraj.tasks.bolt.processors.AbstractBoltProcessor;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;

@Component
public class RefreshPoliticalBodyAdminListForALocationBoltProcessor extends AbstractBoltProcessor {

    @Autowired
    private AppService appService;
    @Autowired
    private LocationCache locationCache;



    @Override
    public Result processTuple(Tuple inputTuple) {
        try{
            Long locationId = (Long) inputTuple.getValue(0);
            
            Set<String> pbAdminIds = appService.getAllCurrentPoliticalAdminIdsOfLocation(locationId);

            locationCache.setLocationPoliticalAdmins(locationId, pbAdminIds);

            return Result.Success;
        }catch(Exception ex){
            logError("Unable to refresh Political Body Admin ", ex);
        }
        return Result.Failed;
    }

}
