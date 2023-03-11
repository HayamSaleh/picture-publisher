package com.picture.publishing.publisher.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface SecurityService {
	boolean isAuthenticated();

	void autoLogin(String email, String password);

	UserDetailsService userService();
}