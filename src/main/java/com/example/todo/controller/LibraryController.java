package com.example.todo.controller;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import org.springframework.web.multipart.MultipartFile;

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
	
	@GetMapping(value = "/log")
	public String getLogPage(Model model) {
		return "/log";
	}

	/**
	 * @author Lee 
	 * 貸しログ出力画面の表示
	 * 今後、user idを@paramにするmethodに変える予定
	 **/
	// @RequestMapping(value = "/borrowlog", method = RequestMethod.POST) edited kk
	@RequestMapping(value = "/borrowlog", method={RequestMethod.GET, RequestMethod.POST})
	public String getBorrowLogPage(@RequestParam(defaultValue = "1") int currPage, Model model, HttpSession session) {
		int userId= Integer.parseInt(session.getAttribute("userId").toString());
		int LogsSize = libraryService.getBorrowLogsSize(userId);
		final int SUBLISTSIZE = 5;
		int maxPageNum = 1;
		int startIndex = (currPage - 1) * SUBLISTSIZE;
		List<SearchLogsDTO> BorrowLogs = libraryService.displayBorrowLogs(SUBLISTSIZE, startIndex, userId);
		if (LogsSize % SUBLISTSIZE == 0) {
			maxPageNum = (int) LogsSize / SUBLISTSIZE;
		} else {
			maxPageNum = (int) (LogsSize / SUBLISTSIZE) + 1;
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
	@RequestMapping(value = "/lendlog", method = {RequestMethod.GET, RequestMethod.POST})
	public String getLendLogPage(@RequestParam(defaultValue = "1") int currPage, Model model, HttpSession session) {
		int userId =Integer.parseInt(session.getAttribute("userId").toString());
		int LogsSize = libraryService.getLendLogsSize(userId);
		final int SUBLISTSIZE = 5;
		int startIndex = (currPage - 1) * SUBLISTSIZE;
		
		int maxPageNum = 1;
		List<SearchLogsDTO> LendLogs = libraryService.displayLendLogs(SUBLISTSIZE, startIndex,userId);
		if (LogsSize % SUBLISTSIZE == 0) {
			maxPageNum = (int) LogsSize / SUBLISTSIZE;
		} else {
			maxPageNum = (int) (LogsSize / SUBLISTSIZE) + 1;
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

	
	@RequestMapping(value = "/mybook", method = {RequestMethod.GET, RequestMethod.POST})
	public String getmybookPage(@RequestParam(defaultValue = "1") int currPage, Model model, HttpSession session) {
		int userId = Integer.parseInt(session.getAttribute("userId").toString());
		int LogsSize = libraryService.getMyBookLogsSize(userId);
		final int SUBLISTSIZE = 5;
		int maxPageNum = 1;
		int startIndex = (currPage - 1) * SUBLISTSIZE;
		List<BooksEntity> bookshelf = libraryService.displayMyBooks(SUBLISTSIZE, startIndex, userId);
		model.addAttribute("mybook", bookshelf);
		model.addAttribute("currentPage", currPage);
		if (LogsSize % SUBLISTSIZE == 0) {
			maxPageNum = (int) LogsSize / SUBLISTSIZE;
		} else {
			maxPageNum = (int) (LogsSize / SUBLISTSIZE) + 1;
		}
		model.addAttribute("maxPageNum", maxPageNum);
		return "/mybook";
	}

	/**
	 * @author Lee 
	 * 借りログ出力画面の表示
	 * 今後、user idを@paramにするmethodに変える予定
	 **/
	@GetMapping(value = "/edituser")
	public String geteditUserPage(Model model) {
		model.addAttribute("userEntity", new UsersEntity());
		return "/edituserInfo";
	}

	/**
	 * @author Lee 
	 * ユーザーの情報を更新するmethod
	 **/
	@RequestMapping(value = "/edituser", method = RequestMethod.POST)
	public String EditUserInfo(@Validated @ModelAttribute UsersEntity usersEntity,
			BindingResult bindingResult, Model model, HttpSession session) {
		if (bindingResult.hasErrors()) {
			List<String> errorList = new ArrayList<String>();
			for (ObjectError error : bindingResult.getAllErrors()) {
				errorList.add(error.getDefaultMessage());
			}
			model.addAttribute("errMsg", errorList);
			model.addAttribute("userEntity", new UsersEntity());
			return "/edituserInfo";
		}
		usersEntity.setPassword(getHashedPassword(usersEntity.getPassword()));
		usersEntity.setId(Integer.parseInt(session.getAttribute("userId").toString()));
		System.out.println(usersEntity);
		libraryService.editUser(usersEntity);
		model.addAttribute("loginRequest", new LoginRequest());
		model.addAttribute("search_box", new SearchBooksRequest());
		return "/login";
	}

	/** @author kk */
	@GetMapping(value = "/login")
	public String getLoginPage(Model model) {
		model.addAttribute("loginRequest", new LoginRequest());
		return "login";
	}

	/** @author kk */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
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
		session.setAttribute("userId", user_info.get(0).getId());
		session.setAttribute("userName", user_info.get(0).getName());
		return "redirect:/home";
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
	public String home(Model model,HttpSession session) {
		
		if (session.getAttribute("userId") == null) {
			return "redirect:/login";
		}
		
		int user_id = (int)session.getAttribute("userId");
		List<SearchLogsDTO> ntf = libraryService.displayNotification(user_id);
		
		
		
		if(ntf.size() == 0) {
			SearchLogsDTO none_message = new SearchLogsDTO();
			none_message.setComment("現在、お知らせはありません");
			ntf.add(none_message);
		}
		
		//お知らせを表示
		session.setAttribute("notification", ntf);

		//本のリストを取得
		List<BooksEntity> bookshelf = libraryService.displayBooks();

		//検索フィールド
		model.addAttribute("search_box", new SearchBooksRequest());

		//本のリストを格納
		model.addAttribute("bookshelf", bookshelf);
		
		// Editor: kk
		// Record and show user's name
		model.addAttribute("userName", session.getAttribute("userName"));

		return "/home";
	}
	
	@GetMapping(value = "/exhibit")
	public String displayAdd(Model model, HttpSession session) {
		if (session.getAttribute("userId") == null) {
			return "redirect:/login";
		}
		BookAddRequest bka = new BookAddRequest();
		model.addAttribute("bookAddRequest", bka);
        return "/add";
    }
	/**
	 * @author Lee 
	 * 本の修正への遷移経路
	 **/
	@GetMapping(value = "/editbook")
	public String displayeditbook(Model model, @RequestParam("id") int id) {
		BookAddRequest bka = new BookAddRequest();
		bka.setId(id);
		model.addAttribute("bookAddRequest", bka);
        return "/editbook";
    }

	
	
	@RequestMapping(value = "/editbook", params= "update", method = RequestMethod.POST)
    public String editBook(@Validated @ModelAttribute BookAddRequest bookRequest, BindingResult bindingResult, Model model, HttpSession session) {		
			if (bindingResult.hasErrors()) {
            		List<String> errorList = new ArrayList<String>();
            		for (ObjectError error : bindingResult.getAllErrors()) {
            			errorList.add(error.getDefaultMessage());
            		}
            		model.addAttribute("validationError", errorList);
            		model.addAttribute("bookAddRequest", new BookAddRequest());
            		return "/editbook";
			}
			List<MultipartFile> multipartFile = bookRequest.getMultipartFile();
			multipartFile.forEach(e -> {
				bookRequest.setImgPath(uploadAction(e));
			});
			if (bookRequest.getLimitdate() == null) {
				String errorMsg = "やってくれたな";
				model.addAttribute("errorMsg", errorMsg);
				return "/editbook";
			}
			libraryService.bookEditer(bookRequest);
	   return "redirect:/home";        
    }
	
	/**
	 * @author Aru
	 * 
	 * Delete a book (Remove from book list, change exhibition flag to 0)
	 * 
	 * @return
	 */
	@RequestMapping(value = "/editbook", params= "delete", method = RequestMethod.POST)
    public String deleteBook(@Validated @ModelAttribute BookAddRequest bookRequest, BindingResult bindingResult, 
    											Model model, HttpSession session) {		

		libraryService.bookDeliter(bookRequest);
		
		return "redirect:/home";        
    }
	
	
	@RequestMapping(value = "/exhibit", method = RequestMethod.POST)
    public String exhibit(@Validated @ModelAttribute BookAddRequest bookRequest, BindingResult bindingResult, Model model, HttpSession session) {
        System.out.println("session is: " + session.getAttribute("userId"));
        bookRequest.setUserId((int)session.getAttribute("userId"));
		if (bindingResult.hasErrors()) {
            // 入力チェックエラーの場合
            List<String> errorList = new ArrayList<String>();
            for (ObjectError error : bindingResult.getAllErrors()) {
                errorList.add(error.getDefaultMessage());
            }
            model.addAttribute("validationError", errorList);
            model.addAttribute("bookAddRequest", new BookAddRequest());
            return "/add";
        }
        List<MultipartFile> multipartFile = bookRequest.getMultipartFile();
   	    multipartFile.forEach(e -> {
          bookRequest.setImgPath(uploadAction(e));
        });
   	    if (bookRequest.getLimitdate() == null) {
   	    	// do something
   	    	String errorMsg = "やってくれたな";
   	    	model.addAttribute("errorMsg", errorMsg);
   	    	return "/add";
   	    }
	   libraryService.bookRegister(bookRequest);
	   
	   try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			System.out.println("待ち時間中に割り込みが発生しました。");
		}
	   
	   System.out.println(session.getAttribute("userId"));
	   System.out.println(session.getAttribute("userName"));
	   

	   return "redirect:/home";        
    }
	
	/**
     * アップロード実行処理
     * @param multipartFile
     */
    private String uploadAction(MultipartFile multipartFile) {
        //ファイル名取得
        String fileName = multipartFile.getOriginalFilename();
        
    	//p1 : uploadImageフォルダへの相対パス
		java.nio.file.Path p1 = Paths.get("src/main/resources/static/uploadImage/"); 
		//p2 : 相対パス→絶対パスに変換
        java.nio.file.Path p2 = p1.toAbsolutePath();
        //filePath : fileNameをパスに追加
        java.nio.file.Path filePath = Paths.get(p2.toString() + "/" + fileName);

        System.out.println(filePath.toString()); 
        
        try {
            //アップロードファイルをバイト値に変換
            byte[] bytes  = multipartFile.getBytes();

            //バイト値を書き込む為のファイルを作成して指定したパスに格納
            OutputStream stream = Files.newOutputStream(filePath);
            //ファイルに書き込み
            stream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String path = filePath.toString();
        return "/uploadImage/"+fileName;
    }



	@RequestMapping(value = "/home", method = RequestMethod.POST)
	public String search(Model model, SearchBooksRequest searchBooksRequest, HttpSession session) {

		List<BooksEntity> bookshelf = libraryService.searchBooks(searchBooksRequest);

		if (searchBooksRequest.getBook_name() != "") {
			model.addAttribute("condition", searchBooksRequest.getBook_name());
		}
		model.addAttribute("search_box", new SearchBooksRequest());
		model.addAttribute("bookshelf", bookshelf);
		
		// Editor: kk
		// Record and show user's name
		model.addAttribute("userName", session.getAttribute("userName"));

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
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String doUserRegistration(@Validated @ModelAttribute UsersEntity usersEntity,
			BindingResult bindingResult, Model model, HttpSession session) {
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
		session.setAttribute("userId", libraryService.getLastIdInUsers());
		session.setAttribute("userName", usersEntity.getName());

		return "redirect:/home";
	}

	/** @author kk */
	@PostMapping("/confirmPage")
	public String getConfirmPage(@RequestParam("id") String bookId,
            					 @RequestParam("title") String bookTitle,
            					 @RequestParam("image") String image, 
            					 @RequestParam("category") String category,
            					 @RequestParam("limitdate") String limitdate, 
            					 @RequestParam("exhibitor") String exhibitor, Model model) {
		BooksEntity book = new BooksEntity();
		book.setCategory(category);
		book.setId(Integer.parseInt(bookId));
		book.setImage(image);
		book.setLimitdate(limitdate);
		book.setTitle(bookTitle);
		book.setExhibitorUserId(Integer.parseInt(exhibitor));
		model.addAttribute("bookEntity", book);
		String exhibitorName = libraryService.getNameBasedOnId(book.getExhibitorUserId());
		model.addAttribute("exhibitorName", exhibitorName);
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
								@RequestParam("exhibitorId") String exhibitorId,
								Model model, HttpSession session) {
		int bookId     = Integer.parseInt(id);
		int lenderId   = Integer.parseInt(exhibitorId);
		int borrowerId = Integer.parseInt(session.getAttribute("userId").toString());
		System.out.println(bookId + " " + lenderId + " " + borrowerId);
		libraryService.updateTransaction(bookId, lenderId, borrowerId);
		libraryService.updateBooksNoLongerExhibit(bookId);
		return "redirect:/borrowlog";
	}
	/**
	 * @author Lee
	 * ログアウトしてlogin画面移戻る。
	 * @param session
	 * @return
	 */
	@PostMapping("/logout")
	public String doLogOut( Model model, HttpSession session) {
		session.invalidate(); // Logout and back to home
		return "redirect:/login";
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