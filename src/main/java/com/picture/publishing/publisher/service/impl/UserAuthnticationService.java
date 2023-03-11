package com.picture.publishing.publisher.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.picture.publishing.publisher.model.User;

@Service
public class UserAuthnticationService implements UserDetailsService {

	@Autowired
	private UserServiceImpl userService;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) {
		User user = userService.findUserByUsername(username);
		if (user != null) {
			Set<String> roles = new HashSet<>();
			roles.add(user.getRole().toString());
			List<GrantedAuthority> authorities = getUserAuthority(roles);
			return buildUserForAuthentication(user, authorities);
		}
		return null;
	}

	private List<GrantedAuthority> getUserAuthority(Set<String> userRoles) {
		Set<GrantedAuthority> roles = new HashSet<>();
		for (String role : userRoles) {
			roles.add(new SimpleGrantedAuthority(role));
		}
		return new ArrayList<>(roles);
	}

	private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), true,
				true, true, true, authorities);
	}
}