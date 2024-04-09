package com.example.todo.controller;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	public String doLogin(Model model, HttpSession session, 
		@ModelAttribute LoginRequest loginRequest) {
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
	
    
	@RequestMapping(value = "/exhibit", method = RequestMethod.POST)
    public String exhibit(@Validated @ModelAttribute BookAddRequest bookRequest, Model model, HttpSession session) {
        session.setAttribute("userId", 1);
        bookRequest.setUserId((int)session.getAttribute("userId"));
//		if (result.hasErrors()) {
//            // 入力チェックエラーの場合
//            List<String> errorList = new ArrayList<String>();
//            for (ObjectError error : result.getAllErrors()) {
//                errorList.add(error.getDefaultMessage());
//            }
//            model.addAttribute("validationError", errorList);
//            return "/exhibit";
//        }
        List<MultipartFile> multipartFile = bookRequest.getMultipartFile();
   	    multipartFile.forEach(e -> {
            //アップロード実行処理メソッド呼び出し
          bookRequest.setImgPath(uploadAction(e));
        });
	   libraryService.bookRegister(bookRequest);
       model.addAttribute("search_box", new SearchBooksRequest());
       List<BooksEntity> bookshelf = libraryService.displayBooks();
	   model.addAttribute("bookshelf", bookshelf);
	   return "/home";
        
    }
	
	/**
     * アップロード実行処理
     * @param multipartFile
     */
    private String uploadAction(MultipartFile multipartFile) {
        //ファイル名取得
        String fileName = multipartFile.getOriginalFilename();

        //格納先のフルパス ※事前に格納先フォルダ「UploadTest」をCドライブ直下に作成しておく
        java.nio.file.Path filePath = Paths.get("C:/pleiades/2023-12/workspace/Library/src/main/resources/static/uploadImage/" + fileName);
        
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
	public String doUserRegistration(Model model, @ModelAttribute UsersEntity usersEntity) {
		usersEntity.setPassword(getHashedPassword(usersEntity.getPassword()));
		libraryService.register(usersEntity);
		
		model.addAttribute("loginRequest", new LoginRequest());
		model.addAttribute("search_box", new SearchBooksRequest());
		
		return "/home";
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