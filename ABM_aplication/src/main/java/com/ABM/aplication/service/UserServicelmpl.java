package com.ABM.aplication.service;

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
	
}

