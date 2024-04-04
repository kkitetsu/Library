package com.example.todo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.todo.entity.BooksEntity;
import com.example.todo.entity.UsersEntity;
import com.example.todo.forms.LoginRequest;
import com.example.todo.mapper.LibraryMapper;

@Service
public class LibraryService {
	
	@Autowired
	private LibraryMapper libraryMapper;
	
	public List<UsersEntity> login(LoginRequest loginRequest) {
		return libraryMapper.login(loginRequest);
	}
	
	/**
	 * @author shunsukekuzawa
	 * 
	 * Search all books list for displaying them at /home .
	 * 
	 * @return List for books
	 */
	public List<BooksEntity> displayBooks(){
		return libraryMapper.displayBooks();
		
	}
	
	public List<BooksEntity> searchBooks(){
		return  libraryMapper.searchBooks();
	}
}
