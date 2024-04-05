package com.example.todo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.todo.DTO.SearchLogsDTO;
import com.example.todo.entity.BooksEntity;
import com.example.todo.entity.TransactionEntity;
import com.example.todo.entity.UsersEntity;
import com.example.todo.forms.LoginRequest;
import com.example.todo.forms.SearchBooksRequest;

@Mapper
public interface LibraryMapper {

	public List<BooksEntity> displayBooks();
	
	public List<BooksEntity> searchBooks(SearchBooksRequest searchBooksRequest);
	
	public List<UsersEntity> login(LoginRequest loginRequest);

	public List<TransactionEntity> displayLogs();

	public List<SearchLogsDTO> displayBorrowLogs();
	
	public List<SearchLogsDTO> displayLendLogs();
}
