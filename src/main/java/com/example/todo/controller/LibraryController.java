package com.example.todo.controller;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.todo.entity.BooksEntity;
import com.example.todo.entity.UsersEntity;
import com.example.todo.forms.BookAddRequest;
import com.example.todo.forms.LoginRequest;
import com.example.todo.forms.SearchBooksRequest;
import com.example.todo.service.LibraryService;
import com.example.todo.utils.HashGenerator;

import jakarta.servlet.http.HttpSession;
@Controller
public class LibraryController {
	
	@GetMapping(value = "/log")
	public String getLogPage(Model model) {
			return "/log";
	}
	
	
	
	@GetMapping(value = "/exhibit")
    public String displayAdd(Model model) {
        BookAddRequest bka = new BookAddRequest();
		model.addAttribute("bookAddRequest", bka);
        return "/add";
    }
	
    
	@RequestMapping(value = "/add", method = RequestMethod.POST)
    public String exhibit(@Validated @ModelAttribute BookAddRequest bookRequest, BindingResult result, Model model, HttpSession session) {
        session.setAttribute("userId", 1);
		int userId = (int) session.getAttribute("userId");
		if (result.hasErrors()) {
            // 入力チェックエラーの場合
            List<String> errorList = new ArrayList<String>();
            for (ObjectError error : result.getAllErrors()) {
                errorList.add(error.getDefaultMessage());
            }
            model.addAttribute("validationError", errorList);
            return "/exhibit";
        }
       model.addAttribute("search_box", new SearchBooksRequest());
       List<BooksEntity> bookshelf = libraryService.displayBooks();
	   model.addAttribute("bookshelf", bookshelf);
		return "/home";
        
    }


	@GetMapping(value = "/mybook")
	public String getmybookPage(Model model) {
			return "/mybook";
	}
	@Autowired
	private LibraryService libraryService;
	
	@GetMapping(value = "/login")
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
	@GetMapping(value = "/home")
	public String home(Model model) {
		List<BooksEntity> bookshelf = libraryService.displayBooks();
		model.addAttribute("bookshelf", bookshelf);
		return "/home";
	}
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String search(Model model) {
		//		String moji = model.getAttribute("search");
		//		List<BooksEntity> bookshelf = libraryService.searchBooks();
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
