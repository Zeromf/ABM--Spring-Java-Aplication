package com.ABM.aplication.service;

import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ABM.aplication.entity.Role;
import com.ABM.aplication.repository.UserRepository;

@Service
@Transactional

public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
    UserRepository userRepository;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		
		com.ABM.aplication.entity.User appUser = 
                userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Login Username Invalido!"));
		
		Set grantList = new HashSet(); 
		
		for(Role roles:appUser.getRoles()) {
			
		//Crear la lista de los roles/accessos que tienen el usuarios
	        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(roles.getDescription());
            grantList.add(grantedAuthority);
		}
		//Crear y retornar Objeto de usuario soportado por Spring Security
			UserDetails user = (UserDetails) new User(appUser.getUsername(), appUser.getPassword(), grantList);
			return user;
	}

}
