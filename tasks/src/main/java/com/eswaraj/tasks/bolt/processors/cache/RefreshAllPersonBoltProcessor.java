package com.eswaraj.tasks.bolt.processors.cache;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.core.service.PersonService;
import com.eswaraj.tasks.bolt.processors.AbstractBoltProcessor;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;
import com.eswaraj.web.dto.PersonDto;
import com.google.gson.JsonObject;

@Component
public class RefreshAllPersonBoltProcessor extends AbstractBoltProcessor {

    @Autowired
    private PersonService personService;

	@Override
    public Result processTuple(Tuple input) throws Exception {
            int page = 0;
            int pageSize = 100;
            List<PersonDto> persons;
            while (true) {
                persons = personService.getPersons(page, pageSize);

                if (persons == null || persons.isEmpty()) {
                    break;
                }
                for (PersonDto onePerson : persons) {
                    logInfo("     onePerson : " + onePerson);
                    if (onePerson != null) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("personId", onePerson.getId());
                    writeToStream(input, new Values(jsonObject.toString()));
                    }
                }
                page++;
            }
            return Result.Success;
	}

}
