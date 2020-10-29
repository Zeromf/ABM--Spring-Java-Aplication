package com.ABM.aplication.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.ABM.aplication.entity.User;
import com.ABM.aplication.repository.RoleRepository;
import com.ABM.aplication.service.UserService;

@Controller
public class UserController {
	
		@Autowired
		UserService userService;
		@Autowired
		RoleRepository roleRepository;
		
		
		@GetMapping("/")
		public String index() {
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

			 return "user-form/user-view";
			
		}
		
		@PostMapping("/editUser")
		
		public String postEditUserForm(@Valid @ModelAttribute("userForm")User user,BindingResult result,ModelMap modelo){ 
			if(result.hasErrors()) {
				modelo.addAttribute("userForm", user);
				modelo.addAttribute("formTab","active");
				modelo.addAttribute("editMode","true");
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
		
		
}