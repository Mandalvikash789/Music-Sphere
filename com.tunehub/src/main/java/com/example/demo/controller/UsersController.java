package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entities.LoginData;
import com.example.demo.entities.Song;
import com.example.demo.entities.Users;
import com.example.demo.services.SongService;
import com.example.demo.services.UsersService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//@CrossOrigin("*")
@Controller
public class UsersController {
	@Autowired
	UsersService service;
	@Autowired
	SongService songService;
	@PostMapping("/registration")
	public String addUsers(@ModelAttribute Users user) {
		boolean userStatus = service.emailExists(user.getEmail());
		if(userStatus == false) {
			service.addUser(user);
			System.out.println("User Added");
		}else {
			System.out.println("User Alraedy Exists");
		}
		
		//return "home";
		return "index";
	}
	
	@PostMapping("/validate")
	public String validate(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
		
		System.out.println("Call Recieved");
		if(service.validateUser(email,password) == true) {
			String role = service.getRole(email);
			
			session.setAttribute("email", email);
			
			if(role.equals("admin")) {
				return "adminHome";
			}else {
				Users user = service.getUser(email);
				boolean userStatus =user.isPremium();
				List<Song> songList = songService.fetchAllSongs();
				model.addAttribute("songs", songList);
				model.addAttribute("isPremium", userStatus);
				
				return "customerHome";
			}
			
		}else {
			return "login";
		}
	}
	
//	@GetMapping("/pay")
//	public String pay(@RequestParam String email) {
//		boolean paymentStatus = false;
//		if(paymentStatus == true) {
//			Users user = service.getUser(email);
//			user.setPremium(true);
//			service.updateUser(user);
//		}
//		return "login";
//	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "login";
	}
	
}
