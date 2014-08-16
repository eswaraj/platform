package com.eswaraj.tasks.bolt;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Tuple;

import com.eswaraj.core.service.LocationKeyService;
import com.eswaraj.core.service.impl.LocationkeyServiceImpl;
import com.eswaraj.tasks.topology.EswarajBaseBolt;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class LocationChangeBolt extends EswarajBaseBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private LocationKeyService locationKeyService;
    private Gson gson;
    private JsonParser jsonParser;
    @Override
    public void onPrepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        locationKeyService = new LocationkeyServiceImpl();
        gson = new Gson();
        jsonParser = new JsonParser();
    }

	@Override
    public Result processTuple(Tuple input) {
		try{
            String message = (String) input.getValue(0);
            JsonObject jsonObject = (JsonObject)jsonParser.parse(message);
            Long locationId = jsonObject.get("LocationId").getAsLong();

            JsonObject outputJsonObject = getStormCacheAppServices().getCompleteLocationInfo(locationId);
            String redisKey = locationKeyService.getLocationInformationKey(locationId);

            String locationInfo = outputJsonObject.toString();
            logInfo("Writing Key {} to redis with Value as {}", redisKey, locationInfo);
            writeToMemoryStoreValue(redisKey, locationInfo);
            return Result.Success;
		}catch(Exception ex){

        }
        return Result.Failed;
	}

}
