package com.picture.publishing.publisher.service.impl;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.picture.publishing.publisher.controller.exception.EntityAlreadyExistsException;
import com.picture.publishing.publisher.dto.UserDto;
import com.picture.publishing.publisher.model.User;
import com.picture.publishing.publisher.model.UserRole;
import com.picture.publishing.publisher.repository.UserRepository;
import com.picture.publishing.publisher.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private static final String EMAIL_ALREADY_EXIST = "There is already a user registered with the email provided";

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public String getCurrentUserName() {
		return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
	}

	@Override
	public User findUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public boolean saveUser(UserDto userDto, boolean admin) {
		boolean userExists = existsByUsernameOrEmail(userDto.getUsername(), userDto.getEmail());
		if (userExists) {
			throw new EntityAlreadyExistsException(EMAIL_ALREADY_EXIST);
		}
		userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
		userDto.setRole(admin ? UserRole.ADMIN_ROLE : UserRole.USER_ROLE);
		userRepository.save(mapper.map(userDto, User.class));
		return true;
	}

	private boolean existsByUsernameOrEmail(String username, String email) {
		return userRepository.existsByUsernameOrEmail(username, email);
	}

}