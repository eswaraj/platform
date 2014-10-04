/**
 * 
 */
package com.eswaraj.core.service.impl;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.eswaraj.core.convertors.DeviceConvertor;
import com.eswaraj.core.convertors.FacebookAccountConvertor;
import com.eswaraj.core.convertors.PersonConvertor;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppKeyService;
import com.eswaraj.core.service.PersonService;
import com.eswaraj.domain.nodes.Address;
import com.eswaraj.domain.nodes.Device;
import com.eswaraj.domain.nodes.Device.DeviceType;
import com.eswaraj.domain.nodes.FacebookAccount;
import com.eswaraj.domain.nodes.FacebookApp;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.User;
import com.eswaraj.domain.nodes.relationships.FacebookAppPermission;
import com.eswaraj.domain.nodes.relationships.UserDevice;
import com.eswaraj.domain.repo.AddressRepository;
import com.eswaraj.domain.repo.LocationRepository;
import com.eswaraj.domain.repo.LocationTypeRepository;
import com.eswaraj.domain.repo.PersonRepository;
import com.eswaraj.domain.repo.UserDeviceRepository;
import com.eswaraj.domain.repo.UserRepository;
import com.eswaraj.web.dto.DeviceDto;
import com.eswaraj.web.dto.PersonDto;
import com.eswaraj.web.dto.RegisterFacebookAccountRequest;
import com.eswaraj.web.dto.RegisterFacebookAccountWebRequest;
import com.eswaraj.web.dto.UpdateUserRequestWebDto;
import com.eswaraj.web.dto.UserDto;

/**
 * @author ravi
 *
 */
@Component
public class PersonServiceImpl extends BaseService implements PersonService {

    private static final long serialVersionUID = 1L;
    @Autowired
    private FacebookAccountConvertor facebookAccountConvertor;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PersonConvertor personConvertor;
    @Autowired
    private PersonRepository personRepository;
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
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private AddressRepository addressRepository;

    private SimpleDateFormat facebookDobFormat = new SimpleDateFormat("MM/dd/yyyy");

	@Override
	public PersonDto savePerson(PersonDto personDto) throws ApplicationException {
		Person person = personConvertor.convert(personDto);
        logger.info("Address is {}", person.getAddress());
		person = personRepository.save(person);
        addressRepository.save(person.getAddress());
        logger.info("Address after save is {}", person.getAddress());
		return personConvertor.convertBean(person);
	}

	@Override
	public PersonDto getPersonById(Long personId) throws ApplicationException {
		Person person = personRepository.findOne(personId);
		return personConvertor.convertBean(person);
	}

	@Override
	public List<PersonDto> searchPersonStartWithName(String name) throws ApplicationException {
		Collection<Person> searchResult = personRepository.searchPersonByName("name:"+name+"*");
		return personConvertor.convertBeanList(searchResult);
	}

	@Override
	public List<PersonDto> searchPersonWithName(String name) throws ApplicationException {
		Collection<Person> searchResult = personRepository.searchPersonByName("name:*"+name+"*");
		return personConvertor.convertBeanList(searchResult);
	}

    @Override
    public UserDto saveUser(UserDto userDto) throws ApplicationException {
        createUpdateUser(userDto);
        return userDto;
    }

    @Override
    public UserDto registerDevice(DeviceDto deviceDto, String userExternalId) throws ApplicationException {
        Device device = deviceRepository.getDeviceByDeviceId(deviceDto.getDeviceId());
        if (device == null) {
            // This means we have never seen this device so create new one
            device = new Device();
            device.setDeviceId(deviceDto.getDeviceId());
            device.setDeviceType(DeviceType.valueOf(deviceDto.getDeviceTypeRef()));
            device = deviceRepository.save(device);
        }

        User user = null;
        if (!StringUtils.isEmpty(userExternalId)) {
            user = userRepository.getUserByUserExternalId(userExternalId);
        }
        if (user == null) {
            user = createAnonymousUserAndPerson();
        } else {
            user.setPerson(personRepository.findOne(user.getPerson().getId()));
        }
        
        UserDevice userDevice = userDeviceRepository.getUserDeviceRelation(user, device);
        if (userDevice == null) {
            userDevice = new UserDevice();
            userDevice.setDevice(device);
            userDevice.setUser(user);
            userDevice = userDeviceRepository.save(userDevice);
        }
        
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        userDto.setDevice(deviceConvertor.convertBean(device));
        userDto.setPerson(personConvertor.convertBean(user.getPerson()));

        return userDto;
    }

    @Override
    public UserDto registerFacebookAccount(RegisterFacebookAccountRequest registerFacebookAccountRequest) throws ApplicationException {
        if (StringUtils.isEmpty(registerFacebookAccountRequest.getUserExternalId())) {
            throw new ApplicationException("User id is not provided");
        }
        // First make sure user is registered
        User user = userRepository.getUserByUserExternalId(registerFacebookAccountRequest.getUserExternalId());
        if (user == null) {
            logger.error("No such user exists {}", registerFacebookAccountRequest.getUserExternalId());
            throw new ApplicationException("Invalid User");
        }

        // Fetch user profile from facebook
        Facebook facebook = new FacebookTemplate(registerFacebookAccountRequest.getToken());
        FacebookProfile facebookUserProfile = facebook.userOperations().getUserProfile();
        String facebookUserId = facebookUserProfile.getId();
        FacebookAccount facebookAccount = facebookAccountRepository.findByPropertyValue("facebookUserId", facebookUserId);
        if (facebookAccount == null) {
            // Create a new new afcebook account and attach it to user
            facebookAccount = new FacebookAccount();
            facebookAccount.setDateCreated(new Date());
            facebookAccount.setDateModified(new Date());
            facebookAccount.setEmail(facebookUserProfile.getEmail());
            facebookAccount.setGender(facebookUserProfile.getGender());
            facebookAccount.setUserName(facebookUserProfile.getUsername());
            facebookAccount.setUser(user);
            facebookAccount.setName(facebookUserProfile.getName());
            facebookAccount.setFacebookUserId(facebookUserProfile.getId());
            facebookAccount.setExternalId(UUID.randomUUID().toString());
            facebookAccount = facebookAccountRepository.save(facebookAccount);

            FacebookApp facebookApp = getOrCreateFacebookApp(registerFacebookAccountRequest.getFacebookAppId());

            FacebookAppPermission facebookAppPermission = new FacebookAppPermission();
            facebookAppPermission.setExpireTime(registerFacebookAccountRequest.getExpireTime());
            facebookAppPermission.setFacebookAccount(facebookAccount);
            facebookAppPermission.setFacebookApp(facebookApp);
            facebookAppPermission.setToken(registerFacebookAccountRequest.getToken());

            facebookAppPermission = facebookAppPermissionRepository.save(facebookAppPermission);

            // Update Person Info too
            Person person = personRepository.getPersonByUser(user);
            updatePersonInfoFromFacebook(person, facebookUserProfile);
        } else {
            // Retrieve user attached to Facebook account and merge it to user
            // userExternalId
            User facebookAccountExistingUser = userRepository.getUserByFacebookUserId("facebookUserId: " + facebookUserId);
            // facebookAccountExistingUser will become main user(anonymous) and
            // user will be merged into it
            user = mergeUser(facebookAccountExistingUser, user);

        }
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        userDto.setPerson(personConvertor.convertBean(user.getPerson()));
        userDto.setFacebookAccount(facebookAccountConvertor.convertBean(facebookAccount));

        return userDto;
    }

    private User mergeUser(User targetUser, User user) {
        return targetUser;
    }

    private void updatePersonInfoFromFacebook(Person person, FacebookProfile facebookUserProfile) {
        if (StringUtils.isEmpty(person.getEmail())) {
            person.setEmail(facebookUserProfile.getEmail());
        }
        if (StringUtils.isEmpty(person.getGender())) {
            person.setGender(facebookUserProfile.getGender());
        }
        if (StringUtils.isEmpty(person.getName()) || person.getName().equals("anonymous")) {
            person.setName(facebookUserProfile.getName());
        }
        if (person.getDob() == null) {
            String dobStr = facebookUserProfile.getBirthday(); // mm/dd/yyyy format
            if (dobStr != null) {
                try{
                    person.setDob(facebookDobFormat.parse(dobStr));
                } catch (Exception ex) {
                    logger.error("unable to parse date of birthe String {}", dobStr);
                }
            }
        }

        person.setProfilePhoto("http://graph.facebook.com/" + facebookUserProfile.getId() + "/picture");
        person = personRepository.save(person);
    }

    @Override
    public UserDto registerFacebookAccountWebUser(RegisterFacebookAccountWebRequest registerFacebookAccountWebRequest) throws ApplicationException {
        Facebook facebook = new FacebookTemplate(registerFacebookAccountWebRequest.getToken());
        FacebookProfile facebookUserProfile = facebook.userOperations().getUserProfile();
        String facebookUserId = facebookUserProfile.getId();
        logger.info("Getting Facebook Account for Id : {}", facebookUserId);
        FacebookAccount facebookAccount = facebookAccountRepository.findByPropertyValue("facebookUserId", facebookUserId);
        User user;
        FacebookApp facebookApp = getOrCreateFacebookApp(registerFacebookAccountWebRequest.getFacebookAppId());
        if (facebookAccount == null) {
            logger.info("No facebook account found for : {} , so will create new one", facebookUserId);
            user = createAnonymousUserAndPerson();
            // Create a new new afcebook account and attach it to user
            facebookAccount = new FacebookAccount();
            facebookAccount.setDateCreated(new Date());
            facebookAccount.setDateModified(new Date());
            facebookAccount.setEmail(facebookUserProfile.getEmail());
            facebookAccount.setGender(facebookUserProfile.getGender());
            facebookAccount.setUserName(facebookUserProfile.getUsername());
            facebookAccount.setUser(user);
            facebookAccount.setName(facebookUserProfile.getName());
            facebookAccount.setFacebookUserId(facebookUserProfile.getId());
            facebookAccount.setExternalId(UUID.randomUUID().toString());
            facebookAccount = facebookAccountRepository.save(facebookAccount);


            FacebookAppPermission facebookAppPermission = new FacebookAppPermission();
            facebookAppPermission.setFacebookAccount(facebookAccount);
            facebookAppPermission.setFacebookApp(facebookApp);
            if (registerFacebookAccountWebRequest.getExpireTime() != null) {
                facebookAppPermission.setExpireTime(new Date(registerFacebookAccountWebRequest.getExpireTime()));
            }
            facebookAppPermission.setToken(registerFacebookAccountWebRequest.getToken());

            facebookAppPermission = facebookAppPermissionRepository.save(facebookAppPermission);

            // Update Person Info too
            Person person = personRepository.getPersonByUser(user);
            updatePersonInfoFromFacebook(person, facebookUserProfile);
        } else {
            user = userRepository.getUserByFacebookUserId("facebookUserId: " + facebookUserId);
            FacebookAppPermission facebookAppPermission = facebookAppPermissionRepository.getFacebookAccountAndAppRelation(facebookAccount, facebookApp);
            if (facebookAppPermission == null) {
                logger.error("No Relation found between facebook App and Facebook account");
            } else {
                logger.error("Updating facebook App Permission");
                if (registerFacebookAccountWebRequest.getExpireTime() != null) {
                    facebookAppPermission.setExpireTime(new Date(registerFacebookAccountWebRequest.getExpireTime()));
                }
                facebookAppPermission.setToken(registerFacebookAccountWebRequest.getToken());
                facebookAppPermission = facebookAppPermissionRepository.save(facebookAppPermission);
            }
            Person person = personRepository.getPersonByUser(user);
            updatePersonInfoFromFacebook(person, facebookUserProfile);
        }
        return convertUser(user);
    }

    private UserDto convertUser(User user) throws ApplicationException {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        userDto.setPerson(personConvertor.convertBean(personRepository.findOne(user.getPerson().getId())));
        FacebookAccount facebookAccount = facebookAccountRepository.getFacebookAccountByUser(user);
        userDto.setFacebookAccount(facebookAccountConvertor.convertBean(facebookAccount));
        return userDto;
    }
    @Override
    public UserDto updateUserInfo(UpdateUserRequestWebDto updateUserRequestWebDto) throws ApplicationException {
        User user = userRepository.findOne(updateUserRequestWebDto.getUserId());
        Person person = personRepository.getPersonByUser(user);
        if (updateUserRequestWebDto.getName() != null) {
            person.setName(updateUserRequestWebDto.getName());
        }
        if (updateUserRequestWebDto.getVoterId() != null) {
            person.setVoterId(updateUserRequestWebDto.getVoterId());
        }
        if (updateUserRequestWebDto.getLattitude() != null && updateUserRequestWebDto.getLongitude() != null) {
            Address address = person.getAddress();
            if (address == null) {
                address = new Address();
            }
            address.setLongitude(updateUserRequestWebDto.getLongitude());
            address.setLattitude(updateUserRequestWebDto.getLattitude());

            updateAddressLocationBasedOnLatLong(address);
            person.setAddress(address);
            address = addressRepository.save(address);
        }
        person = personRepository.save(person);
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return convertUser(user);
    }

    private void updateAddressLocationBasedOnLatLong(Address address) throws ApplicationException {
        String redisKey = appKeyService.buildLocationKey(address.getLattitude(), address.getLongitude());
        logger.info("Redis Key = " + redisKey);
        Set<String> locations = stringRedisTemplate.opsForSet().members(redisKey);
        logger.info("Redis Values = " + locations);
        if(locations == null || locations.isEmpty()){
            return;
        }

        Location location;
        for (String oneLocation : locations) {
            try{
                location = locationRepository.findOne(Long.parseLong(oneLocation));
                logger.info("location = " + location);
                address.getLocations().add(location);
            }catch(Exception ex){
                logger.error("Unable to add location to address", ex);
            }
            
            
        }

    }
}
