package com.example.ProductsAndCatagories.controllers;

import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.ProductsAndCatagories.models.LoginUser;
import com.example.ProductsAndCatagories.models.User;
import com.example.ProductsAndCatagories.repositories.UserRepository;
import com.example.ProductsAndCatagories.service.UserService;



@Controller
public class HomeController {

	@Autowired
	private UserService userServ;
	
	
	@GetMapping("/")
	public String index(Model model) {
	    model.addAttribute("newUser", new User());
	    model.addAttribute("newLogin", new LoginUser());
	    return "index.jsp";
	}
	
	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("newUser") User newUser, 
	        BindingResult result, Model model, HttpSession session) {
	    userServ.register(newUser, result);
	    if(result.hasErrors()) {
	        model.addAttribute("newLogin", new LoginUser());
	        return "index.jsp";
	    }
	    session.setAttribute("user_id", newUser.getId());
	    return "redirect:/home";
	}
	
	@PostMapping("/login")
	public String login(@Valid @ModelAttribute("newLogin") LoginUser newLogin, 
			BindingResult result, Model model, HttpSession session) {
	    User user = userServ.login(newLogin, result);
	    if(result.hasErrors()) {
	        model.addAttribute("newUser", new User());
	        return "index.jsp";
	    }
	    session.setAttribute("user_id", user.getId());
	    session.setAttribute("user", user);

	    return "redirect:/home";
	}
	
	@GetMapping("/home")
	public String home(Model model, HttpSession session) {
		
		if (session.getAttribute("user_id") == null) {
			return "redirect:/";
		}
		
		System.out.println("I should be home");
	
		Long userId = (Long) session.getAttribute("user_id");
		System.out.println("I AM TRYING TO GET THE USER" + session.getAttribute("user"));
		Optional<User> user = userServ.getUserById(userId);
		model.addAttribute("user", user);
		


		System.out.println(model.getAttribute("user"));
		return "home.jsp";
	}
}