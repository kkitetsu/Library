package com.example.todo.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.todo.dto.NotificationDTO;
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

	@RequestMapping(value = "/borrowlog", method = { RequestMethod.GET, RequestMethod.POST })
	public String getBorrowLogPage(@RequestParam(defaultValue = "1") int currPage, Model model, HttpSession session) {
		int userId = Integer.parseInt(session.getAttribute("userId").toString());
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
		model.addAttribute("LogsSize", LogsSize);
		if (LogsSize == 0) {
			model.addAttribute("errMsg", "表示する項目がありませんでした。");
		}
		return "/borrowlog";
	}

	/**
	 * @author Lee 
	 * 借りログ出力画面の表示
	 * 今後、user idを@paramにするmethodに変える予定
	 **/
	@RequestMapping(value = "/lendlog", method = { RequestMethod.GET, RequestMethod.POST })
	public String getLendLogPage(@RequestParam(defaultValue = "1") int currPage, Model model, HttpSession session) {
		int userId = Integer.parseInt(session.getAttribute("userId").toString());
		int LogsSize = libraryService.getLendLogsSize(userId);
		final int SUBLISTSIZE = 5;
		int startIndex = (currPage - 1) * SUBLISTSIZE;

		int maxPageNum = 1;
		List<SearchLogsDTO> LendLogs = libraryService.displayLendLogs(SUBLISTSIZE, startIndex, userId);
		if (LogsSize % SUBLISTSIZE == 0) {
			maxPageNum = (int) LogsSize / SUBLISTSIZE;
		} else {
			maxPageNum = (int) (LogsSize / SUBLISTSIZE) + 1;
		}
		model.addAttribute("LendLogs", LendLogs);
		model.addAttribute("currentPage", currPage);
		model.addAttribute("maxPageNum", maxPageNum);
		model.addAttribute("LogsSize", LogsSize);
		if (LogsSize == 0) {
			model.addAttribute("errMsg", "表示する項目がありませんでした。");
		}

		return "/lendlog";
	}

	/**
	 * @author Lee 
	 * 登録したマイブックの出力画面の表示
	 * 今後、user idを@paramにするmethodに変える予定
	 **/
	@RequestMapping(value = "/mybook", method = { RequestMethod.GET, RequestMethod.POST })
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
		model.addAttribute("LogsSize", LogsSize);
		if (LogsSize == 0) {
			model.addAttribute("errMsg", "表示する項目がありませんでした。");
		}
		return "/mybook";
	}

	/**
	 * @author Lee 
	 * 借りログ出力画面の表示
	 * 今後、user idを@paramにするmethodに変える予定
	 **/
	@GetMapping(value = "/edituser")
	public String geteditUserPage(Model model, HttpSession session) {
		// Edited by kk. Display user ID on the page
		UsersEntity user = new UsersEntity();
		user.setLoginId(
				libraryService.getLoginIdBasedOnId(Integer.parseInt(session.getAttribute("userId").toString())));
		model.addAttribute("userEntity", user);
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
			/**
			 * @author shunsukekuzawa
			 * セッションスコープに保存されている本の情報をクリア
			 */
			DeleteSession(session);
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
	 * @param model
	 * @return URL of home.html
	 */
	@GetMapping(value = "/home")
	public String home(@RequestParam(defaultValue = "1") int currPage, Model model,
			@ModelAttribute("alertMessage") String alertMessage, HttpSession session) {
		model.addAttribute("condition", model.getAttribute("alertMessage"));

		if (session.getAttribute("userId") == null) {
			return "redirect:/login";
		}
		//ユーザーID：セッション取得
		int user_id = (int) session.getAttribute("userId");

		//お知らせ取得：貸出要請
		List<NotificationDTO> lend_notification = libraryService.LendNotification(user_id);
		lend_notification.forEach(
				e -> e.setMessage(e.getNotificationDate() + " : 【" + e.getBorrowerName() + "】様から【" + e.getBookTitle()
						+ "】の貸出要請がありました"));

		//お知らせ取得：期限
		List<NotificationDTO> limit_notification = libraryService.LimitNotification(user_id);
		limit_notification.forEach(
				e -> e.setMessage(e.getNotificationDate() + " : 【" + e.getLenderName() + "】様の【" + e.getBookTitle()
						+ "】の貸出期限まで１週間になりました"));

		//お知らせ合体
		List<NotificationDTO> ntf = new ArrayList<NotificationDTO>();
		ntf.addAll(limit_notification);
		ntf.addAll(lend_notification);

		//お知らせ時系列順に並べ替え
		Collections.sort(
				ntf, new Comparator<NotificationDTO>() {
					@Override
					public int compare(NotificationDTO obj1, NotificationDTO obj2) {
						return obj2.getNotificationDate().compareTo(obj1.getNotificationDate());
					}
				});

		//お知らせを表示
		if (ntf.size() == 0) {
			session.setAttribute("notification", null);
		} else {
			session.setAttribute("notification", ntf);
		}

		if (Objects.equals(session.getAttribute("category"), "lenduser")) {
			session.setAttribute("category_type", "出品者");
		} else if (Objects.equals(session.getAttribute("category"), "title")) {
			session.setAttribute("category_type", "タイトル");
		} else if (Objects.equals(session.getAttribute("category"), "content")) {
			session.setAttribute("category_type", "コンテンツ");
		} else {
			session.setAttribute("category_type", "タイトル");
		}

		int maxPageNum;
		
		//本の全リストを取得：ログイン時、本追加・修正からリダイレクトした時に実行
		if (session.getAttribute("bookshelf") == null) {
			List<BooksEntity> bookshelf = libraryService.displayBooks();
			session.setAttribute("condition", null);
			session.setAttribute("bookshelf", bookshelf);
			// Editor : Lee
			// 本が何冊か計算します。
			int bookCount = bookshelf.size();
			session.setAttribute("bookCount", bookCount);
			maxPageNum = bookCount / 20;
			if (bookCount % 20 > 0) {
				maxPageNum++;
			}
			session.setAttribute("maxPageNum", maxPageNum);
		}
		model.addAttribute("currentPage", currPage);
		//検索フィールド
		SearchBooksRequest newBookRequest = new SearchBooksRequest();
		newBookRequest.setBook_name((String) session.getAttribute("search_name"));

		model.addAttribute("search_box", newBookRequest);

		// Editor: kk
		// Record and show user's name
		model.addAttribute("userName", session.getAttribute("userName"));
		return "/home";
	}

	/**
	 * @author shunsukekuzawa
	 * Search books by keyword
	 * @param model
	 * @param searchBooksRequest
	 * @param session
	 * @param category
	 * @return
	 */
	@RequestMapping(value = "/home", params = "search", method = RequestMethod.POST)
	public String search(Model model, SearchBooksRequest searchBooksRequest, HttpSession session,
			@RequestParam("category") String category) {

		//カテゴリの指定がなければ、前回選択したカテゴリを引き継ぎ
		List<BooksEntity> bookshelf = new ArrayList<BooksEntity>();
		if (category != "") {
			session.setAttribute("category", category);
		}

		if (Objects.equals(session.getAttribute("category"), "title")) {
			bookshelf = libraryService.searchBooksByTitle(searchBooksRequest);
		} else if (Objects.equals(session.getAttribute("category"), "content")) {
			bookshelf = libraryService.searchBooksByContent(searchBooksRequest);
		} else if (Objects.equals(session.getAttribute("category"), "lenduser")) {
			bookshelf = libraryService.searchBooksByUser(searchBooksRequest);
		} else {
			bookshelf = libraryService.searchBooksByTitle(searchBooksRequest);
		}

		if (bookshelf.isEmpty()) {
			// Added by kk. If serach result is empty, display "no search result"
			session.setAttribute("condition", "検索結果がありません");
		} else {
			session.setAttribute("condition", null);
		}

		// Added by kk. Used to store the search history
		session.setAttribute("search_name", searchBooksRequest.getBook_name());

		session.setAttribute("bookshelf", bookshelf);
		session.setAttribute("category", session.getAttribute("category"));
		// Editor: kk
		// Record and show user's name
		session.setAttribute("userName", session.getAttribute("userName"));

		// Editor: Lee
		// 本が何冊か計算します。
		int bookCount = bookshelf.size();
		session.setAttribute("bookCount", bookCount);
		int maxPageNum = bookCount / 20;
		if (bookCount % 20 > 0) {
			maxPageNum++;
		}
		session.setAttribute("maxPageNum", maxPageNum);

		return "redirect:/home";
	}

	/**
	 * @author shunsukekuzawa
	 * read the notifications(お知らせの既読機能)
	 * @param model
	 * @param searchBooksRequest
	 * @param session
	 * @param note
	 * @return
	 */
	@RequestMapping(value = "/home", params = "note", method = RequestMethod.POST)
	public String note(Model model, SearchBooksRequest searchBooksRequest, HttpSession session,
			@RequestParam("note") String[] note) {

		for (int i = 0; i < note.length - 1; i++) {
			libraryService.confirmBorrowerNotification(Integer.parseInt(note[i]), (int) session.getAttribute("userId"));
			libraryService.confirmLenderNotification(Integer.parseInt(note[i]), (int) session.getAttribute("userId"));
		}

		return "redirect:/home";
	}

	/**
	 * @author shunsukekuzawa
	 * 表示する本の情報を初期化
	 * @param session
	 */
	private void DeleteSession(HttpSession session) {
		session.setAttribute("bookshelf", null);
		session.setAttribute("search_name", null);
		session.setAttribute("category", null);
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
	public String displayeditbook(Model model, @RequestParam("id") int id, @RequestParam("title") String title,
			@RequestParam("category") String category, 
			@RequestParam("limitdate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate limitdate,
			@RequestParam("image") String image) {
		BookAddRequest bka = new BookAddRequest();
		bka.setId(id);
		bka.setTitle(title);
		bka.setCategory(category);
		bka.setLimitdate(limitdate);
		bka.setImage(image);
		model.addAttribute("bookAddRequest", bka);
		return "/editbook";
	}
	
	
	/**@author Aru*/
	
	@RequestMapping(value = "/editbook", params= "update", method = RequestMethod.POST)
    public String editBook(@Validated @ModelAttribute BookAddRequest bookRequest, BindingResult bindingResult, Model model, HttpSession session) {		
			if (bindingResult.hasErrors()) {
            		List<String> errorList = new ArrayList<String>();
            		for (ObjectError error : bindingResult.getAllErrors()) {
            			errorList.add(error.getDefaultMessage());
            		}
            		model.addAttribute("validationError", errorList);
        			model.addAttribute("bookAddRequest", bookRequest);
        			return "/editbook";

			 }
			List<MultipartFile> multipartFile = bookRequest.getMultipartFile();
			multipartFile.forEach(e -> {
				String e_name = e.getOriginalFilename();
				if (e_name == "") {
					bookRequest.setImgPath(bookRequest.getImage());
				} else {
					bookRequest.setImgPath(uploadAction(e));
				}
	
			});
			libraryService.bookEditer(bookRequest);
			
			try {
				Thread.sleep(7000);
			} catch (InterruptedException e) {
				System.out.println("待ち時間中に割り込みが発生しました。");
			}
			
			DeleteSession(session);
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
		System.out.println("Here");
		session.setAttribute("userId", 1);
		bookRequest.setUserId((int) session.getAttribute("userId"));
		if (bindingResult.hasErrors()) {
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

		libraryService.bookRegister(bookRequest);

		try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			System.out.println("待ち時間中に割り込みが発生しました。");
		}

		System.out.println(session.getAttribute("userId"));
		System.out.println(session.getAttribute("userName"));

		DeleteSession(session);
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
			byte[] bytes = multipartFile.getBytes();

			//バイト値を書き込む為のファイルを作成して指定したパスに格納
			OutputStream stream = Files.newOutputStream(filePath);
			//ファイルに書き込み
			stream.write(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String path = filePath.toString();
		return "/uploadImage/" + fileName;
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

		List<UsersEntity> users = libraryService.getUsers();
		for (UsersEntity eachUser : users) {
			if (eachUser.getLoginId().equals(usersEntity.getLoginId())) {
				model.addAttribute("errMsg", "このIDはすでに存在しています");
				model.addAttribute("userEntity", new UsersEntity());
				return "/register";
			}
		}

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
            					 @RequestParam("exhibitorId") String exhibitorId, 
            					 Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		

		if (exhibitorId.equals(session.getAttribute("userId").toString())) {
			// User cannot borrow his or her own book
			redirectAttributes.addFlashAttribute("alertMessage", "自分の本は借りる・もらうことができません");
			return "redirect:/home";
		}
		BooksEntity book = new BooksEntity();
		book.setCategory(category);
		book.setId(Integer.parseInt(bookId));
		book.setImage(image);
		book.setLimitdate(limitdate);
		book.setTitle(bookTitle);
		book.setExhibitorUserId(Integer.parseInt(exhibitorId));
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
	@RequestMapping(value = "/confirm", method = RequestMethod.POST)
	public String doBookConfirm(@RequestParam("id") String id,
			@RequestParam("exhibitorId") String exhibitorId,
			Model model, HttpSession session) {
		int bookId = Integer.parseInt(id);
		int lenderId = Integer.parseInt(exhibitorId);
		int borrowerId = Integer.parseInt(session.getAttribute("userId").toString());
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
	public String doLogOut(Model model, HttpSession session) {
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