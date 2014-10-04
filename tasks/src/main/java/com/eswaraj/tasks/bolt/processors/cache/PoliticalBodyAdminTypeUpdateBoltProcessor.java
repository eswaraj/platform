package com.eswaraj.tasks.bolt.processors.cache;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backtype.storm.tuple.Tuple;

import com.eswaraj.core.service.AppKeyService;
import com.eswaraj.core.service.AppService;
import com.eswaraj.tasks.bolt.processors.AbstractBoltProcessor;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;
import com.eswaraj.web.dto.PoliticalBodyTypeDto;

@Component
public class PoliticalBodyAdminTypeUpdateBoltProcessor extends AbstractBoltProcessor {

    @Autowired
    private AppService appService;
    @Autowired
    private AppKeyService appKeyService;

	@Override
    public Result processTuple(Tuple input) {
		try{
            List<PoliticalBodyTypeDto> allPoliticalBodyTypes = appService.getAllPoliticalBodyTypes();
            for (PoliticalBodyTypeDto onePoliticalBodyTypeDto : allPoliticalBodyTypes) {
                //Save PoliticalBodyTypes with Key as its Id
              //Save PoliticalBodyTypes Id with Key as its Shortname in lowercase
            }
            return Result.Success;
		}catch(Exception ex){
            logError("Unable to process", ex);
        }
        return Result.Failed;
	}

}
