package com.ABM.aplication.service;

import com.ABM.aplication.Exceptions.UsernameOrIdNotFound;
import com.ABM.aplication.dto.ChangePasswordForm;
import com.ABM.aplication.entity.User;

public interface UserService {

	public Iterable<User>getAllUsers();
	
	public User createUser(User user) throws Exception;
	
	
	public User getUserById(Long id)throws Exception;
	
	
	public User updateUser(User user)throws Exception;
	
	public void deleteUser(Long id)throws UsernameOrIdNotFound;
	
	public User changePassword(ChangePasswordForm form) throws Exception;

	
} 
