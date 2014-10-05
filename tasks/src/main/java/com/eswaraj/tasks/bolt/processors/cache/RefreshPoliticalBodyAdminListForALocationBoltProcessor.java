package com.eswaraj.tasks.bolt.processors.cache;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import backtype.storm.tuple.Tuple;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppKeyService;
import com.eswaraj.core.service.AppService;
import com.eswaraj.core.service.LocationService;
import com.eswaraj.core.service.StormCacheAppServices;
import com.eswaraj.queue.service.QueueService;
import com.eswaraj.tasks.bolt.processors.AbstractBoltProcessor;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;
import com.eswaraj.web.dto.LocationDto;
import com.eswaraj.web.dto.PoliticalBodyAdminDto;

@Component
public class RefreshPoliticalBodyAdminListForALocationBoltProcessor extends AbstractBoltProcessor {

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



    @Override
    public Result processTuple(Tuple inputTuple) {
        try{
            Long locationId = (Long) inputTuple.getValue(0);

            Set<Long> pbAdminIds = new HashSet<>();
            List<LocationDto> allParentLocations = locationService.getAllParents(locationId);
            if (allParentLocations != null) {
                for (LocationDto oneLocationDto : allParentLocations) {
                    processLocationCurrentAdmins(pbAdminIds, oneLocationDto.getId());
                }
            }

            String locationRedisKey = appKeyService.getLocationKey(locationId);
            String pbaListHashKey = appKeyService.getPoliticalBodyAdminHashKey();
            stringRedisTemplate.opsForHash().delete(locationRedisKey, pbaListHashKey);

            logInfo("pbAdminIds = {}", pbAdminIds);
            if (!pbAdminIds.isEmpty()) {
                String locationAllCurrentAdmins = StringUtils.collectionToCommaDelimitedString(pbAdminIds);
                stringRedisTemplate.opsForHash().put(locationRedisKey, pbaListHashKey, locationAllCurrentAdmins);
            }


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
