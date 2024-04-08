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

import com.example.todo.dto.SearchLogsDTO;
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

	@Autowired
	private LibraryService libraryService;
	
	/**
	 * @author Lee 
	 * 貸しログ出力画面の表示
	 * 今後、user idを@paramにするmethodに変える予定
	 **/
	@GetMapping(value = "/borrowlog")
	public String getBorrowLogPage(Model model) {
	    List<SearchLogsDTO> BorrowLogs = libraryService.displayBorrowLogs();
	    model.addAttribute("BorrowLogs", BorrowLogs);
		return "/borrowlog";
	}
	/**
	 * @author Lee 
	 * 借りログ出力画面の表示
	 * 今後、user idを@paramにするmethodに変える予定
	 **/
	@GetMapping(value = "/lendlog")
	public String getLendLogPage(Model model) {
	    List<SearchLogsDTO> LendLogs = libraryService.displayLendLogs();
	    model.addAttribute("LendLogs", LendLogs);
		return "/lendlog";
		}
	/**
	 * @author Lee 
	 * 登録したマイブックの出力画面の表示
	 * 今後、user idを@paramにするmethodに変える予定
	 **/	
	@GetMapping(value = "/mybook")
	public String getmybookPage(Model model) {
			return "/mybook";
	}

	/** @author kk */
	@GetMapping(value = "/login")
	public String getLoginPage(Model model) {
		model.addAttribute("loginRequest", new LoginRequest());
		return "login";
	}
	
	/** @author kk */
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String doLogin(@Validated @ModelAttribute LoginRequest loginRequest, BindingResult bindingResult, 
																		Model model, HttpSession session) {
		if (bindingResult.hasErrors()) {
			List<String> errorList = new ArrayList<String>();
            for (ObjectError error : bindingResult.getAllErrors()) {
                errorList.add(error.getDefaultMessage());
            }
			model.addAttribute("errMsg", errorList);
            model.addAttribute("logininfo", new LoginRequest());
			return "/login";
        }

		String hashedPassword = getHashedPassword(loginRequest.getLogin_pw());
		loginRequest.setLogin_pw(hashedPassword);
		
        // ユーザのログイン情報でsqlに取得
        List<UsersEntity> user_info = libraryService.login(loginRequest);
        if (user_info.isEmpty()) {
        	// もしログイン情報が間違いだったら、SQLは空
            model.addAttribute("errMsg", "社員番号もしくはパスワードが違います");
            model.addAttribute("logininfo", new LoginRequest());
            return "/login";
        }
        session.setAttribute("userId", loginRequest.getLogin_id());
        model.addAttribute("search_box", new SearchBooksRequest());
		return "/home";
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String search(Model model) {
		//		String moji = model.getAttribute("search");
		//		List<BooksEntity> bookshelf = libraryService.searchBooks();
		return "/home";
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
	
	@GetMapping(value = "/home")
	public String home(Model model) {
		List<BooksEntity> bookshelf = libraryService.displayBooks();
		model.addAttribute("search_box",new SearchBooksRequest());
		model.addAttribute("bookshelf", bookshelf);
		return "/home";
	}

	@RequestMapping(value = "/home", method = RequestMethod.POST)
	public String search(Model model, SearchBooksRequest searchBooksRequest) {

		List<BooksEntity> bookshelf = libraryService.searchBooks(searchBooksRequest);
		
		if (searchBooksRequest.getBook_name() != "") {
			model.addAttribute("condition", searchBooksRequest.getBook_name());
		}
		model.addAttribute("search_box",new SearchBooksRequest());
		model.addAttribute("bookshelf",bookshelf);
		
		return "/home";
	}

	/** @author kk */
	@GetMapping("/register")
    public String getRegisterPage(Model model) {
		model.addAttribute("userEntity", new UsersEntity());
        return "/register";
    }
	
	@GetMapping(value = "/modifyUserInfo")
	public String getModifyUserInfo(Model model) {
			return "/modifyUserInfo";
	}
	
	/** @author kk */
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public String doUserRegistration(@Validated @ModelAttribute UsersEntity usersEntity, 
												BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			List<String> errorList = new ArrayList<String>();
            for (ObjectError error : bindingResult.getAllErrors()) {
                errorList.add(error.getDefaultMessage());
            }
			model.addAttribute("errMsg", errorList);
            model.addAttribute("userEntity", new UsersEntity());
			return "/register";
        }
		usersEntity.setPassword(getHashedPassword(usersEntity.getPassword()));
		libraryService.register(usersEntity);
		
		model.addAttribute("loginRequest", new LoginRequest());
		model.addAttribute("search_box", new SearchBooksRequest());
		
		return "/home";
	}
	
	/** @author kk */
	@GetMapping("/confirm")
	public String getConfirmPage(Model model) {
		BooksEntity book = new BooksEntity();
		book.setTitle("testBook");
		book.setCategory("HAHA");
		book.setImage("THIS IS IMAGE");
		book.setLimitdate("YYYYMMDD");
		model.addAttribute("bookEntity", book);
		return "/confirm";
	}
	
	/**
	 * @author kk
	 * 
	 * Encode text into SHA256.
	 * 
	 * @param plainPassword
	 * @return
	 */
	public String getHashedPassword(String plainPassword) {
		String hashedPassword = "";
		try {
			hashedPassword = HashGenerator.generateHash(plainPassword);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hashedPassword;
	}
}