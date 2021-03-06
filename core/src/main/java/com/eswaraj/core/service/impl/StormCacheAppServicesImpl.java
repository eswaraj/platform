package com.eswaraj.core.service.impl;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.StormCacheAppServices;
import com.eswaraj.domain.nodes.Address;
import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.nodes.Comment;
import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.nodes.DepartmentAdmin;
import com.eswaraj.domain.nodes.DepartmentPost;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.LocationType;
import com.eswaraj.domain.nodes.Party;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.Photo;
import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.nodes.PoliticalBodyType;
import com.eswaraj.domain.nodes.relationships.ComplaintLoggedByPerson;
import com.eswaraj.domain.repo.AddressRepository;
import com.eswaraj.domain.repo.CategoryRepository;
import com.eswaraj.domain.repo.CommentRepository;
import com.eswaraj.domain.repo.ComplaintLoggedByPersonRepository;
import com.eswaraj.domain.repo.ComplaintRepository;
import com.eswaraj.domain.repo.DepartmentAdminRepository;
import com.eswaraj.domain.repo.DepartmentPostRepository;
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
    private DepartmentAdminRepository executiveBodyAdminRepository;
    @Autowired
    private DepartmentPostRepository executivePostRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ComplaintLoggedByPersonRepository complaintLoggedByPersonRepository;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

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
        jsonObject.addProperty("kml", location.getBoundaryFile());
        jsonObject.addProperty("url", location.getUrlIdentifier());
        jsonObject.addProperty("mobileHeaderImageUrl", location.getMobileHeaderImageUrl());

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
                    politicalBodyJsonObject.addProperty("partyImageUrl", party.getImageUrl());
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
        complaintJsonObject.addProperty("locationAddress", complaint.getLocationAddress());
        complaintJsonObject.addProperty("shortUrl", complaint.getShortUrl());


        if (complaint.getAdministrator() != null) {
            JsonArray ebaIds = new JsonArray();
            JsonObject oneAdmin = new JsonObject();
            oneAdmin.addProperty("id", complaint.getAdministrator().getId());
            ebaIds.add(oneAdmin);
            complaintJsonObject.add("eba", ebaIds);
        }

        Collection<PoliticalBodyAdmin> politicalBodyAdmins = politicalBodyAdminRepository.getAllPoliticalAdminOfComplaint(complaint);
        if (politicalBodyAdmins != null && !CollectionUtils.isEmpty(politicalBodyAdmins)) {
            JsonArray jsonArray = new JsonArray();
            for (PoliticalBodyAdmin onePoliticalBodyAdmin : politicalBodyAdmins) {
                JsonObject pbaJsonObject = new JsonObject();
                pbaJsonObject.addProperty("id", onePoliticalBodyAdmin.getId());
                jsonArray.add(pbaJsonObject);
            }
            complaintJsonObject.add("pba", jsonArray);
        }

        if (!CollectionUtils.isEmpty(complaint.getCategories())) {
            JsonArray jsonArray = new JsonArray();
            for (Category oneCategory : complaint.getCategories()) {
                JsonObject categoryJsonObject = new JsonObject();
                oneCategory = categoryRepository.findOne(oneCategory.getId());
                if (!oneCategory.isRoot()) {
                    complaintJsonObject.addProperty("categoryTitle", oneCategory.getName());
                    if (oneCategory.getSystemCategory() != null) {
                        complaintJsonObject.addProperty("systemCategory", oneCategory.getSystemCategory().getName());
                    }
                }
                categoryJsonObject.addProperty("externalId", oneCategory.getExternalId());
                categoryJsonObject.addProperty("headerImageurl", oneCategory.getHeaderImageUrl());
                categoryJsonObject.addProperty("imageUrl", oneCategory.getImageUrl());
                categoryJsonObject.addProperty("name", oneCategory.getName());
                categoryJsonObject.addProperty("videoUrl", oneCategory.getVideoUrl());
                categoryJsonObject.addProperty("root", oneCategory.isRoot());
                categoryJsonObject.addProperty("id", oneCategory.getId());
                jsonArray.add(categoryJsonObject);
            }
            complaintJsonObject.add("categories", jsonArray);

            // add total Complaints
            complaintJsonObject.addProperty("totalComments", commentRepository.findTotalCommentsByComplaint(complaint));
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
        
        List<ComplaintLoggedByPerson> complaintLoggedByPersons = complaintLoggedByPersonRepository.getComplaintLoggedByPersonRelation(complaint);
        if (!CollectionUtils.isEmpty(complaintLoggedByPersons)) {
            JsonArray jsonArray = new JsonArray();
            for (ComplaintLoggedByPerson oneComplaintLoggedByPerson : complaintLoggedByPersons) {
                JsonObject personJsonObject = new JsonObject();
                Person person = oneComplaintLoggedByPerson.getPerson();
                if (oneComplaintLoggedByPerson.isAnonymous()) {
                    personJsonObject.addProperty("name", "Anonymous");
                    personJsonObject.addProperty("profilePhoto", "https://cdn3.iconfinder.com/data/icons/humano2/72x72/emblems/emblem-people.png");
                } else {
                    personJsonObject.addProperty("name", person.getName());
                    personJsonObject.addProperty("profilePhoto", person.getProfilePhoto());
                }
                personJsonObject.addProperty("externalId", person.getExternalId());
                personJsonObject.addProperty("id", person.getId());
                jsonArray.add(personJsonObject);
            }
            complaintJsonObject.add("createdBy", jsonArray);
        }
        Collection<Photo> complaintPhotos = photoRepository.getComplaintPhotos(complaint);
        logger.info("Existing Photos for Complaint : {} = {}", complaint.getId(), complaintPhotos);
        if (!CollectionUtils.isEmpty(complaintPhotos)) {
            JsonArray photosArray = new JsonArray();
            for (Photo onePhoto : complaintPhotos) {
                JsonObject locationJsonObject = new JsonObject();
                locationJsonObject.addProperty("imageUrlLarge", onePhoto.getLargeUrl());
                locationJsonObject.addProperty("imageUrlMedium", onePhoto.getMediumUrl());
                locationJsonObject.addProperty("imageUrlSmall", onePhoto.getSmallUrl());
                locationJsonObject.addProperty("imageUrlSquare", onePhoto.getSquareUrl());
                locationJsonObject.addProperty("orgUrl", onePhoto.getOrgUrl());
                photosArray.add(locationJsonObject);
            }
            complaintJsonObject.add("images", photosArray);
        }
        return complaintJsonObject;
    }

    @Override
    public JsonObject getPoliticalBodyAdmin(Long politicalBodyAdminId) throws ApplicationException {
        PoliticalBodyAdmin onePoliticalBodyAdmin = politicalBodyAdminRepository.findOne(politicalBodyAdminId);

        JsonObject politicalBodyJsonObject = new JsonObject();
        addPropertyIfNotNull(politicalBodyJsonObject, "externalId", onePoliticalBodyAdmin.getExternalId());
        addPropertyIfNotNull(politicalBodyJsonObject, "id", onePoliticalBodyAdmin.getId());
        addPropertyIfNotNull(politicalBodyJsonObject, "startDate", onePoliticalBodyAdmin.getStartDate().getTime());
        addPropertyIfNotNull(politicalBodyJsonObject, "fbAccount", onePoliticalBodyAdmin.getFbAccount());
        addPropertyIfNotNull(politicalBodyJsonObject, "fbPage", onePoliticalBodyAdmin.getFbPage());
        addPropertyIfNotNull(politicalBodyJsonObject, "twitterHandle", onePoliticalBodyAdmin.getTwitterHandle());
        addPropertyIfNotNull(politicalBodyJsonObject, "officeEmail", onePoliticalBodyAdmin.getEmail());
        addPropertyIfNotNull(politicalBodyJsonObject, "urlIdentifier", onePoliticalBodyAdmin.getUrlIdentifier());
        addAddress(politicalBodyJsonObject, "homeAddress", onePoliticalBodyAdmin.getHomeAddress());
        addAddress(politicalBodyJsonObject, "officeAddress", onePoliticalBodyAdmin.getOfficeAddress());

        Person person = personRepository.findOne(onePoliticalBodyAdmin.getPerson().getId());
        politicalBodyJsonObject.addProperty("name", person.getName());
        politicalBodyJsonObject.addProperty("personExternalId", person.getExternalId());
        politicalBodyJsonObject.addProperty("gender", person.getGender());
        politicalBodyJsonObject.addProperty("profilePhoto", person.getProfilePhoto());
        politicalBodyJsonObject.addProperty("biodata", person.getBiodata());
        addPropertyIfNotNull(politicalBodyJsonObject, "landlineNumber1", person.getLandlineNumber1());
        addPropertyIfNotNull(politicalBodyJsonObject, "landlineNumber2", person.getLandlineNumber2());
        addPropertyIfNotNull(politicalBodyJsonObject, "mobileNumber1", person.getMobileNumber1());
        addPropertyIfNotNull(politicalBodyJsonObject, "mobileNumber2", person.getMobileNumber2());
        addPropertyIfNotNull(politicalBodyJsonObject, "email", person.getEmail());
        
        Location location = locationRepository.findOne(onePoliticalBodyAdmin.getLocation().getId());
        JsonObject locationJsonObject = new JsonObject();
        locationJsonObject.addProperty("name", location.getName());
        locationJsonObject.addProperty("id", location.getId());
        politicalBodyJsonObject.add("location", locationJsonObject);

        if (onePoliticalBodyAdmin.getParty() != null) {
            Party party = partyRepository.findOne(onePoliticalBodyAdmin.getParty().getId());
            JsonObject partyJsonObject = new JsonObject();
            addPropertyIfNotNull(partyJsonObject, "externalId", party.getExternalId());
            addPropertyIfNotNull(partyJsonObject, "name", party.getName());
            addPropertyIfNotNull(partyJsonObject, "shortName", party.getShortName());
            addPropertyIfNotNull(partyJsonObject, "id", party.getId());
            addPropertyIfNotNull(partyJsonObject, "imageUrl", party.getImageUrl());
            politicalBodyJsonObject.add("party", partyJsonObject);
        }

        if (onePoliticalBodyAdmin.getPoliticalBodyType() != null) {
            PoliticalBodyType politicalBodyType = politicalBodyTypeRepository.findOne(onePoliticalBodyAdmin.getPoliticalBodyType().getId());
            JsonObject politicalBodyAdminTypeJsonObject = new JsonObject();
            addPropertyIfNotNull(politicalBodyAdminTypeJsonObject, "name", politicalBodyType.getName());
            addPropertyIfNotNull(politicalBodyAdminTypeJsonObject, "shortName", politicalBodyType.getShortName());
            addPropertyIfNotNull(politicalBodyAdminTypeJsonObject, "externalId", politicalBodyType.getExternalId());
            addPropertyIfNotNull(politicalBodyAdminTypeJsonObject, "id", politicalBodyType.getId());
            addPropertyIfNotNull(politicalBodyAdminTypeJsonObject, "description", politicalBodyType.getDescription());
            politicalBodyJsonObject.add("politicalAdminType", politicalBodyAdminTypeJsonObject);
        }


        return politicalBodyJsonObject;
    }

    private void addAddress(JsonObject jsonObject, String propertyName, Address address) {
        if (address == null) {
            return;
        }
        Address addressFromDb = addressRepository.findOne(address.getId());
        JsonObject addressJsonObject = new JsonObject();
        addPropertyIfNotNull(addressJsonObject, "externalId", addressFromDb.getExternalId());
        addPropertyIfNotNull(addressJsonObject, "id", addressFromDb.getId());
        addPropertyIfNotNull(addressJsonObject, "line1", addressFromDb.getLine1());
        addPropertyIfNotNull(addressJsonObject, "line2", addressFromDb.getLine2());
        addPropertyIfNotNull(addressJsonObject, "line3", addressFromDb.getLine3());
        addPropertyIfNotNull(addressJsonObject, "postalCode", addressFromDb.getPostalCode());
        Set<Location> locations = addressFromDb.getLocations();
        if (locations != null) {
            List<Long> locationIds = new ArrayList<>(locations.size());
            for (Location oneLocation : locations) {
                locationIds.add(oneLocation.getId());
            }
            Iterable<Location> locationsFromDb = locationRepository.findAll(locationIds);
            String locationType;
            String jsonPropertyName;
            for (Location oneLocation : locationsFromDb) {
                locationType = oneLocation.getLocationType().getName();
                jsonPropertyName = locationType.toLowerCase();
                switch (jsonPropertyName) {
                case "country":
                    jsonPropertyName = "country";
                    break;
                case "state":
                    jsonPropertyName = "state";
                    break;
                case "assembly constituency":
                    jsonPropertyName = "ac";
                    break;
                case "parliament constituency":
                    jsonPropertyName = "pc";
                    break;
                case "ward":
                    jsonPropertyName = "ward";
                    break;
                case "city":
                    jsonPropertyName = "city";
                    break;
                case "village":
                    jsonPropertyName = "village";
                    break;
                }
                addPropertyIfNotNull(addressJsonObject, jsonPropertyName, oneLocation.getName());
                addPropertyIfNotNull(addressJsonObject, jsonPropertyName + "Url", oneLocation.getUrlIdentifier());
            }
        }
        jsonObject.add(propertyName, addressJsonObject);
    }

    private void addPropertyIfNotNull(JsonObject jsonObject, String propertyName, String value) {
        if (!StringUtils.isEmpty(value)) {
            jsonObject.addProperty(propertyName, value);
        }
    }

    private void addPropertyIfNotNull(JsonObject jsonObject, String propertyName, Long value) {
        if (value != null) {
            jsonObject.addProperty(propertyName, value);
        }
    }

    @Override
    public JsonObject getExecutiveBodyAdmin(Long executiveBodyAdminId) throws ApplicationException {
        DepartmentAdmin eba = executiveBodyAdminRepository.findOne(executiveBodyAdminId);
        Person ebaPerson = personRepository.findOne(eba.getPerson().getId());
        DepartmentPost ebaExecutivePost = executivePostRepository.findOne(eba.getPost().getId());
        JsonObject ebaJsonObject = new JsonObject();
        ebaJsonObject.addProperty("name", ebaPerson.getName());
        ebaJsonObject.addProperty("profilePhoto", ebaPerson.getProfilePhoto());
        ebaJsonObject.addProperty("biodata", ebaPerson.getBiodata());
        ebaJsonObject.addProperty("postTitle", ebaExecutivePost.getTitle());
        ebaJsonObject.addProperty("postShortTitle", ebaExecutivePost.getShortTitle());
        return ebaJsonObject;
    }

    @Override
    public JsonObject getComment(Long commentId) throws ApplicationException {
        logger.info("commentRepository = {} ", commentRepository);
        Comment comment = commentRepository.findOne(commentId);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("text", comment.getText());
        jsonObject.addProperty("creationTime", comment.getCreationTime().getTime());
        jsonObject.addProperty("id", comment.getId());
        if (!StringUtils.isEmpty(comment.getExternalId())) {
            jsonObject.addProperty("externalId", comment.getExternalId());
        }
        jsonObject.addProperty("commentedById", comment.getCreatedBy().getId());

        if (comment.getPoliticalBodyAdmin() != null) {
            jsonObject.addProperty("politicalAdminId", comment.getPoliticalBodyAdmin().getId());
            jsonObject.addProperty("adminComment", true);
        } else {
            jsonObject.addProperty("adminComment", false);
        }

        return jsonObject;
    }

    @Override
    public JsonObject getPerson(Long personId) throws ApplicationException {
        Person person = personRepository.findOne(personId);

        JsonObject personJsonObject = new JsonObject();
        personJsonObject.addProperty("name", person.getName());
        personJsonObject.addProperty("externalId", person.getExternalId());
        personJsonObject.addProperty("gender", person.getGender());
        personJsonObject.addProperty("profilePhoto", person.getProfilePhoto());
        personJsonObject.addProperty("id", person.getId());
        personJsonObject.addProperty("biodata", person.getBiodata());
        return personJsonObject;
    }

}
