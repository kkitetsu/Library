package com.example.todo.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.example.todo.entity.BooksEntity;
import com.example.todo.forms.SearchBooksRequest;
import com.example.todo.service.LibraryService;

/**
 * @author kk
 * 
 * This is the class for tests.
 * It is using Junit 5.
 * 
 * Before and after each test, cleanup will be run and all the data will be cleared.
 * 
 * For each initialization, the corresponding id will be automatically incremented.
 * For example, if three users are initialized (three userEntity),
 * 		corresponding id will be 1, 2, and 3. 
 *
 * Use libraryService to check if the sql statement is correct.
 * Use libraryController to check if the direct destination is correct.
 * 
 */
@SpringBootTest
class LibraryHomeTest {

	@Autowired
	private LibraryService libraryService;

	@Autowired
	private LibraryController libraryController;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private LocalValidatorFactoryBean validatorFactory;

	/** @author kk */
	@BeforeEach
	@AfterEach
	public void cleanup() throws SQLException {
		try (Connection connection = dataSource.getConnection();
				Statement statement = connection.createStatement()) {
			statement.executeUpdate("DELETE FROM transactions");
			statement.executeUpdate("DELETE FROM comments");
			statement.executeUpdate("DELETE FROM hashtag_maps");
			statement.executeUpdate("DELETE FROM books");
			statement.executeUpdate("DELETE FROM users");
			statement.executeUpdate("ALTER TABLE users AUTO_INCREMENT = 1;");
			statement.executeUpdate("ALTER TABLE transactions AUTO_INCREMENT = 1;");
			statement.executeUpdate("ALTER TABLE books AUTO_INCREMENT = 1;");
		}
	}

	//************************************************************************************
	// * Test helper functions
	     

	@Test
	public void testHomeAccess() {
		Model model = mock(Model.class);
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("userId", 1);
		assertEquals("/home", libraryController.home(model, session));

	}

	@Test
	public void testHomeAccessNull() {
		Model model = mock(Model.class);
		MockHttpSession session = new MockHttpSession();
		assertEquals("redirect:/login", libraryController.home(model, session));
	}

	@Test
	public void testHomeDisplay() throws SQLException {
		try (Connection connection = dataSource.getConnection();
				Statement statement = connection.createStatement()) {
			String sqlUsers = "INSERT INTO users " +
					"(name, department, mailaddress, login_id, password, joined_date, edited_date, del_flag)" +
					" VALUES " +
					"('User 1', 'Department 1', 'user1@example.com', 1001, 'password1', '2024-04-03 12:00:00', '2024-04-03 12:00:00', 0),"
					+
					"('User 2', 'Department 2', 'user2@example.com', 1002, 'password2', '2024-04-03 12:00:00', '2024-04-03 12:00:00', 0),"
					+
					"('User 3', 'Department 3', 'user3@example.com', 1003, 'password3', '2024-04-03 12:00:00', '2024-04-03 12:00:00', 0)";
			statement.executeUpdate(sqlUsers);
			String sqlBooks = "INSERT INTO books " +
					"(title, content, exhibitor_user_id, category, limitdate, image, exhibition_flag)" +
					" VALUES " +
					"('Book1', 'Content of Book1', 1, 'giving', CURRENT_TIMESTAMP, 'book1.jpg', 1) ," +
					"('Book2', 'Content of Book2', 2, 'lending', CURRENT_TIMESTAMP, 'book2.jpg', 1) ," +
					"('Book3', 'Content of Book3', 3, 'lending', CURRENT_TIMESTAMP, 'book3.jpg', 1)";
			statement.executeUpdate(sqlBooks);
		}
		List<BooksEntity> bookshelfTest = libraryService.displayBooks();
		int LEN = bookshelfTest.size();
		assertEquals(3, LEN);

	}

	@Test
	public void testHomeDisplay2() throws SQLException {
		Model model = mock(Model.class);
		try (Connection connection = dataSource.getConnection();
				Statement statement = connection.createStatement()) {
			String sqlUsers = "INSERT INTO users " +
					"(name, department, mailaddress, login_id, password, joined_date, edited_date, del_flag)" +
					" VALUES " +
					"('User 1', 'Department 1', 'user1@example.com', 1001, 'password1', '2024-04-03 12:00:00', '2024-04-03 12:00:00', 0),"
					+
					"('User 2', 'Department 2', 'user2@example.com', 1002, 'password2', '2024-04-03 12:00:00', '2024-04-03 12:00:00', 0),"
					+
					"('User 3', 'Department 3', 'user3@example.com', 1003, 'password3', '2024-04-03 12:00:00', '2024-04-03 12:00:00', 0)";
			statement.executeUpdate(sqlUsers);
			//exhibition_flagを0で追加
			String sqlBooks = "INSERT INTO books " +
					"(title, content, exhibitor_user_id, category, limitdate, image, exhibition_flag)" +
					" VALUES " +
					"('Book4', 'Content of Book4', 1, 'giving', CURRENT_TIMESTAMP, 'book1.jpg', 1) ," +
					"('Book5', 'Content of Book5', 2, 'lending', CURRENT_TIMESTAMP, 'book2.jpg', 0) ," +
					"('Book6', 'Content of Book6', 3, 'lending', CURRENT_TIMESTAMP, 'book3.jpg', 0)";
			statement.executeUpdate(sqlBooks);
		}
		List<BooksEntity> bookshelfTest = libraryService.displayBooks();
		int LEN = bookshelfTest.size();
		////exhibition_flagが0のデータは取らないかテスト
		assertEquals(1, LEN);

	}

	@Test
	public void testHomeSearchController() throws SQLException {
		Model model = mock(Model.class);
		SearchBooksRequest searchBooksRequest = new SearchBooksRequest();
		assertEquals("/home", libraryController.search(model, searchBooksRequest));
	}

	@Test
	public void testHomeSearchService() throws SQLException {
		try (Connection connection = dataSource.getConnection();
				Statement statement = connection.createStatement()) {
			String sqlUsers = "INSERT INTO users " +
					"(name, department, mailaddress, login_id, password, joined_date, edited_date, del_flag)" +
					" VALUES " +
					"('User 1', 'Department 1', 'user1@example.com', 1001, 'password1', '2024-04-03 12:00:00', '2024-04-03 12:00:00', 0),"
					+
					"('User 2', 'Department 2', 'user2@example.com', 1002, 'password2', '2024-04-03 12:00:00', '2024-04-03 12:00:00', 0),"
					+
					"('User 3', 'Department 3', 'user3@example.com', 1003, 'password3', '2024-04-03 12:00:00', '2024-04-03 12:00:00', 0)";
			statement.executeUpdate(sqlUsers);
			//exhibition_flagを0で追加
			String sqlBooks = "INSERT INTO books " +
					"(title, content, exhibitor_user_id, category, limitdate, image, exhibition_flag)" +
					" VALUES " +
					"('Book4', 'Content of Book4', 1, 'giving', CURRENT_TIMESTAMP, 'book1.jpg', 1) ," +
					"('Book5', 'Content of Book5', 2, 'lending', CURRENT_TIMESTAMP, 'book2.jpg', 0) ," +
					"('Book6', 'Content of Book6', 3, 'lending', CURRENT_TIMESTAMP, 'book3.jpg', 1)";
			statement.executeUpdate(sqlBooks);
		}
		SearchBooksRequest searchBooksRequest = new SearchBooksRequest();

		//検索条件「Book」（曖昧検索できているか）
		searchBooksRequest.setBook_name("Book");
		List<BooksEntity> bookshelfTest = libraryService.searchBooks(searchBooksRequest);
		int LEN1 = bookshelfTest.size();
		assertEquals(2, LEN1);

		//検索条件「4」（曖昧検索できているか）
		searchBooksRequest.setBook_name("4");
		bookshelfTest = libraryService.searchBooks(searchBooksRequest);
		int LEN2 = bookshelfTest.size();
		assertEquals(1, LEN2);

		//検索条件「B」（曖昧検索できているか）
		searchBooksRequest.setBook_name("B");
		bookshelfTest = libraryService.searchBooks(searchBooksRequest);
		int LEN4 = bookshelfTest.size();
		assertEquals(2, LEN4);

		//検索条件「NARUTO」（データがないもの）
		searchBooksRequest.setBook_name("NARUTO");
		bookshelfTest = libraryService.searchBooks(searchBooksRequest);
		int LEN3 = bookshelfTest.size();
		assertEquals(0, LEN3);

		//検索条件「null」（Nullチェック）
		searchBooksRequest.setBook_name("");
		bookshelfTest = libraryService.searchBooks(searchBooksRequest);
		int LEN5 = bookshelfTest.size();
		assertEquals(2, LEN5);

	}

}
