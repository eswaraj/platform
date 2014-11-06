package com.eswaraj.tasks.bolt.processors;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.cache.PoliticalAdminCache;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppKeyService;
import com.eswaraj.core.service.AppService;
import com.eswaraj.core.service.LocationService;
import com.eswaraj.core.service.StormCacheAppServices;
import com.eswaraj.queue.service.QueueService;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;
import com.eswaraj.web.dto.PoliticalBodyAdminDto;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class PoliticalBodyAdminChangeBoltProcessor extends AbstractBoltProcessor {

    @Autowired
    private StormCacheAppServices stormCacheAppServices;
    @Autowired
    private AppKeyService appKeyService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private QueueService queueService;
    @Autowired
    private AppService appService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private PoliticalAdminCache politicalAdminCache;



    @Override
    public Result processTuple(Tuple inputTuple) {
        try{
            String pbAdminChangeMessage = (String) inputTuple.getValue(0);
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject)jsonParser.parse(pbAdminChangeMessage);
            Long locationId = jsonObject.get("locationId").getAsLong();
            Long politicalBodyAdminId = jsonObject.get("politicalBodyAdminId").getAsLong();
            
            politicalAdminCache.refreshPoliticalBodyAdmin(politicalBodyAdminId);
            
            JsonObject locationJsonObject = new JsonObject();
            locationJsonObject.addProperty("locationId", locationId);
            writeToStream(inputTuple, new Values(locationJsonObject.toString()));

            //Now send a message to process all complaint of this Location

            // queueService.sendReprocesAllComplaintOfLocation(locationId);
            return Result.Success;
        }catch(Exception ex){
            logError("Unable to refresh Political Body Admin ", ex);
        }
        return Result.Failed;
    }

    private void processLocationCurrentAdmins(Set<Long> pbAdminIds, Long locationId) throws ApplicationException {
        logInfo("Processing location {}", locationId);
        List<PoliticalBodyAdminDto> allCurrentPoliticalAdmins = appService.getAllCurrentPoliticalBodyAdminByLocationId(locationId);

        if (allCurrentPoliticalAdmins != null && !allCurrentPoliticalAdmins.isEmpty()) {
            for (PoliticalBodyAdminDto onePoliticalBodyAdminDto : allCurrentPoliticalAdmins) {
                pbAdminIds.add(onePoliticalBodyAdminDto.getId());
            }
        }

    }
}
