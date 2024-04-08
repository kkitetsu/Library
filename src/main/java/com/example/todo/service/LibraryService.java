package com.example.todo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.todo.dto.SearchLogsDTO;
import com.example.todo.entity.BooksEntity;
import com.example.todo.entity.TransactionEntity;
import com.example.todo.entity.UsersEntity;
import com.example.todo.forms.LoginRequest;
import com.example.todo.forms.SearchBooksRequest;
import com.example.todo.mapper.LibraryMapper;

@Service
public class LibraryService {

	@Autowired
	private LibraryMapper libraryMapper;

	/** @author kk */
	public List<UsersEntity> login(LoginRequest loginRequest) {
		return libraryMapper.login(loginRequest);
	}

	/** @author kk */
	public void register(UsersEntity usersEntity) {
		libraryMapper.register(usersEntity);
	}

	/**
	 * @author shunsukekuzawa
	 * 
	 * Search all books list for displaying them at /home .
	 * 
	 * @return List for books
	 */
	public List<BooksEntity> displayBooks() {
		return libraryMapper.displayBooks();
	}

	/** 
	 * @author Lee
	 * 貸し借りの全履歴(idベース)
	 * @returnList
	 * id(取引id),
	 * book_id(貸し借りた本のid),
	 * lender_user_id(貸し手のユーザーID)
	 * lender_borrower_user_id(貸し手のユーザーID)
	 * tradede_date(貸し借り日付)
	 **/
	public List<TransactionEntity> displayLogs() {
		return libraryMapper.displayLogs();
	}
	
	public List<BooksEntity> searchBooks(SearchBooksRequest searchBooksRequest) {
		return libraryMapper.searchBooks(searchBooksRequest);
	}

	/** 
	 * @author Lee
	 * 「借り」の全履歴
	 * @returnList
	 * TransId（取引ID）
	 * TradedDate(取引日付)
	 * BookTitle（本のタイトル） 
	 * LenderName（貸し手の名前）
	 * LimitDate（返却期限）
	 * BorrowerId（借り手のID）
	 **/
	public List<SearchLogsDTO> displayBorrowLogs() {
		return libraryMapper.displayBorrowLogs();
	}

	/** 
	 * @author Lee
	 * 「貸し」の全履歴
	 * @returnList
	 * TransId（取引ID）
	 * TradedDate(取引日付)
	 * BookTitle（本のタイトル） 
	 * BorrowerName（借り手の名前）
	 * LimitDate（返却期限）
	 * LenderId（貸し手のID）
	 **/
	public List<SearchLogsDTO> displayLendLogs(final int SUBLISTSIZE, int startIndex) {
		return libraryMapper.displayLendLogs(SUBLISTSIZE, startIndex);
	}
	/** 
	 * @author Lee
	 * 「貸し」の履歴数を返却
	 * @return Logsize
	 **/
	public int getLendLogsSize() {
		return libraryMapper.getLendLogsSize();
	}
}
