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
	public String view(Model model) {
		DongwookRequest tmp = new DongwookRequest();
		model.addAttribute("kawabesan" , tmp);
		return "login";
	}
	@GetMapping(value="/register")
	public String view1(Model model) {
		DongwookRequest tmp = new DongwookRequest();
		model.addAttribute("kawabesan" , tmp);
		return "register";
	}
	@RequestMapping(value="/kitetsu" , method=RequestMethod.POST)
	public String confirmLibrary(Model model,DongwookRequest don) {
		String dongwook = don.getSuzaki();
		model.addAttribute("kuzawa" , dongwook);
		return "confirm";
	}
}
