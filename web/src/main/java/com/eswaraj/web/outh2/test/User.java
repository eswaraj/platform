package com.eswaraj.web.outh2.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class User implements UserDetails {

	private String emailAddress;
	private String hashedPassword;
	private Boolean verified = false;
	private List<Role> roles = new ArrayList<Role>();

	public User() {
		super();
	}

	public User(final String emailAddress, final String hashedPassword, Role role) {
		this();
		this.emailAddress = emailAddress;
		this.hashedPassword = hashedPassword;
		this.roles.add(role);
	}

	private void deSerializeRoles(List<String> roles) {
		for(String role : roles) {
			this.addRole(Role.valueOf(role));
		}
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		for( Role role : this.getRoles() ){
			GrantedAuthority authority = new SimpleGrantedAuthority(role.name());
			authorities.add(authority);
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		return hashedPassword;
	}

	@Override
	public String getUsername() {
		return emailAddress;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public String getEmailAddress() {
		return emailAddress;
	}


	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Boolean isVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public List<Role> getRoles() {
		return Collections.unmodifiableList(this.roles);
	}

	public void addRole(Role role) {
		this.roles.add(role);
	}

	public boolean hasRole(Role role) {
		return (this.roles.contains(role));
	}
}