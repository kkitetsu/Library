package com.example.todo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.todo.entity.BooksEntity;
import com.example.todo.entity.UsersEntity;
import com.example.todo.forms.LoginRequest;

@Mapper
public interface LibraryMapper {

	public List<BooksEntity> displayBooks();
	
	public List<BooksEntity> searchBooks();
	
	public List<UsersEntity> login(LoginRequest loginRequest);
	
	public List<UsersEntity> add(LoginRequest loginRequest);
	
}
