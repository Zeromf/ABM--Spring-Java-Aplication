package com.ABM.aplication.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ABM.aplication.Exceptions.CustomeFieldValidationException;
import com.ABM.aplication.Exceptions.UsernameOrIdNotFound;
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
			throw new CustomeFieldValidationException("Username no disponible","username");
		}
		return true;
	}
	
	
	private boolean cheackPasswordValid(User user) throws Exception {
		
		if (user.getConfirmPassword()==null || user.getConfirmPassword().isEmpty()) {
			throw new CustomeFieldValidationException("El Password es obligatorio","confirmPassword");

		}
		
		
		if (!user.getPassword().equals(user.getConfirmPassword())) {
			throw new CustomeFieldValidationException("Password y Confirm Password no son iguales","password");
		}
		
		return true;
	}
	@Override
	public User createUser(User user) throws Exception {

		if (checkusernameAvailable(user) && cheackPasswordValid(user)) {
			String encodePassword=bCryptPasswordEncoder.encode(user.getPassword());
			user.setPassword(encodePassword);
			
			user=repository.save(user);
		}

		return user;
	}
	
	@Override
	public User getUserById(Long id) throws UsernameOrIdNotFound {
		return repository.findById(id).orElseThrow(() -> new UsernameOrIdNotFound("El Id del usuario no existe"));
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
	public void deleteUser(Long id) throws UsernameOrIdNotFound {
		
		User user= getUserById(id);
		
		repository.delete(user);
		
		
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
		//Obtener el usuario logeado
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		UserDetails loggedUser = null;
		Object roles = null;

		//Verificar que ese objeto traido de sesion es el usuario
		if (principal instanceof UserDetails) {
			loggedUser = (UserDetails) principal;

			roles = loggedUser.getAuthorities().stream()
					.filter(x -> "ROLE_ADMIN".equals(x.getAuthority())).findFirst()
					.orElse(null); 
		}
		return roles != null ? true : false;
	}
	
	
	public User getLoggedUser() throws Exception {
		//Obtener el usuario logeado
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		UserDetails loggedUser = null;

		//Verificar que ese objeto traido de sesion es el usuario
		if (principal instanceof UserDetails) {
			loggedUser = (UserDetails) principal;
		}
		
		User myUser = repository
				.findByUsername(loggedUser.getUsername()).orElseThrow(() -> new Exception("Problemas obteniendo usuario de sesion"));
		
		return myUser;
	}
	
	
	
	
	
	
	
}