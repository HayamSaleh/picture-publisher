package com.picture.publishing.publisher.service;

import com.picture.publishing.publisher.dto.UserDto;
import com.picture.publishing.publisher.model.User;

public interface UserService {

	String getCurrentUserName();

	User findUserByUsername(String username);

	boolean saveUser(UserDto userDto, boolean admin);

}
