package com.eswaraj.core.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.eswaraj.core.convertors.DeviceConvertor;
import com.eswaraj.core.convertors.FacebookAccountConvertor;
import com.eswaraj.core.convertors.PersonConvertor;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppKeyService;
import com.eswaraj.core.service.TempService;
import com.eswaraj.core.util.DateUtil;
import com.eswaraj.domain.nodes.Election;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.LocationType;
import com.eswaraj.domain.nodes.Party;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.nodes.PoliticalBodyType;
import com.eswaraj.domain.repo.AddressRepository;
import com.eswaraj.domain.repo.ElectionRepository;
import com.eswaraj.domain.repo.LocationRepository;
import com.eswaraj.domain.repo.LocationTypeRepository;
import com.eswaraj.domain.repo.PartyRepository;
import com.eswaraj.domain.repo.PersonRepository;
import com.eswaraj.domain.repo.PoliticalBodyAdminRepository;
import com.eswaraj.domain.repo.PoliticalBodyTypeRepository;
import com.eswaraj.domain.repo.UserDeviceRepository;
import com.eswaraj.domain.repo.UserRepository;
import com.eswaraj.domain.validator.exception.ValidationException;
import com.eswaraj.queue.service.aws.impl.AwsUploadUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class TempServiceImpl extends BaseService implements TempService {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private FacebookAccountConvertor facebookAccountConvertor;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PersonConvertor personConvertor;
    @Autowired
    private UserDeviceRepository userDeviceRepository;
    @Autowired
    private DeviceConvertor deviceConvertor;
    @Autowired
    private AppKeyService appKeyService;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private LocationTypeRepository locationTypeRepository;
    @Autowired
    private PartyRepository partyRepository;
    @Autowired
    @Qualifier("stringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private PoliticalBodyAdminRepository politicalBodyAdminRepository;
    @Autowired
    private PoliticalBodyTypeRepository politicalBodyTypeRepository;
    @Autowired
    private ElectionRepository electionRepository;
    @Autowired
    private AwsUploadUtil awsUploadUtil;

    private Gson gson = new Gson();

    /**
     * Only used for importing data DO NOT use any where as it uploads photo
     */
    @Override
    public Person savePerson(Person person) throws ApplicationException {
        person = personRepository.save(person);
        String imageType = ".jpg";
        String remoteFileName = person.getId() + imageType;
        if (!StringUtils.isEmpty(person.getProfilePhoto())) {
            InputStream localFilePathToUpload;
            try {
                localFilePathToUpload = new URL(person.getProfilePhoto()).openStream();
                logger.info("Remote File Name : {}", remoteFileName);
                logger.info("Downloading File : {}", person.getProfilePhoto());
                String httpPath = awsUploadUtil.uploadProfileImageJpeg(remoteFileName, localFilePathToUpload);
                person.setProfilePhoto(httpPath);
                logger.info("Uplaoded File : {}", httpPath);
                person = personRepository.save(person);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger.info("person after save is {}", person);
        return (person);
    }

    Pattern PATTERN = Pattern
            .compile("(?:(?:\\r\\n)?[ \\t])*(?:(?:(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*\\<(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*(?:,@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)*:(?:(?:\\r\\n)?[ \\t])*)?(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:(?:\\r\\n)?[ \\t])*)|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*:(?:(?:\\r\\n)?[ \\t])*(?:(?:(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*\\<(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*(?:,@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)*:(?:(?:\\r\\n)?[ \\t])*)?(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:(?:\\r\\n)?[ \\t])*)(?:,\\s*(?:(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*\\<(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*(?:,@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)*:(?:(?:\\r\\n)?[ \\t])*)?(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:(?:\\r\\n)?[ \\t])*))*)?;\\s*)");

    private String getPartyName(JsonObject jsonObject) {
        String partyName = jsonObject.get("party").getAsString();
        switch(partyName){

        case "Congress":
        case "INC":
            return "INDIAN NATIONAL CONGRESS";
        case "JD s":
        case "JD S":
        case "JD(S)":
            return "Janata Dal (Secular)";
        case "BJP":
            return "BHARTIYA JANTA PARTY";
        case "Bharatiya Janata Party":
            return "BHARTIYA JANTA PARTY";
        case "INDEPENDENT":
        case "Independent":
            return "INDEPENDENTS";
            default :
                return partyName;
        }
        
    }
    @Override
    public JsonArray createLocationAndMpRecord(String body) throws ApplicationException {
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = jsonParser.parse(body).getAsJsonArray();
        Map<String, Set<String>> states = new TreeMap<String, Set<String>>();
        Map<String, Set<String>> utes = new TreeMap<String, Set<String>>();
        Set<String> parties = new TreeSet<String>();
        Set<String> uts = new HashSet<String>();
        uts.add("Andaman and Nicobar Islands");
        uts.add("Chandigarh");
        uts.add("Dadra and Nagar Haveli");
        uts.add("Daman and Diu");
        uts.add("Lakshadweep");
        uts.add("Puducherry");
        Map<String, Location> existingLocationMap = new HashMap<String, Location>();
        Map<String, Party> existingPartyMap = new HashMap<String, Party>();
        Party party;
        Calendar startDate = Calendar.getInstance();
        startDate.set(Calendar.YEAR, 2014);
        startDate.set(Calendar.DATE, 16);
        startDate.set(Calendar.MONTH, 4);
        Location india = locationRepository.findLocationByName("(?i)India");
        if (india == null) {
            throw new ApplicationException("India Location Not found");
        }
        PoliticalBodyType politicalBodyType = politicalBodyTypeRepository.findByPropertyValue("shortName", "MP");
        if (politicalBodyType == null) {
            politicalBodyType = politicalBodyTypeRepository.findByName("(?i)Member of Parliament");
        }
        if (politicalBodyType == null) {
            throw new ApplicationException("Member of Parliament Political Body Type Not found");
        }
        Election election = electionRepository.findAllElectionsOfPoliticalBodyType(politicalBodyType).get(0);
        if (election == null) {
            throw new ApplicationException("No Election found for political Body Type");
        }

        JsonArray returenJsonArray = new JsonArray();
        JsonArray returenNotCreateJsonArray = new JsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            String name = jsonObject.get("name").getAsString();
            String state = jsonObject.get("state").getAsString();
            String partyName = getPartyName(jsonObject);
            parties.add(partyName);
            String constituency = jsonObject.get("constituency").getAsString();
            Location stateUt = null;
            if (uts.contains(state)) {
                if (utes.get(state) == null) {
                    utes.put(state, new TreeSet<String>());
                }
                utes.get(state).add(constituency);
                stateUt = createLocation(existingLocationMap, state, "Union Territory", india, true);
            } else {
                if (states.get(state) == null) {
                    states.put(state, new TreeSet<String>());
                }
                states.get(state).add(constituency);
                stateUt = createLocation(existingLocationMap, state, "State", india, true);
            }
            // Create PC
            String emails = null;
            if (jsonObject.has("email")) {
                emails = jsonObject.get("email").getAsString();
            }

            String officeEmail = null;
            String personEmail = null;
            if (!StringUtils.isEmpty(emails)) {
                emails = emails.replaceAll("\\[dot\\]", ".");
                emails = emails.replaceAll("\\[at\\]", "@");
                emails = emails.replaceAll("\\(i\\) ", "");
                Matcher matcher = PATTERN.matcher(emails);
                while (matcher.find()) {
                    String email = matcher.group().replaceAll(" ", "");
                    if (email.contains("sansad")) {
                        officeEmail = email;
                    } else {
                        personEmail = email;
                    }
                }
            }

            party = createParty(existingPartyMap, partyName);
            Location pc = createLocation(existingLocationMap, constituency, "Parliament Constituency", stateUt, false);
            List<Person> persons = personRepository.findPersonsByName(name);
            if (persons.isEmpty() || persons.size() > 1) {
                createPoliticalBodyAdmin(pc, politicalBodyType, party, null, election, startDate.getTime(), officeEmail, returenNotCreateJsonArray);
            } else {
                createPoliticalBodyAdmin(pc, politicalBodyType, party, persons.get(0), election, startDate.getTime(), officeEmail, returenJsonArray);
            }

        }
        List<String> allData = new ArrayList<String>();
        System.out.println("********");
        allData.addAll(parties);
        printAll(parties);
        int count = 1;
        for (Entry<String, Set<String>> oneState : states.entrySet()) {
            allData.add(count + ". State : " + oneState.getKey());
            allData.addAll(oneState.getValue());
            System.out.println(count + ". State : " + oneState.getKey());
            printAll(oneState.getValue());
            count++;
        }
        for (Entry<String, Set<String>> oneState : utes.entrySet()) {
            allData.add(count + ". UT : " + oneState.getKey());
            allData.addAll(oneState.getValue());
            System.out.println(count + ". State : " + oneState.getKey());
            printAll(oneState.getValue());
            count++;
        }
        returenJsonArray.add(returenNotCreateJsonArray);
        return returenJsonArray;
    }

    private void createPoliticalBodyAdmin(Location location, PoliticalBodyType politicalBodyType, Party party, Person person, Election election, Date startDate, String officeEmail,
            JsonArray returenJsonArray) throws ApplicationException {
        PoliticalBodyAdmin politicalBodyAdmin = new PoliticalBodyAdmin();
        politicalBodyAdmin.setLocation(location);
        politicalBodyAdmin.setPoliticalBodyType(politicalBodyType);
        politicalBodyAdmin.setParty(party);
        politicalBodyAdmin.setPerson(person);
        politicalBodyAdmin.setElection(election);
        politicalBodyAdmin.setStartDate(startDate);
        politicalBodyAdmin.setEmail(officeEmail);
        if (person != null) {
            politicalBodyAdmin = savePoliticalBodyAdmin(politicalBodyAdmin);
        }
        returenJsonArray.add(gson.toJsonTree(politicalBodyAdmin));
    }

    private Party createParty(Map<String, Party> existingPartyMap, String partyName) {
        Party party = existingPartyMap.get(partyName);
        if (party != null) {
            return party;
        }
        party = partyRepository.findPartyByName("(?i)" + partyName);
        if (party == null) {
            party = new Party();
            party.setName(partyName);
            party.setShortName(partyName);
            System.out.println("Creating new Party " + party);
            party = partyRepository.save(party);
        }
        existingPartyMap.put(partyName, party);
        return party;
    }

    @Deprecated
    private Location createLocation(Map<String, Location> existingLocationMap, String locationName, String locationTypename, Location parentLocation, boolean cache) throws ApplicationException {
        Location location = existingLocationMap.get(locationName);
        if (location != null) {
            return location;
        }
        LocationType locationType = locationTypeRepository.findLocationTypeByName("(?i)" + locationTypename);
        if (locationType == null) {
            throw new ApplicationException("No lcoation type found of name " + locationTypename);
        }

        location = locationRepository.findLocationByNameAndLocationTypeAndParent("(?i)" + locationName, locationType, parentLocation);
        if (location == null) {
            location = new Location();
            location.setName(locationName);
            location.setParentLocation(parentLocation);
            location.setLocationType(locationType);
            System.out.println("Creating new Location " + location + " for " + locationTypename);
            location = saveLocation(location);
        }
        if (cache) {
            existingLocationMap.put(locationName, location);
        }

        return location;
    }

    @Deprecated
    private void printAll(Set<String> data) {
        System.out.println(" ---------------- ");
        for (String oneString : data) {
            System.out.println(oneString);
        }
    }

    public PoliticalBodyAdmin savePoliticalBodyAdmin(PoliticalBodyAdmin politicalBodyAdmin) throws ApplicationException {
        politicalBodyAdmin.setActive(isActive(politicalBodyAdmin));
        if (politicalBodyAdmin.getLocation().getUrlIdentifier().startsWith("/")) {
            politicalBodyAdmin.setUrlIdentifier("/leader" + politicalBodyAdmin.getLocation().getUrlIdentifier() + "/" + politicalBodyAdmin.getPoliticalBodyType().getShortName().toLowerCase());
        } else {
            politicalBodyAdmin.setUrlIdentifier("/leader/" + politicalBodyAdmin.getLocation().getUrlIdentifier() + "/" + politicalBodyAdmin.getPoliticalBodyType().getShortName().toLowerCase());
        }
        validateWithExistingData(politicalBodyAdmin);
        return politicalBodyAdminRepository.save(politicalBodyAdmin);
    }

    private boolean isActive(PoliticalBodyAdmin politicalBodyAdmin) {
        if (politicalBodyAdmin.getStartDate() == null) {
            throw new ValidationException("Start date can not be null");
        }
        boolean active = false;
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(politicalBodyAdmin.getStartDate());
        startDate = DateUtil.getStartOfDay(startDate);
        Calendar today = Calendar.getInstance();
        if (today.after(startDate) && (politicalBodyAdmin.getEndDate() == null || politicalBodyAdmin.getEndDate().after(today.getTime()))) {
            active = true;
        }
        return active;
    }

    private void validateWithExistingData(PoliticalBodyAdmin politicalBodyAdmin) throws ApplicationException {
        if (politicalBodyAdmin.getLocation() != null && politicalBodyAdmin.getPoliticalBodyType() != null) {
            Collection<PoliticalBodyAdmin> allPoliticalBodyAdminsForLocation = politicalBodyAdminRepository.getAllPoliticalAdminByLocationAndPoliticalBodyType(politicalBodyAdmin.getLocation(),
                    politicalBodyAdmin.getPoliticalBodyType());
            adjustActivePoliticalAdminForLocation(politicalBodyAdmin, allPoliticalBodyAdminsForLocation);
            checkForDateOverlap(politicalBodyAdmin, allPoliticalBodyAdminsForLocation);
        }
    }

    private void adjustActivePoliticalAdminForLocation(PoliticalBodyAdmin politicalBodyAdmin, Collection<PoliticalBodyAdmin> allPoliticalBodyAdminsForLocation) throws ApplicationException {
        if (!politicalBodyAdmin.isActive()) {
            // if this is not active just go back
            return;
        }

        for (PoliticalBodyAdmin onePoliticalBodyAdmin : allPoliticalBodyAdminsForLocation) {
            if (!onePoliticalBodyAdmin.getId().equals(politicalBodyAdmin.getId())) {
                if (onePoliticalBodyAdmin.isActive()) {
                    // throw new ApplicationException("Another Active Political Admin exists [id="+onePoliticalBodyAdmin.getId()+"], please make him/her inactive first and then make this active");
                    // instead of throwing exception we are just turning other active Admin to inactive
                    onePoliticalBodyAdmin.setActive(false);
                    politicalBodyAdminRepository.save(onePoliticalBodyAdmin);
                }

            }
        }
    }

    private void checkForDateOverlap(PoliticalBodyAdmin politicalBodyAdmin, Collection<PoliticalBodyAdmin> allPoliticalBodyAdminsForLocation) throws ApplicationException {
        for (PoliticalBodyAdmin onePoliticalBodyAdmin : allPoliticalBodyAdminsForLocation) {
            if (!onePoliticalBodyAdmin.getId().equals(politicalBodyAdmin.getId())) {
                // We need to check political admin being saved with other admins only
                if (checkIfDatesAreOverlapped(onePoliticalBodyAdmin.getStartDate(), onePoliticalBodyAdmin.getEndDate(), politicalBodyAdmin.getStartDate(), politicalBodyAdmin.getEndDate())) {
                    throw new ApplicationException("Start date and end dates of two Political admin for this location overallped [id1=" + onePoliticalBodyAdmin.getId() + ", startDate="
                            + onePoliticalBodyAdmin.getStartDate() + ", endDate=" + onePoliticalBodyAdmin.getEndDate() + "] and [id2=" + politicalBodyAdmin.getId() + ", startDat="
                            + politicalBodyAdmin.getStartDate() + ", endDate=" + politicalBodyAdmin.getEndDate());
                }
            }
        }
    }

    private boolean checkIfDatesAreOverlapped(Date startDate1, Date endDate1, Date startDate2, Date endDate2) {
        if (endDate1 == null && endDate2 == null) {
            return true;
        }
        if (endDate2 == null && (startDate1.after(startDate2) || endDate1.after(startDate2))) {
            return true;
        }
        if (endDate1 == null && (startDate2.after(startDate1) || endDate2.after(startDate1))) {
            return true;
        }
        if (endDate1 != null && endDate2 != null && ((startDate1.after(startDate2) && startDate1.before(endDate2))) || (endDate1.after(startDate2) && endDate1.before(endDate2))
                || (endDate2.after(startDate1) && endDate2.before(endDate1)) || (startDate2.after(startDate1) && startDate2.before(endDate1))) {
            return true;
        }

        return false;
    }
    public Location saveLocation(Location location) throws ApplicationException {
        location.setUrlIdentifier(getLocationUrlIdentifier(location));
        checkParentChildRule(location);
        return locationRepository.save(location);
    }

    private void checkParentChildRule(Location location) throws ApplicationException {
        // LocationType parentLocationType = locationTypeRepository.findOne(location.getParentLocation().getLocationType().getId());
        if (location.getParentLocation() == null) {
            if (location.getLocationType().getParentLocationType() != null) {
                throw new ApplicationException("Can not create a Location of type [" + location.getLocationType().getName() + "], without a parent Location");
            }
        } else {
            Location parentLocation = locationRepository.findOne(location.getParentLocation().getId());
            logger.info("Location : {}", location);
            logger.info("parentLocation : {}", parentLocation);
            LocationType locationType = locationTypeRepository.findOne(location.getLocationType().getId());
            logger.info("locationType : {}", locationType);
            if (!locationType.getParentLocationType().getId().equals(parentLocation.getLocationType().getId())) {
                LocationType parentLocationType = locationTypeRepository.findOne(parentLocation.getLocationType().getId());
                throw new ApplicationException("Can not create a Location of type [" + location.getLocationType().getName() + "], under location type [" + parentLocationType.getName() + "]");
            }
        }
    }

    private String getLocationUrlIdentifier(Location oneLocation) {

        if (oneLocation.getParentLocation() == null) {
            String urlIdentifier = removeExtraChars(oneLocation.getName());
            return urlIdentifier;
        }

        Location location = oneLocation;
        LocationType locationType;
        String locationTypeUrlId;
        String locationTypeNameUrl;
        String urlIdentifier = "";
        while (location != null) {
            locationType = locationTypeRepository.findOne(location.getLocationType().getId());
            locationTypeUrlId = getLocationTypeUrlIdentifier(locationType);
            if (!locationTypeUrlId.equals("country")) {// No need to add country
                                                       // in url
                locationTypeNameUrl = removeExtraChars(location.getName());
                urlIdentifier = "/" + locationTypeUrlId + "/" + locationTypeNameUrl + urlIdentifier;
            }

            if (location.getParentLocation() == null) {
                break;
            }
            location = locationRepository.findOne(location.getParentLocation().getId());
        }
        return urlIdentifier;
    }

    private String removeExtraChars(String str) {
        String urlIdentifier = str.toLowerCase();
        urlIdentifier = urlIdentifier.replace(' ', '-');
        urlIdentifier = urlIdentifier.replace("&", "");
        return urlIdentifier;
    }

    private String getLocationTypeUrlIdentifier(LocationType oneLocationType) {
        String urlIdentifier = oneLocationType.getName().toLowerCase();
        urlIdentifier = urlIdentifier.replace("&", "");
        if (urlIdentifier.contains(" ")) {
            // names like assembly constituency
            String parts[] = urlIdentifier.split(" ");
            String id = "";
            for (String onePart : parts) {
                if (StringUtils.isEmpty(onePart)) {
                    continue;
                }
                id = id + onePart.charAt(0);
            }
            urlIdentifier = id;
        }
        return urlIdentifier;
    }

    @Override
    public JsonArray createLocationAndWardRecord(String body) throws ApplicationException {
        String stateName = "Karnataka";
        String politicalBodyTypeName = "Corporator";
        String electionName = "BBMP Elections 2010";
        String corporationName = "Bruhat Bengaluru Mahanagara Palike";
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = jsonParser.parse(body).getAsJsonArray();
        Set<String> parties = new TreeSet<String>();
        Set<String> wards = new TreeSet<String>();
        Map<String, Location> existingLocationMap = new HashMap<String, Location>();
        Map<String, Party> existingPartyMap = new HashMap<String, Party>();
        Party party;
        Calendar startDate = Calendar.getInstance();
        startDate.set(Calendar.YEAR, 2010);
        startDate.set(Calendar.DATE, 28);
        startDate.set(Calendar.MONTH, 2);
        Location india = locationRepository.findLocationByName("(?i)" + stateName);
        if (india == null) {
            throw new ApplicationException(stateName + " Location Not found");
        }
        Location corporation = locationRepository.findLocationByName("(?i)" + corporationName);
        if (corporation == null) {
            throw new ApplicationException(corporationName + " Location Not found");
        }
        
        PoliticalBodyType politicalBodyType = politicalBodyTypeRepository.findByPropertyValue("shortName", politicalBodyTypeName);
        if (politicalBodyType == null) {
            politicalBodyType = politicalBodyTypeRepository.findByName("(?i)" + politicalBodyTypeName);
        }
        if (politicalBodyType == null) {
            throw new ApplicationException(politicalBodyTypeName + " Political Body Type Not found");
        }
        Election election = electionRepository.findByName("(?i)" + electionName);
        if (election == null) {
            throw new ApplicationException("No Election found for name " + electionName);
        }

        JsonArray returenJsonArray = new JsonArray();
        JsonArray returenNotCreateJsonArray = new JsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            String name = jsonObject.get("name").getAsString();
            String partyName = getPartyName(jsonObject);
            String constituency = jsonObject.get("ac_name").getAsString();
            String wardName = jsonObject.get("ward_name").getAsString();
            String wardNumber = jsonObject.get("ward_number").getAsString();
            String address = jsonObject.get("address").getAsString();
            String mobile = jsonObject.get("phone").getAsString();
            String profilePhoto = null;
            if (jsonObject.get("photo") != null) {
                profilePhoto = jsonObject.get("photo").getAsString();
            }

            parties.add(partyName);
            wards.add(wardNumber + " - " + wardName);

            party = createParty(existingPartyMap, partyName);
            Location ward = createLocation(existingLocationMap, wardNumber + " - " + wardName, "Ward", corporation, false);
            List<Person> persons = personRepository.findPersonsByName(name);

            if (persons.isEmpty()) {
                Person person = new Person();
                person.setName(name);
                person.setMobileNumber1(mobile);
                person.setProfilePhoto(profilePhoto);
                person.setBiodata("<b>Address : </b> " + address);

                person = savePerson(person);
                createPoliticalBodyAdmin(ward, politicalBodyType, party, person, election, startDate.getTime(), null, returenNotCreateJsonArray);
            } else if (persons.size() > 1) {
                createPoliticalBodyAdmin(ward, politicalBodyType, party, null, election, startDate.getTime(), null, returenNotCreateJsonArray);
            } else {
                createPoliticalBodyAdmin(ward, politicalBodyType, party, persons.get(0), election, startDate.getTime(), null, returenJsonArray);
            }


        }
        List<String> allData = new ArrayList<String>();
        System.out.println("********");
        allData.addAll(parties);
        printAll(parties);
        allData.addAll(wards);
        printAll(wards);
        returenJsonArray.add(returenNotCreateJsonArray);
        return returenJsonArray;
    }

    @Override
    public JsonArray createLocationAndMlaRecord(String body) throws ApplicationException {
        String stateName = "Karnataka";
        String politicalBodyTypeName = "MLA";
        String electionName = "Karnataka Assembly Elections 2013";
        String locationTypeNameToBeCreated = "Assembly Constituency";
        createLocationAndLeaderRecord(body, politicalBodyTypeName, electionName, stateName, locationTypeNameToBeCreated);
        return null;
    }

    private JsonArray createLocationAndLeaderRecord(String body, String politicalBodyTypeName, String electionName, String parentLocationName, String locationTypeName) throws ApplicationException {
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = jsonParser.parse(body).getAsJsonArray();
        Set<String> parties = new TreeSet<String>();
        Set<String> locations = new TreeSet<String>();
        Map<String, Location> existingLocationMap = new HashMap<String, Location>();
        Map<String, Party> existingPartyMap = new HashMap<String, Party>();
        Party party;
        Location parentLocation = locationRepository.findLocationByName("(?i)" + parentLocationName);
        if (parentLocation == null) {
            throw new ApplicationException(parentLocationName + " Location Not found");
        }

        PoliticalBodyType politicalBodyType = politicalBodyTypeRepository.findByPropertyValue("shortName", politicalBodyTypeName);
        if (politicalBodyType == null) {
            politicalBodyType = politicalBodyTypeRepository.findByShortName("(?i)" + politicalBodyTypeName);
        }
        if (politicalBodyType == null) {
            throw new ApplicationException(politicalBodyTypeName + " Political Body Type Not found");
        }
        Election election = electionRepository.findByName("(?i)" + electionName);
        if (election == null) {
            throw new ApplicationException("No Election found for name " + electionName);
        }

        JsonArray returenJsonArray = new JsonArray();
        JsonArray returenNotCreateJsonArray = new JsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            String name = jsonObject.get("name").getAsString();
            String partyName = getPartyName(jsonObject);
            String constituency = jsonObject.get("constituency").getAsString();
            String address = jsonObject.get("address").getAsString();
            String mobile = null;
            if (jsonObject.get("phone") != null) {
                mobile = jsonObject.get("phone").getAsString();
            }

            String email = null;
            if (jsonObject.get("email") != null) {
                email = jsonObject.get("email").getAsString();
                if (email.trim().equals("")) {
                    email = null;
                }
            }
            String profilePhoto = null;
            if (jsonObject.get("photo") != null) {
                profilePhoto = jsonObject.get("photo").getAsString();
            }

            parties.add(partyName);
            locations.add(constituency);

            // party = createParty(existingPartyMap, partyName);
            // Location ward = createLocation(existingLocationMap, constituency, locationTypeName, parentLocation, false);
            List<Person> persons = personRepository.findPersonsByName(name);

            if (persons.isEmpty()) {
                Person person = new Person();
                person.setName(name);
                person.setMobileNumber1(mobile);
                person.setProfilePhoto(profilePhoto);
                person.setBiodata("<b>Address : </b> " + address);

                person = savePerson(person);
                // createPoliticalBodyAdmin(ward, politicalBodyType, party, person, election, election.getStartDate(), null, returenNotCreateJsonArray);
            } else if (persons.size() > 1) {
                // createPoliticalBodyAdmin(ward, politicalBodyType, party, null, election, election.getStartDate(), null, returenNotCreateJsonArray);
            } else {
                // createPoliticalBodyAdmin(ward, politicalBodyType, party, persons.get(0), election, election.getStartDate(), null, returenJsonArray);
            }

        }
        List<String> allData = new ArrayList<String>();
        System.out.println("********");
        allData.addAll(parties);
        printAll(parties);
        allData.addAll(locations);
        printAll(locations);
        returenJsonArray.add(returenNotCreateJsonArray);
        return returenJsonArray;
    }

}
