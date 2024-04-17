package com.example.todo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.todo.dto.NotificationDTO;
import com.example.todo.dto.SearchLogsDTO;
import com.example.todo.entity.BooksEntity;
import com.example.todo.entity.TransactionEntity;
import com.example.todo.entity.UsersEntity;
import com.example.todo.forms.BookAddRequest;
import com.example.todo.forms.LoginRequest;
import com.example.todo.forms.SearchBooksRequest;
import com.example.todo.mapper.LibraryMapper;

@Service
public class LibraryService {

	@Autowired
	private LibraryMapper libraryMapper;
	
	/** @author kk */
	public List<UsersEntity> getUsers() {
		return libraryMapper.getUsers();
	}

	/** @author kk */
	public List<UsersEntity> login(LoginRequest loginRequest) {
		return libraryMapper.login(loginRequest);
	}

	/** @author kk */
	public void register(UsersEntity usersEntity) {
		libraryMapper.register(usersEntity);
	}
	
	/** 
	 * @author kk 
	 * @param bookId 
	 * @param lenderId 
	 * @param borrowerId
	 */
	public void updateTransaction(int bookId, int lenderId, int borrowerId) {
		libraryMapper.updateTransaction(bookId, lenderId, borrowerId);
	}

	/**
	 * @author kk
	 */
	public String getNameBasedOnId(int id) {
		return libraryMapper.getNameBasedOnId(id);
	}
	
	/** @author kk */
	public void updateBooksNoLongerExhibit(int bookId) {
		libraryMapper.updateBooksNoLongerExhibit(bookId);
	}
	
	/** @author kk */
	public Integer getLoginIdBasedOnId(int id) {
		return libraryMapper.getLoginIdBasedOnId(id);
	}
	
	/** 
	 * @author Lee
	 * Userの情報修正
	 **/
	public void editUser(UsersEntity usersEntity) {
		libraryMapper.editUser(usersEntity);
	}
	
	public List<BooksEntity> displayBooks(){
		return libraryMapper.displayBooks();
	}
	
	/**
	 * @author shunsukekuzawa
	 * 
	 * Select all book-info for display.
	 * 
	 * @return List for books
	 */
	public List<BooksEntity> searchBooksByTitle(SearchBooksRequest searchBooksRequest) {
		return libraryMapper.searchBooksByTitle(searchBooksRequest);
	}
	public List<BooksEntity> searchBooksByContent(SearchBooksRequest searchBooksRequest) {
		return libraryMapper.searchBooksByContent(searchBooksRequest);
	}
	public List<BooksEntity> searchBooksByUser(SearchBooksRequest searchBooksRequest) {
		return libraryMapper.searchBooksByUser(searchBooksRequest);
	}

	public List<NotificationDTO> LendNotification(int user_id){
		return libraryMapper.LendNotification(user_id);
	}
	
	public List<NotificationDTO> LimitNotification(int user_id){
		return libraryMapper.LimitNotification(user_id);
	}
	
	public void confirmBorrowerNotification(int note,int user_id) {
		libraryMapper.confirmBorrowerNotification(note, user_id);
	}
	
	public void confirmLenderNotification(int note,int user_id) {
		libraryMapper.confirmLenderNotification(note, user_id);
	}

	/** 
	 * @author Lee
	 * Mybookの履歴表示
	 * @returnList
	 * id(book id),
	 * title(貸し借りた本のid),
	 * exihibitorUserId(貸し手のユーザーID)
	 * category(レンタル・譲渡の区分)
	 * limitedate(貸し出し期限)
	 * 
	 * @param userId added by kk
	 **/
	public List<BooksEntity> displayMyBooks(final int SUBLISTSIZE, int startIndex, int userId) {
		return libraryMapper.displayMyBooks(SUBLISTSIZE, startIndex, userId);
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

	/**
	 * @author shunsukekuzawa
	 * 
	 * Search books which meet conditions.
	 * 
	 * @param searchBooksRequest
	 * @return
	 */
	public List<BooksEntity> searchBooks(SearchBooksRequest searchBooksRequest){
		return  libraryMapper.searchBooks(searchBooksRequest);
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
	public List<SearchLogsDTO> displayBorrowLogs(final int SUBLISTSIZE, int startIndex, int userId) {
		return libraryMapper.displayBorrowLogs(SUBLISTSIZE, startIndex, userId);
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
	public List<SearchLogsDTO> displayLendLogs(final int SUBLISTSIZE, int startIndex, int userId) {
		return libraryMapper.displayLendLogs(SUBLISTSIZE, startIndex, userId);
	}
	
	public void bookRegister(BookAddRequest bookRequest) {
		libraryMapper.bookRegister(bookRequest);
	}
	
	public void bookEditer(BookAddRequest bookRequest) {
		libraryMapper.bookEditer(bookRequest);
	}
	
	public void bookDeliter(BookAddRequest bookRequest) {
		libraryMapper.bookDeliter(bookRequest);
	}
	/** 
	 * @author Lee
	 * 「貸し」/「借り」/Mybookの履歴数を返す
	 * @return Lend/Borrow Logsize
	 **/
	public int getLendLogsSize(int userId) {
		return libraryMapper.getLendLogsSize(userId);
	}
	public int getBorrowLogsSize(int userId) {
		return libraryMapper.getBorrowLogsSize(userId);
	}
	public int getMyBookLogsSize(int userId) {
		return libraryMapper.getMyBookLogsSize(userId);
	}
	public int getLendableBookSize(int userId) {
		return libraryMapper.getLendableBookSize(userId);
	}
	/** @author kk */
	public int getLastIdInUsers() {
		return libraryMapper.getLastIdInUsers();
	}

}
