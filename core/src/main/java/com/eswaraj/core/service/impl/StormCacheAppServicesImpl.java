package com.eswaraj.core.service.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.StormCacheAppServices;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.LocationType;
import com.eswaraj.domain.nodes.Party;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.nodes.PoliticalBodyType;
import com.eswaraj.domain.repo.LocationRepository;
import com.eswaraj.domain.repo.LocationTypeRepository;
import com.eswaraj.domain.repo.PartyRepository;
import com.eswaraj.domain.repo.PersonRepository;
import com.eswaraj.domain.repo.PoliticalBodyAdminRepository;
import com.eswaraj.domain.repo.PoliticalBodyTypeRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component
public class StormCacheAppServicesImpl implements StormCacheAppServices {

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private LocationTypeRepository locationTypeRepository;
    @Autowired
    private PoliticalBodyAdminRepository politicalBodyAdminRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PartyRepository partyRepository;
    @Autowired
    private PoliticalBodyTypeRepository politicalBodyTypeRepository;

    @Override
    public JsonObject getCompleteLocationInfo(Long locationId) throws ApplicationException {
        Location location = locationRepository.findOne(locationId);
        LocationType locationType = locationTypeRepository.findOne(location.getLocationType().getId());
        Collection<PoliticalBodyAdmin> politicalBodyAdmins = politicalBodyAdminRepository.getCurrentPoliticalAdminByLocation(location);
        return buildLocationInfoObject(location, locationType, politicalBodyAdmins);
    }

    private JsonObject buildLocationInfoObject(Location location, LocationType locationType, Collection<PoliticalBodyAdmin> politicalBodyAdmins) {
        JsonObject jsonObject = new JsonObject();

        // Read Location object
        jsonObject.addProperty("externalId", location.getExternalId());
        jsonObject.addProperty("name", location.getName());
        jsonObject.addProperty("latitude", location.getLatitude());
        jsonObject.addProperty("longitude", location.getLongitude());
        jsonObject.addProperty("totalFemaleLiteratePopulation", location.getTotalFemaleLiteratePopulation());
        jsonObject.addProperty("totalFemalePopulation", location.getTotalFemalePopulation());
        jsonObject.addProperty("totalFemaleWorkingPopulation", location.getTotalFemaleWorkingPopulation());
        jsonObject.addProperty("totalLiteratePopulation", location.getTotalLiteratePopulation());
        jsonObject.addProperty("totalMaleLiteratePopulation", location.getTotalMaleLiteratePopulation());
        jsonObject.addProperty("totalMalePopulation", location.getTotalMalePopulation());
        jsonObject.addProperty("totalMaleWorkingPopulation", location.getTotalMaleWorkingPopulation());
        jsonObject.addProperty("totalNumberOfHouses", location.getTotalNumberOfHouses());
        jsonObject.addProperty("totalPopulation", location.getTotalPopulation());
        jsonObject.addProperty("totalWorkingPopulation", location.getTotalWorkingPopulation());

        // Read LocationType Object
        jsonObject.addProperty("locationType", locationType.getName());

        // Read Political body Admins
        if (!CollectionUtils.isEmpty(politicalBodyAdmins)) {
            JsonObject politicalBodyJsonObject;
            JsonArray politicalBodyJsonArray = new JsonArray();
            Party party;
            Person person;
            PoliticalBodyType politicalBodyType;
            for (PoliticalBodyAdmin onePoliticalBodyAdmin : politicalBodyAdmins) {
                politicalBodyJsonObject = new JsonObject();
                politicalBodyJsonObject.addProperty("externalId", onePoliticalBodyAdmin.getExternalId());
                politicalBodyJsonObject.addProperty("startDate", onePoliticalBodyAdmin.getStartDate().getTime());

                person = personRepository.findOne(onePoliticalBodyAdmin.getPerson().getId());
                politicalBodyJsonObject.addProperty("name", person.getName());
                politicalBodyJsonObject.addProperty("personExternalId", person.getExternalId());
                politicalBodyJsonObject.addProperty("gender", person.getGender());
                politicalBodyJsonObject.addProperty("profilePhoto", person.getProfilePhoto());

                if (onePoliticalBodyAdmin.getParty() != null) {
                    party = partyRepository.findOne(onePoliticalBodyAdmin.getParty().getId());
                    politicalBodyJsonObject.addProperty("partyExternalId", party.getExternalId());
                    politicalBodyJsonObject.addProperty("partyName", party.getName());
                    politicalBodyJsonObject.addProperty("partyShortName", party.getShortName());
                }

                politicalBodyType = politicalBodyTypeRepository.findOne(onePoliticalBodyAdmin.getPoliticalBodyType().getId());
                politicalBodyJsonObject.addProperty("politicalAdminType", politicalBodyType.getName());
                politicalBodyJsonObject.addProperty("politicalAdminTypeShort", politicalBodyType.getShortName());
                politicalBodyJsonObject.addProperty("politicalAdminTypeExternalId", politicalBodyType.getExternalId());
                politicalBodyJsonArray.add(politicalBodyJsonObject);
            }
            jsonObject.add("politicalInfo", politicalBodyJsonArray);
        }
        return jsonObject;
    }

}
