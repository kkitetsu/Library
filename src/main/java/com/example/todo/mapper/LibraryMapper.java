package com.example.todo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.todo.dto.SearchLogsDTO;
import com.example.todo.entity.BooksEntity;
import com.example.todo.entity.TransactionEntity;
import com.example.todo.entity.UsersEntity;
import com.example.todo.forms.LoginRequest;
import com.example.todo.forms.SearchBooksRequest;

@Mapper
public interface LibraryMapper {

	public List<BooksEntity> displayBooks();
	
	public List<BooksEntity> searchBooks(SearchBooksRequest searchBooksRequest);
	
	/** @author kk */
	public List<UsersEntity> login(LoginRequest loginRequest);

	
	/** 
	 * @author Lee 
	 * @param transaction リクエストデータ 
	 * @return トランザクションの一覧(ID表記)
	 **/
	public List<TransactionEntity> displayLogs();

	/** 
	 * @author Lee 
	 * @param 借りた履歴リクエストデータ(今後 ユーザーID　主キーに修正予定)
	 * @return 借りた履歴結果
	 **/
	public List<SearchLogsDTO> displayBorrowLogs();
	
	/**
	 * @author Lee 
	 * @param 貸した履歴リクエストデータ(今後 ユーザーID　主キーに修正予定)
	 * @return 貸した履歴結果
	 **/
	public List<SearchLogsDTO> displayLendLogs();

	/** @author kk */
	public void register(UsersEntity usersEntity);
}
