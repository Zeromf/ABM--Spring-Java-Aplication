package com.ABM.aplication.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ABM.aplication.entity.User;
import com.ABM.aplication.repository.UserRepository;

@Service
public class UserServicelmpl implements UserService{

	@Autowired
	UserRepository repository;
	
	
	
	@Override
	public Iterable <User> getAllUsers(){
		//Me trae todos lo usuarios
		return repository.findAll();
		
		
	}
	private boolean checkusernameAvailable(User user) throws Exception {
		
		Optional<User> userFound=repository.findByUsername(user.getUsername());
		if (userFound.isPresent()) {
			throw new Exception("Username no disponible");
		}
		return true;
	}
	
	
	private boolean cheackPasswordValid(User user) throws Exception {
		if (!user.getPassword().equals(user.getConfirmPassword())) {
			throw new Exception("Password y Confirm Password no son iguales");
		}
		
		return true;
	}
	@Override
	public User createUser(User user) throws Exception {

		if (checkusernameAvailable(user) && cheackPasswordValid(user)) {
			user=repository.save(user);
		}
		
		
		return user;
	}
	
	
}

