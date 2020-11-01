package com.ABM.aplication.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ABM.aplication.dto.ChangePasswordForm;
import com.ABM.aplication.entity.User;
import com.ABM.aplication.repository.UserRepository;

@Service
public class UserServicelmpl implements UserService{

	@Autowired
	UserRepository repository;
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	
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
		
		if (user.getConfirmPassword()==null || user.getConfirmPassword().isEmpty()) {
			throw new Exception("El Password es obligatorio");

		}
		
		
		if (!user.getPassword().equals(user.getConfirmPassword())) {
			throw new Exception("Password y Confirm Password no son iguales");
		}
		
		return true;
	}
	@Override
	public User createUser(User user) throws Exception {

		if (checkusernameAvailable(user) && cheackPasswordValid(user)) {
			user=repository.save(user);
			String encodePassword=bCryptPasswordEncoder.encode(user.getPassword());
			user.setPassword(encodePassword);
			
		}

		return user;
	}
	
	@Override
	public User getUserById(Long id) throws Exception {
		return repository.findById(id).orElseThrow(() -> new Exception("El usuario no existe"));
	}
	@Override
	public User updateUser(User fromUser) throws Exception { 
		User toUser=getUserById(fromUser.getId());
		mapUser(fromUser,toUser);
		
		return repository.save(toUser);
	                                   
	}
	
	
	/**
	 * Map Everythin but the password
	 * @param from
	 * @param to
	 */
	
	protected void mapUser(User from,User to) {//Se debe hacer meapeo ,pasar los objetos por los valores
		to.setUsername(from.getUsername());    //De el usuario que tenemos del formulario al usuario encontrado
		to.setFirstName(from.getFirstName()); //en la base de datos
		to.setLastName(from.getLastName());
		to.setEmail(from.getEmail());
		to.setRoles(from.getRoles());
		}
	@Override
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public User deleteUser(Long id) throws Exception {
		
		User user= getUserById(id);
		
		repository.delete(user);
		
		return null;
	}
	@Override
	public User changePassword(ChangePasswordForm form) throws Exception {
		
		User user= getUserById(form.getId());
		
		if(!isLoggedUserADMIN() && !user.getPassword().equals(form.getCurrentPassword())) {
			throw new Exception("Current Password invalido");
		}else {
		
		if (user.getPassword().equals(form.getNewPassword())) {
			throw new Exception("Nuevo debe ser diferente al password actual");

		}}
		
		if (!form.getNewPassword().equals(form.getConfirmPassword())) {
			throw new Exception("Nuevo Password y Current Password no coinciden");

		}
		String encodePassword=bCryptPasswordEncoder.encode(form.getNewPassword());
		user.setPassword(encodePassword);
		
		return repository.save(user);
		
	}
	
	
	
	private boolean isLoggedUserADMIN() {
		 return loggedUserHasRole("ROLE_ADMIN");
	}
	public boolean loggedUserHasRole(String role) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails loggedUser = null;
		Object roles = null; 
		if (principal instanceof UserDetails) {
			loggedUser = (UserDetails) principal;
		
			roles = loggedUser.getAuthorities().stream()
					.filter(x -> role.equals(x.getAuthority() ))      
					.findFirst().orElse(null); //loggedUser = null;
		}
		return roles != null ?true :false;
	}
	
	
	
	
}