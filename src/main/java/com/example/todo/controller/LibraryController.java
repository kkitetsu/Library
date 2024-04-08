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

import com.example.todo.entity.BooksEntity;
import com.example.todo.entity.UsersEntity;
import com.example.todo.forms.LoginRequest;
import com.example.todo.forms.SearchBooksRequest;
import com.example.todo.service.LibraryService;
import com.example.todo.utils.HashGenerator;

import jakarta.servlet.http.HttpSession;
@Controller
public class LibraryController {
	
	@Autowired
	private LibraryService libraryService;
	
	@GetMapping(value = "/log")
	public String getLogPage(Model model) {
			return "/log";
	}
	@GetMapping(value = "/mybook")
	public String getmybookPage(Model model) {
			return "/mybook";
	}
	
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
	
	/**
	 * @author shunsukekuzawa
	 * 
	 * Access home.
	 * 
	 * @param model
	 * @return URL of home.html
	 */
	@GetMapping(value = "/home")
	public String home(Model model) {
		//本のリストを取得
		List<BooksEntity> bookshelf = libraryService.displayBooks();
		
		//検索フィールド
		model.addAttribute("search_box",new SearchBooksRequest());
		
		//本のリストを格納
		model.addAttribute("bookshelf", bookshelf);
		return "/home";
	}

	/**
	 * @author shunsukekuzawa
	 * 
	 * Select books which match conditions.
	 * 
	 * @param model
	 * @param searchBooksRequest
	 * @return URL of home.html
	 */
	@RequestMapping(value = "/home", method = RequestMethod.POST)
	public String search(Model model, SearchBooksRequest searchBooksRequest) {
		//条件に合う本のリストを取得
		List<BooksEntity> bookshelf = libraryService.searchBooks(searchBooksRequest);
		
		//条件が入力
		if (searchBooksRequest.getBook_name() != "") {
			model.addAttribute("condition", searchBooksRequest.getBook_name());
		}
		model.addAttribute("search_box",new SearchBooksRequest());
		model.addAttribute("bookshelf",bookshelf);
		
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