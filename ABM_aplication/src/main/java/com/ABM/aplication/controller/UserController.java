package com.ABM.aplication.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ABM.aplication.Exceptions.UsernameOrIdNotFound;
import com.ABM.aplication.dto.ChangePasswordForm;
import com.ABM.aplication.entity.Role;
import com.ABM.aplication.entity.User;
import com.ABM.aplication.repository.RoleRepository;
import com.ABM.aplication.service.UserService;

@Controller
public class UserController {
	
		@Autowired
		UserService userService;
		@Autowired
		RoleRepository roleRepository;
		
		
		@GetMapping({"/","/login"})
		public String index() {
			return "index";

		}
		
		
		@GetMapping("/signup")
		public String signup(ModelMap modelo) {
			modelo.addAttribute("userForm", new User());
			modelo.addAttribute("roles",roleRepository.findAll());
		//	Role userRole=roleRepository.findByName("USER");
		//	List<Role>roles=Arrays.asList(userRole);
		//	modelo.addAttribute("userForm", new User());
		//	modelo.addAttribute("roles",roles);
			modelo.addAttribute("signup",true);

			return "user-form/user-signup";
		}
		
		@PostMapping("/signup")
		public String postSignup(@Valid @ModelAttribute("userForm")User user,BindingResult result,ModelMap modelo){   
			
		//	modelo.addAttribute("roles",roleRepository.findAll());
			//Role userRole=roleRepository.findByName("USER");
			//List<Role>roles=Arrays.asList(userRole);
		//modelo.addAttribute("userForm", user);
	//		modelo.addAttribute("roles",roles);
		//	modelo.addAttribute("signup",true);
		
	//		if(result.hasErrors()) {

				//return "user-form/user-signup";
				
				//}else {
					//try {
						//userService.createUser(user);
					//} catch (Exception e) {
					//	modelo.addAttribute("formErrorMessage",e.getMessage());
				//		return signup(modelo);

						
			//		}
					
					
		//		}
	//		return "index";
			
//		}
			modelo.addAttribute("userForm", user);
			modelo.addAttribute("roles",roleRepository.findAll());
			modelo.addAttribute("signup",true);

			if(result.hasErrors()) {
				return "user-form/user-signup";
				}else {
					try {
						userService.createUser(user);	
					} catch (Exception e) {
						modelo.addAttribute("formErrorMessage",e.getMessage());
						return "user-form/user-signup";

						
					}
					
					
				}
			
			
			
			return "index";
		
		}
		
		@GetMapping("/userForm")
		public String userForm(Model modelo) {
			modelo.addAttribute("userForm", new User());
			modelo.addAttribute("userList",userService.getAllUsers());
			modelo.addAttribute("roles",roleRepository.findAll());
			modelo.addAttribute("listTab","active");
			return "user-form/user-view";
			
			
		}
		
		
		@PostMapping("/userForm")
		
		public String createUser(@Valid @ModelAttribute("userForm")User user,BindingResult result,ModelMap modelo){   
		
			if(result.hasErrors()) {
				modelo.addAttribute("userForm", user);
				modelo.addAttribute("formTab","active");
				}else {
					try {
						userService.createUser(user);
						modelo.addAttribute("userForm", new User());
						modelo.addAttribute("listTab","active");
					} catch (Exception e) {
						modelo.addAttribute("formErrorMessage",e.getMessage());
						modelo.addAttribute("userForm", user);
						modelo.addAttribute("formTab","active");
						modelo.addAttribute("userList",userService.getAllUsers());
						modelo.addAttribute("roles",roleRepository.findAll());
						
					}
					
					
				}
			
			
			
			modelo.addAttribute("userList", userService.getAllUsers());
			modelo.addAttribute("roles",roleRepository.findAll());
			return "user-form/user-view";
	}
		
		@GetMapping("/editUser/{id}")
		public String getEditUserForm(Model model, @PathVariable(name="id") Long id) throws Exception {
		
			User usertToEdit=userService.getUserById(id);
			
			model.addAttribute("userForm", usertToEdit);
			model.addAttribute("userList",userService.getAllUsers());
			model.addAttribute("roles",roleRepository.findAll());
			model.addAttribute("formTab","active");
			model.addAttribute("editMode","true");
			model.addAttribute("passwordForm",new ChangePasswordForm(id));

			 return "user-form/user-view";
			
		}
		
		@PostMapping("/editUser")
		
		public String postEditUserForm(@Valid @ModelAttribute("userForm")User user,BindingResult result,ModelMap modelo){ 
			if(result.hasErrors()) {
				modelo.addAttribute("userForm", user);
				modelo.addAttribute("formTab","active");
				modelo.addAttribute("editMode","true");
				modelo.addAttribute("passwordForm",new ChangePasswordForm(user.getId()));
				}else {
					try {
						userService.updateUser(user);
						modelo.addAttribute("userForm", new User());
						modelo.addAttribute("listTab","active");
						modelo.addAttribute("editMode","false");

					} catch (Exception e) {
						modelo.addAttribute("formErrorMessage",e.getMessage());
						modelo.addAttribute("userForm", user);
						modelo.addAttribute("formTab","active");
						modelo.addAttribute("userList",userService.getAllUsers());
						modelo.addAttribute("roles",roleRepository.findAll());
						modelo.addAttribute("editMode","false");
						modelo.addAttribute("passwordForm",new ChangePasswordForm(user.getId()));

					}
					
					
				}
			
			
			
			modelo.addAttribute("userList", userService.getAllUsers());
			modelo.addAttribute("roles",roleRepository.findAll());
			return "user-form/user-view";
		
		}
		
		@GetMapping("/userForm/cancel")
		public String cancelEditUser(ModelMap model) {
			return "redirect:/userForm";
		}
		
	
		@GetMapping("/deleteUser/{id}")
		public String deleteUser(Model model, @PathVariable(name="id") Long id) {
			
			try {
				userService.deleteUser(id);
				
			} catch (UsernameOrIdNotFound el) {
				
				model.addAttribute("listErrorMessage",el.getMessage());
				
			}
			
			return userForm(model);
			
		}
		
		@PostMapping("/editUser/changePassword")
		public ResponseEntity postEditUseChangePassword(@Valid @RequestBody ChangePasswordForm form, Errors errors) {

		try {
			
			if (errors.hasErrors()) {
				 String result = errors.getAllErrors()
	             .stream().map(x -> x.getDefaultMessage())
	             .collect(Collectors.joining(""));
				 
		            throw new Exception(result);

			}

			userService.changePassword(form);
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
			
			
			return ResponseEntity.ok("Success");

			
		}	
}