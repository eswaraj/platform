package com.eswaraj.core.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.util.StringUtils;

import com.eswaraj.core.convertors.FacebookAccountConvertor;
import com.eswaraj.core.convertors.FacebookAppPermissionConvertor;
import com.eswaraj.core.convertors.PersonConvertor;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Device;
import com.eswaraj.domain.nodes.Device.DeviceType;
import com.eswaraj.domain.nodes.FacebookAccount;
import com.eswaraj.domain.nodes.FacebookApp;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.User;
import com.eswaraj.domain.nodes.relationships.FacebookAppPermission;
import com.eswaraj.domain.repo.DeviceRepository;
import com.eswaraj.domain.repo.FacebookAccountRepository;
import com.eswaraj.domain.repo.FacebookAppPermissionRepository;
import com.eswaraj.domain.repo.FacebookAppRepository;
import com.eswaraj.domain.repo.PersonRepository;
import com.eswaraj.domain.repo.UserRepository;
import com.eswaraj.web.dto.UserDto;

public class BaseService implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected FacebookAccountConvertor facebookAccountConvertor;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected PersonConvertor personConvertor;
    @Autowired
    protected PersonRepository personRepository;
    @Autowired
    protected FacebookAppPermissionRepository facebookAppPermissionRepository;
    @Autowired
    protected FacebookAppPermissionConvertor facebookAppPermissionConvertor;
    @Autowired
    protected DeviceRepository deviceRepository;
    @Autowired
    protected FacebookAppRepository facebookAppRepository;
    @Autowired
    protected FacebookAccountRepository facebookAccountRepository;


    protected <DbType> DbType getObjectIfExistsElseThrowExcetpion(Long id, String objectName, GraphRepository<DbType> repository) throws ApplicationException {
		DbType dbObject = null;
		if(id != null && id > 0){
			dbObject = repository.findOne(id);
		}
		if(dbObject == null){
			throw new ApplicationException("No such " + objectName + " exists[id="+ id +"]");
		}
		return dbObject;
	}

    protected String readFile(String file) throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classloader.getResourceAsStream(file);
        String json = IOUtils.toString(inputStream, "UTF-8");
        return json;
    }

    protected User createUpdateUser(UserDto userDto) throws ApplicationException {
        User existingFacebookUser = null;
        Collection<User> existingDeviceUsers = null;
        User returnUniqueUser = null;
        if (userDto.getFacebookAccount() != null && !StringUtils.isEmpty(userDto.getFacebookAccount().getFacebookUserId())) {
            existingFacebookUser = userRepository.getUserByFacebookUserId(userDto.getFacebookAccount().getFacebookUserId());
        }
        if (userDto.getDevice() != null && !StringUtils.isEmpty(userDto.getDevice().getDeviceId())) {
            existingDeviceUsers = userRepository.getUserByDevice(userDto.getDevice().getDeviceId());
        }
        if (existingDeviceUsers == null && existingFacebookUser == null) {
            // Create New User and Person
            returnUniqueUser = createAnonymousUserAndPerson(userDto);
        }
        if (existingDeviceUsers != null && existingFacebookUser != null) {
            // Merge Two Users only if device user do not have facebook account
            // linked to it
            returnUniqueUser = mergeUser(existingFacebookUser, existingDeviceUsers);
        }
        if (existingDeviceUsers == null && existingFacebookUser != null) {
            returnUniqueUser = existingFacebookUser;
        }
        if (existingDeviceUsers != null && existingFacebookUser == null) {
            returnUniqueUser = existingDeviceUsers.iterator().next();
        }

        addFacebookAccount(userDto, returnUniqueUser);
        addUserDevice(userDto, returnUniqueUser);

        return returnUniqueUser;
    }

    private User createAnonymousUserAndPerson(UserDto userDto) throws ApplicationException {
        Person person = null;
        if (userDto.getPerson() == null) {
            person = new Person();
            person.setName("anonymous");
            person.setExternalId(UUID.randomUUID().toString());
        } else {
            person = personConvertor.convert(userDto.getPerson());
        }
        person = personRepository.save(person);

        User user = new User();
        user.setPerson(person);
        user = userRepository.save(user);
        return user;
    }

    protected User createAnonymousUserAndPerson() throws ApplicationException {
        Person person = new Person();
        person.setName("anonymous");
        person.setExternalId(UUID.randomUUID().toString());
        person = personRepository.save(person);

        User user = new User();
        user.setPerson(person);
        user.setExternalId(UUID.randomUUID().toString());
        user = userRepository.save(user);
        return user;
    }

    private void addFacebookAccount(UserDto userDto, User user) throws ApplicationException {
        if (userDto.getFacebookAccount() != null) {
            FacebookAccount facebookAccount = facebookAccountConvertor.convert(userDto.getFacebookAccount());
            facebookAccount.setUser(user);
            facebookAccount = facebookAccountRepository.save(facebookAccount);

            FacebookAppPermission facebookAppPermission = facebookAppPermissionConvertor.convert(userDto.getFacebookAppPermission());
            facebookAppPermission.setFacebookAccount(facebookAccount);
            facebookAppPermission = facebookAppPermissionRepository.save(facebookAppPermission);
        }
    }

    private Device addUserDevice(UserDto userDto, User user) {
        Device device = null;
        if (userDto.getDevice() != null) {
            
            device = deviceRepository.getDeviceByDeviceId(userDto.getDevice().getDeviceId());
            if (device == null) {
                device = new Device();
                device.setDeviceId(userDto.getDevice().getDeviceId());
                device.setDeviceType(DeviceType.valueOf(userDto.getDevice().getDeviceTypeRef()));
                device = deviceRepository.save(device);
            }
        }
        return device;
    }

    private User mergeUser(User facebookUser, Collection<User> deviceUser) {

        return facebookUser;
    }

    protected FacebookApp getOrCreateFacebookApp(String facebookAppId) {
        FacebookApp facebookApp = facebookAppRepository.findByPropertyValue("appId", facebookAppId);
        if (facebookApp == null) {
            facebookApp = new FacebookApp();
            facebookApp.setAppId(facebookAppId);
            facebookApp = facebookAppRepository.save(facebookApp);
        }
        return facebookApp;
    }

}
