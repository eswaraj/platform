package com.eswaraj.tasks.bolt.processors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import backtype.storm.tuple.Tuple;

import com.eswaraj.core.service.LocationKeyService;
import com.eswaraj.core.service.StormCacheAppServices;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class LocationUpdatedBoltProcessor extends AbstractBoltProcessor {

    @Autowired
    private LocationKeyService locationKeyService;
    @Autowired
    private StormCacheAppServices stormCacheAppServices;
    private JsonParser jsonParser = new JsonParser();

	@Override
    public Result processTuple(Tuple input) {
		try{
            String message = (String) input.getValue(0);
            JsonObject jsonObject = (JsonObject)jsonParser.parse(message);
            Long locationId = jsonObject.get("locationId").getAsLong();

            JsonObject outputJsonObject = stormCacheAppServices.getCompleteLocationInfo(locationId);
            String urlId = outputJsonObject.get("url").getAsString();
            String redisKey = locationKeyService.getLocationInformationKey(locationId);

            String locationInfo = outputJsonObject.toString();
            logInfo("Writing Key {} to redis with Value as {}", redisKey, locationInfo);
            writeToMemoryStoreValue(redisKey, locationInfo);
            if (!StringUtils.isEmpty(urlId)) {
                writeToMemoryStoreValue(urlId, locationId);
            }

            return Result.Success;
		}catch(Exception ex){
            logError("Unable to process message " + input.getValue(0), ex);
        }
        return Result.Failed;
	}

}
