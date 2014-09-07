package com.eswaraj.tasks.bolt.processors.cache;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.core.service.LocationService;
import com.eswaraj.tasks.bolt.processors.AbstractBoltProcessor;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;
import com.eswaraj.web.dto.LocationDto;
import com.google.gson.JsonObject;

@Component
public class RefreshAllLocationBoltProcessor extends AbstractBoltProcessor {

    @Autowired
    private LocationService locationService;

	@Override
    public Result processTuple(Tuple input) {
		try{
            // String jsonMessage = input.getString(0);

            Long start = 0L;
            Long pageSize = 100L;
            List<LocationDto> locations;
            while (true) {
                locations = locationService.getLocations(start, pageSize);

                if (locations == null || locations.isEmpty()) {
                    break;
                }
                for (LocationDto oneLocation : locations) {
                    logInfo("     oneLocation : " + oneLocation);
                    if (oneLocation != null) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("locationId", oneLocation.getId());
                        writeToStream(input, new Values(jsonObject.toString()));
                    }
                }
                start = start + locations.size();
            }
            return Result.Success;
		}catch(Exception ex){
            logError("Unable to process", ex);
        }
        return Result.Failed;
	}

}
