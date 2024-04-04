package com.example.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.todo.forms.DongwookRequest;

@Controller
public class LibraryController {

	@GetMapping(value="/login")
	public String view_login(Model model) {
		DongwookRequest tmp = new DongwookRequest();
		model.addAttribute("kawabesan" , tmp);
		return "login";
	}
	@GetMapping(value="/register")
	public String view_register(Model model) {
		DongwookRequest tmp = new DongwookRequest();
		model.addAttribute("kawabesan" , tmp);
		return "register";
	}
	@GetMapping(value="/log")
	public String view_log(Model model) {
		DongwookRequest tmp = new DongwookRequest();
		model.addAttribute("kawabesan" , tmp);
		return "log";
	}
	@GetMapping(value="/mybook")
	public String view_mybook(Model model) {
		DongwookRequest tmp = new DongwookRequest();
		model.addAttribute("kawabesan" , tmp);
		return "mybook";
	}
	@RequestMapping(value="/kitetsu" , method=RequestMethod.POST)
	public String confirmLibrary(Model model,DongwookRequest don) {
		String dongwook = don.getSuzaki();
		model.addAttribute("kuzawa" , dongwook);
		return "confirm";
	}
}
