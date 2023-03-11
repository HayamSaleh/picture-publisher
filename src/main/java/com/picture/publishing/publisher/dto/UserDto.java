package com.picture.publishing.publisher.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.picture.publishing.publisher.model.UserRole;

public class UserDto {

	@JsonIgnore
	private Long id;
	private String username;
	private String email;
	private String password;

	@JsonIgnore
	private UserRole role;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

}