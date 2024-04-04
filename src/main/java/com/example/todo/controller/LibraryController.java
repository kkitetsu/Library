package com.example.todo.controller;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.todo.entity.BooksEntity;
import com.example.todo.forms.DongwookRequest;
import com.example.todo.service.LibraryService;
import com.example.todo.utils.HashGenerator;

@Controller
public class LibraryController {

	@Autowired
	private LibraryService libraryService;

	@GetMapping(value = "/login")
	public String getLoginPage(Model model) {
		return "/login";

	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String doLogin(Model model, @RequestParam("id") String id,
			@RequestParam("pw") String pw) {

		String hashedPassword = "";
		try {
			hashedPassword = HashGenerator.generateHash(pw);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		// TODO: Handle login logic
		System.out.println(hashedPassword);

		if (true) {
			return "/login";
		}

		return "/home";
	}

	@GetMapping(value = "/home")
	public String home(Model model) {

		List<BooksEntity> bookshelf = libraryService.displayBooks();

		model.addAttribute("bookshelf", bookshelf);

		return "/home";
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String search(Model model) {

		//		String moji = model.getAttribute("search");
		//		
		//		List<BooksEntity> bookshelf = libraryService.searchBooks();

		return "/home";
	}

	@GetMapping("/register")
	public String getRegisterPage() {
		return "/register";
	}
}
