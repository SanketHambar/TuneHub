package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entities.Users;
import com.example.demo.services.UsersService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
	@Autowired
	UsersService service;

	@PostMapping("/register")
	public String addUser(@ModelAttribute Users user) {
		boolean userStatus = service.emailExists(user.getEmail());
		if (userStatus == false) {
			service.addUser(user);
			System.out.println("User added");
		} else {
			System.out.println("User already exist");
		}
		return "home";
	}

	@PostMapping("/validate")
	public String validate(@RequestParam("email") String email,
			@RequestParam("password") String password,
			HttpSession session) {
		if (service.validateUser(email, password) == true) {
			String role = service.getRole(email);
			
			session.setAttribute("email", email);
			
			if (role.equals("Admin")) {
				return "adminHome";
			} else {
				return "customerHome";
			}
		}

		else {
			return "login";
		}

	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "login";
	}
	
}