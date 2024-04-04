package com.example.todo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.todo.entity.BooksEntity;
import com.example.todo.forms.DongwookRequest;
import com.example.todo.service.LibraryService;

@Controller
public class LibraryController {

	@Autowired
	private LibraryService libraryService;
	
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
	
	@GetMapping(value="/home")
	public String home(Model model) { 
		
		List<BooksEntity> bookshelf = libraryService.displayBooks();
		
		model.addAttribute("bookshelf",bookshelf);
		
		return "/home";
	}
	
	@RequestMapping(value="/search" , method=RequestMethod.POST)
	public String search(Model model) {
		
//		String moji = model.getAttribute("search");
//		
//		List<BooksEntity> bookshelf = libraryService.searchBooks();
		
		return "/home";
	}
}
