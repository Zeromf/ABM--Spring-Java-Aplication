package com.ABM.aplication.service;

import com.ABM.aplication.entity.User;

public interface UserService {

	public Iterable<User>getAllUsers();
	
	public User createUser(User user) throws Exception;
}
