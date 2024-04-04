package com.example.todo.controller;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.todo.entity.UsersEntity;
import com.example.todo.forms.LoginRequest;
import com.example.todo.service.LibraryService;
import com.example.todo.utils.HashGenerator;

import jakarta.servlet.http.HttpSession;

@Controller
public class LibraryController {
	
	@Autowired
	private LibraryService libraryService;
	
	@GetMapping(value="/login")
	public String getLoginPage(Model model) {
		return "/login";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String doLogin(Model model, HttpSession session, 
										@ModelAttribute LoginRequest loginRequest) {
		
		String hashedPassword = "";
		try {
			hashedPassword = HashGenerator.generateHash(loginRequest.getLogin_pw());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
        // ユーザのログイン情報でsqlに取得
        List<UsersEntity> user_info = libraryService.login(loginRequest);
        if (user_info.isEmpty()) {
        	// もしログイン情報が間違いだったら、SQLは空
            model.addAttribute("errMsg", "社員番号もしくはパスワードが違います");
            model.addAttribute("logininfo", new LoginRequest());
            return "/login";
        }

		return "/home";
	}
	
	@GetMapping("/register")
    public String getRegisterPage() {
        return "/register";
    }
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public String doUserRegistration() {
		return "/login";
	}
}
