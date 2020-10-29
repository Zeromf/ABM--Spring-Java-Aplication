package com.ABM.aplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

		
		
	}
