package com.picture.publishing.publisher.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.picture.publishing.publisher.dto.UserDto;
import com.picture.publishing.publisher.service.impl.UserServiceImpl;

@RestController
public class RegisterationController {

	@Autowired
	private UserServiceImpl userService;

	@PostMapping(value = "/register")
	public ResponseEntity<Boolean> createNewUser(@RequestBody UserDto user) {
		return new ResponseEntity<>(userService.saveUser(user, false), HttpStatus.CREATED);
	}

	@PostMapping(value = "/admin/register")
	public ResponseEntity<Boolean> createNewAdmin(@RequestBody UserDto user) {
		return new ResponseEntity<>(userService.saveUser(user, true), HttpStatus.CREATED);
	}

}