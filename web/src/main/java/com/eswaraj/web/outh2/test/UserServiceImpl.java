package com.eswaraj.web.outh2.test;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class UserServiceImpl implements UserService, UserDetailsService {
	private Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
	private PasswordEncoder passwordEncoder;
	
	List<User> users = new ArrayList<>();
	
	@Autowired
	public UserServiceImpl(PasswordEncoder passwordEncoder) {
		users.add(new User("a@a.com",passwordEncoder.encodePassword("password", "salt"), Role.ROLE_ADMIN));
		users.add(new User("b@b.com",passwordEncoder.encodePassword("password", "salt"), Role.ROLE_ADMIN));
		users.add(new User("c@c.com",passwordEncoder.encodePassword("password", "salt"), Role.ROLE_ADMIN));
		users.add(new User("d@d.com",passwordEncoder.encodePassword("password", "salt"), Role.ROLE_ADMIN));
	}
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			return locateUser(username);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private User locateUser(final String username) throws Exception {
		User user = null;
		for(User fetchUser: users) {
			if(fetchUser.getUsername().equals(username)) {
				return user;
			}
		}
		if (user == null) {
			LOG.debug("Credentials [{}] failed to locate a user - hint, username.", username.toLowerCase());
			throw new Exception();
		}
		return user;
	}
}
