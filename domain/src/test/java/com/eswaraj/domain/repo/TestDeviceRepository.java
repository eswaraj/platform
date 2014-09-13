package com.eswaraj.domain.repo;


import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.eswaraj.domain.nodes.Device;
import com.eswaraj.domain.nodes.Device.DeviceType;
import com.eswaraj.domain.nodes.relationships.UserDevice;
import com.eswaraj.domain.nodes.User;

/**
 * Test for LocationType repository
 * @author ravi
 * @data Apr 20, 2014
 */

@ContextConfiguration(locations = { "classpath:eswaraj-domain-test.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class TestDeviceRepository extends BaseNeo4jEswarajTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PersonRepository personRepository;
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    UserDeviceRepository userDeviceRepository;
	
    /**
     * A simple test to create a Device with a user
     */
	@Test
    public void test01_DeviceRepository() {
        // dontDeleteDbObjects();
        User user = createUserRandom(userRepository, personRepository);
        Device device = createDeviceRandom(deviceRepository, DeviceType.Android);
        UserDevice userDevice = new UserDevice();
        userDevice.setDevice(device);
        userDevice.setUser(user);
        userDevice = userDeviceRepository.save(userDevice);
        System.out.println("User Device : " + userDevice);
	}
	
    /**
     * A simple test to create a Device with two user
     */
    @Test
    public void test02_DeviceRepository() {

        User userA = createUser(userRepository, personRepository, "Ravi");
        User userB = createUser(userRepository, personRepository, "Test");
        Device device = createDeviceRandom(deviceRepository, DeviceType.Android);
        
        UserDevice userDeviceA = createUserDevice(userDeviceRepository, userA, device);
        UserDevice userDeviceB = createUserDevice(userDeviceRepository, userB, device);

        System.out.println("User Device A: " + userDeviceA);
        System.out.println("User Device B: " + userDeviceB);

        // get users by device Id
        List<User> users = userRepository.getUserByDevice(device);
        System.out.println("users=" + users);
        users = userRepository.getUserByDevice(device.getDeviceId());
        System.out.println("users=" + users);

        List<Device> devicesA = deviceRepository.getAllDevicesOfUser(userA);
        System.out.println("devicesA=" + devicesA);
        List<Device> devicesB = deviceRepository.getAllDevicesOfUser(userB);
        System.out.println("devicesB=" + devicesB);
    }
	
	
}
