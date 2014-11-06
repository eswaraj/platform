package com.eswaraj.tasks.bolt.processors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backtype.storm.tuple.Tuple;

import com.eswaraj.cache.LocationCache;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class LocationUpdatedBoltProcessor extends AbstractBoltProcessor {

    @Autowired
    private LocationCache locationCache;

    private JsonParser jsonParser = new JsonParser();

	@Override
    public Result processTuple(Tuple input) {
		try{
            String message = (String) input.getValue(0);
            JsonObject jsonObject = (JsonObject)jsonParser.parse(message);
            Long locationId = jsonObject.get("locationId").getAsLong();

            locationCache.refreshLocationInfo(locationId);

            return Result.Success;
		}catch(Exception ex){
            logError("Unable to process message " + input.getValue(0), ex);
        }
        return Result.Failed;
	}

}
