package com.example.todo.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;

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
class LibraryMyBookTest {

	@Autowired
	private LibraryService libraryService;

	@Autowired
	private LibraryController libraryController;

	@Autowired
	private DataSource dataSource;

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

	public void InsertData() throws SQLException {
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
	}

	//************************************************************************************
	// Controller Test





	@Test
	public void testConfirmRequest() throws SQLException {
		Model model = mock(Model.class);
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("userId", 1);
		String id = "1";
		String exhibitorId = "2";

		InsertData();

		assertEquals("redirect:/borrowlog", libraryController.doBookConfirm(id, exhibitorId, model, session));
	}

	//************************************************************************************
	// Service Test
	@Test
	public void testConfirmService() throws SQLException {

		InsertData();

		String exhibitorName = libraryService.getNameBasedOnId(1);

		assertEquals("User 1", exhibitorName);

		int bookId = 1;
		int lenderId = 2;
		int borrowerId = 3;

		libraryService.updateTransaction(bookId, lenderId, borrowerId);
		try (Connection connection = dataSource.getConnection();
				Statement statement = connection.createStatement()) {
			//exhibition_flagを0で追加
			String sqlBooks = "Select * From transactions WHERE book_id =" + bookId + ";";
			ResultSet rs = statement.executeQuery(sqlBooks);
			while (rs.next()) {
                // 列名を指定してデータを取得する例
				assertEquals(1,rs.getInt("book_id"));
				assertEquals(2,rs.getInt("lender_user_id"));
				assertEquals(3,rs.getInt("borrower_user_id"));
            }
		}
	}

}
