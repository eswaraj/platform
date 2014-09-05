package com.eswaraj.core.service.impl;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.StormCacheAppServices;
import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.nodes.ExecutiveBodyAdmin;
import com.eswaraj.domain.nodes.ExecutivePost;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.LocationType;
import com.eswaraj.domain.nodes.Party;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.Photo;
import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.nodes.PoliticalBodyType;
import com.eswaraj.domain.repo.CategoryRepository;
import com.eswaraj.domain.repo.ComplaintRepository;
import com.eswaraj.domain.repo.ExecutiveBodyAdminRepository;
import com.eswaraj.domain.repo.ExecutivePostRepository;
import com.eswaraj.domain.repo.LocationRepository;
import com.eswaraj.domain.repo.LocationTypeRepository;
import com.eswaraj.domain.repo.PartyRepository;
import com.eswaraj.domain.repo.PersonRepository;
import com.eswaraj.domain.repo.PhotoRepository;
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
    @Autowired
    private ComplaintRepository complaintRepository;
    @Autowired
    private ExecutiveBodyAdminRepository executiveBodyAdminRepository;
    @Autowired
    private ExecutivePostRepository executivePostRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PhotoRepository photoRepository;

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
        jsonObject.addProperty("url", location.getUrlIdentifier());

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

    @Override
    public JsonObject getCompleteComplaintInfo(Long complaintId) throws ApplicationException {
        Complaint complaint = complaintRepository.findOne(complaintId);
        return buildComplaintInfo(complaint);
    }

    private JsonObject buildComplaintInfo(Complaint complaint) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        JsonObject complaintJsonObject = new JsonObject();
        complaintJsonObject.addProperty("id", complaint.getId());
        complaintJsonObject.addProperty("complaintTime", complaint.getComplaintTime());
        complaintJsonObject.addProperty("complaintTimeIso", sdf.format(new Date(complaint.getComplaintTime())));
        complaintJsonObject.addProperty("title", complaint.getTitle());
        complaintJsonObject.addProperty("description", complaint.getDescription());
        complaintJsonObject.addProperty("lattitude", complaint.getLattitude());
        complaintJsonObject.addProperty("longitude", complaint.getLongitude());
        complaintJsonObject.addProperty("status", complaint.getStatus().toString());

        if (complaint.getAdministrator() != null) {
            ExecutiveBodyAdmin eba = executiveBodyAdminRepository.findOne(complaint.getAdministrator().getId());
            Person ebaPerson = personRepository.findOne(eba.getPerson().getId());
            ExecutivePost ebaExecutivePost = executivePostRepository.findOne(eba.getPost().getId());
            JsonObject ebaJsonObject = new JsonObject();
            ebaJsonObject.addProperty("name", ebaPerson.getName());
            ebaJsonObject.addProperty("profilePhoto", ebaPerson.getProfilePhoto());
            ebaJsonObject.addProperty("postTitle", ebaExecutivePost.getTitle());
            ebaJsonObject.addProperty("postShortTitle", ebaExecutivePost.getShortTitle());
            complaintJsonObject.add("executiveBodyAdmin", ebaJsonObject);
        }

        if (!CollectionUtils.isEmpty(complaint.getCategories())) {
            JsonArray jsonArray = new JsonArray();
            for (Category oneCategory : complaint.getCategories()) {
                JsonObject categoryJsonObject = new JsonObject();
                oneCategory = categoryRepository.findOne(oneCategory.getId());
                if (!oneCategory.isRoot()) {
                    complaintJsonObject.addProperty("categoryTitle", oneCategory.getName());
                }
                categoryJsonObject.addProperty("externalId", oneCategory.getExternalId());
                categoryJsonObject.addProperty("headerImageurl", oneCategory.getHeaderImageUrl());
                categoryJsonObject.addProperty("imageUrl", oneCategory.getImageUrl());
                categoryJsonObject.addProperty("name", oneCategory.getName());
                categoryJsonObject.addProperty("videoUrl", oneCategory.getVideoUrl());
                categoryJsonObject.addProperty("root", oneCategory.isRoot());
                jsonArray.add(categoryJsonObject);
            }
            complaintJsonObject.add("categories", jsonArray);
        }

        if (!CollectionUtils.isEmpty(complaint.getLocations())) {
            JsonArray jsonArray = new JsonArray();
            for (Location oneLocation : complaint.getLocations()) {
                JsonObject locationJsonObject = new JsonObject();
                oneLocation = locationRepository.findOne(oneLocation.getId());
                locationJsonObject.addProperty("externalId", oneLocation.getExternalId());
                locationJsonObject.addProperty("name", oneLocation.getName());
                jsonArray.add(locationJsonObject);
            }
            complaintJsonObject.add("locations", jsonArray);
        }
        if (!CollectionUtils.isEmpty(complaint.getPhotos())) {
            JsonArray photosArray = new JsonArray();
            for (Photo onePhoto : complaint.getPhotos()) {
                JsonObject locationJsonObject = new JsonObject();
                onePhoto = photoRepository.findOne(onePhoto.getId());
                locationJsonObject.addProperty("imageUrlLarge", onePhoto.getLargeUrl());
                locationJsonObject.addProperty("imageUrlMedium", onePhoto.getMediumUrl());
                locationJsonObject.addProperty("imageUrlSmall", onePhoto.getSmallUrl());
                locationJsonObject.addProperty("imageUrlSquare", onePhoto.getSquareUrl());
                photosArray.add(locationJsonObject);
            }
            complaintJsonObject.add("photos", photosArray);
        }
        if (complaint.getPerson() != null) {
            Person person = personRepository.findOne(complaint.getPerson().getId());
            JsonObject personJsonObject = new JsonObject();
            personJsonObject.addProperty("name", person.getName());
            complaintJsonObject.add("loggedBy", personJsonObject);
        }
        return complaintJsonObject;
    }

}
