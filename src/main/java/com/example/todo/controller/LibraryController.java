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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
	public String getBorrowLogPage(@RequestParam(defaultValue = "1") int currPage, Model model) {
		int LogsSize = libraryService.getBorrowLogsSize();
		final int SUBLISTSIZE=5;
		int maxPageNum=1;
		int startIndex =(currPage - 1) * SUBLISTSIZE;
	    List<SearchLogsDTO> BorrowLogs = libraryService.displayBorrowLogs(SUBLISTSIZE,startIndex);
		if ( LogsSize%SUBLISTSIZE==0){
			maxPageNum=(int) LogsSize/SUBLISTSIZE;
		} else {
			maxPageNum=(int)(LogsSize/SUBLISTSIZE)+1;
		}
	    model.addAttribute("BorrowLogs", BorrowLogs);
	    model.addAttribute("currentPage", currPage);
	    model.addAttribute("maxPageNum", maxPageNum);
		return "/borrowlog";
	}
	/**
	 * @author Lee 
	 * 借りログ出力画面の表示
	 * 今後、user idを@paramにするmethodに変える予定
	 **/
	@GetMapping(value = "/lendlog")
	public String getLendLogPage(@RequestParam(defaultValue = "1") int currPage, Model model) {
		int LogsSize = libraryService.getLendLogsSize();
		final int SUBLISTSIZE=5;
		int startIndex =(currPage - 1) * SUBLISTSIZE;
		int maxPageNum=1;
	    List<SearchLogsDTO> LendLogs = libraryService.displayLendLogs(SUBLISTSIZE,startIndex);
		if ( LogsSize%SUBLISTSIZE==0){
			maxPageNum=(int) LogsSize/SUBLISTSIZE;
		} else {
			maxPageNum=(int)(LogsSize/SUBLISTSIZE)+1;
		}
	    model.addAttribute("LendLogs", LendLogs);
	    model.addAttribute("currentPage", currPage);
	    model.addAttribute("maxPageNum", maxPageNum);
		return "/lendlog";
		}
	/**
	 * @author Lee 
	 * 登録したマイブックの出力画面の表示
	 * 今後、user idを@paramにするmethodに変える予定
	 **/	
	@GetMapping(value = "/mybook")
	public String getmybookPage(@RequestParam(defaultValue = "1") int currPage, Model model) {
		int LogsSize = libraryService.getMyBookLogsSize();
		final int SUBLISTSIZE=5;
		int maxPageNum=1;
		int startIndex =(currPage - 1) * SUBLISTSIZE;
		List<BooksEntity> bookshelf = libraryService.displayMyBooks(SUBLISTSIZE,startIndex);
		model.addAttribute("mybook", bookshelf);
		model.addAttribute("currentPage", currPage);
		if ( LogsSize%SUBLISTSIZE==0){
			maxPageNum=(int) LogsSize/SUBLISTSIZE;
		} else {
			maxPageNum=(int)(LogsSize/SUBLISTSIZE)+1;
		}
		model.addAttribute("maxPageNum", maxPageNum);
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
		return "redirect:/home";
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
		
		return "redirect:/home";
	}
	
	/** @author kk */
	@PostMapping("/confirmPage")
	public String getConfirmPage(@RequestParam("id") String bookId,
            					 @RequestParam("title") String bookTitle,
            					 @RequestParam("image") String image, 
            					 @RequestParam("category") String category,
            					 @RequestParam("limitdate") String limitdate, Model model) {
		BooksEntity book = new BooksEntity();
		book.setCategory(category);
		book.setId(Integer.parseInt(bookId));
		book.setImage(image);
		book.setLimitdate(limitdate);
		book.setTitle(bookTitle);
		System.out.println(book);
		model.addAttribute("bookEntity", book);
		return "/confirm";
	}
	
	/** 
	 * @author kk 
	 * 
	 * Confirm borrowing and update transaction data.
	 * 
	 */
	@RequestMapping(value="/confirm", method=RequestMethod.POST)
	public String doBookConfirm(@RequestParam("id") String id,
												Model model, HttpSession session) {
		// TODO: Get each id from html
		// int borrowerId = bookEntity.getId();
		System.out.println(id);
		int borrowerId = Integer.parseInt(session.getAttribute("userId").toString());
		int lenderId   = Integer.parseInt("1");
		int bookId     = Integer.parseInt(id);
		System.out.println(bookId + " " + lenderId + " " + borrowerId);
		libraryService.updateTransaction(bookId, lenderId, borrowerId);
		return "redirect:/borrowlog";
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