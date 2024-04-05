package com.example.todo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.todo.forms.BookAddRequest;
import com.example.todo.forms.DongwookRequest;

@Controller
public class LibraryController {

	@GetMapping(value="/index")
	public String view(Model model) {
		DongwookRequest tmp = new DongwookRequest();
		model.addAttribute("kawabesan" , tmp);
		return "tutorial";
	}
	
	@RequestMapping(value="/kitetsu" , method=RequestMethod.POST)
	public String confirmLibrary(Model model,DongwookRequest don) {
		String dongwook = don.getSuzaki();
		model.addAttribute("kuzawa" , dongwook);
		return "confirm";
	}
	
	
	@GetMapping(value = "/exhibit")
    public String displayAdd(Model model) {
        BookAddRequest bka = new BookAddRequest();
		model.addAttribute("bookAddRequest", bka);
        return "add";
    }
	
    
	@RequestMapping(value = "/add", method = RequestMethod.POST)
    public String exhibit(@Validated @ModelAttribute BookAddRequest bookRequest, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // 入力チェックエラーの場合
            List<String> errorList = new ArrayList<String>();
            for (ObjectError error : result.getAllErrors()) {
                errorList.add(error.getDefaultMessage());
            }
            model.addAttribute("validationError", errorList);
            return "confirm";
        }
        // ユーザー情報の登録
//        userInfoService.save(bookRequest);
        return "redirect:/user/list";
    }
}
