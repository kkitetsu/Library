package com.example.todo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;

import com.example.todo.controller.LibraryController;
import com.example.todo.entity.TransactionEntity;
import com.example.todo.entity.UsersEntity;
import com.example.todo.forms.LoginRequest;
import com.example.todo.service.LibraryService;

@SpringBootTest
class LibraryApplicationTests {
	
	@Autowired
    private LibraryService libraryService;

	@Autowired
    private LibraryController libraryController;
    
    @Autowired
    private DataSource dataSource; 
    
    /** @author kk */
    @AfterEach
    public void cleanup() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
        		statement.executeUpdate("DELETE FROM transactions");
        		statement.executeUpdate("DELETE FROM books");
        		statement.executeUpdate("DELETE FROM users");
        		statement.executeUpdate("ALTER TABLE users AUTO_INCREMENT = 1;");
        		statement.executeUpdate("ALTER TABLE transactions AUTO_INCREMENT = 1;");
        		statement.executeUpdate("ALTER TABLE books AUTO_INCREMENT = 1;");
        }
    }
    
    /** @author kk */
    @Test
 	public void testHashing() {
 		String inputPW = "test";
 		String hashedPassword = libraryController.getHashedPassword(inputPW);
 		assertEquals("9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08", hashedPassword);
 	}
	
	/** @author kk */
	@Test
    public void testGetLoginPage() {
        Model model = mock(Model.class);
        String viewName = libraryController.getLoginPage(model);
        assertEquals("login", viewName);
	}
	
	/** @author kk */
	@Test
	public void testRegistrationSuccess() {
		UsersEntity usersEntity = createTestUserEntity();
		libraryService.register(usersEntity);
		checkInsertedDatabase(usersEntity.getMailaddress(), usersEntity.getLogin_id());
	}
 
	/** @author kk */
	private void checkInsertedDatabase(String inputMailAddress, int inputLoginId) {
		try (Connection connection = dataSource.getConnection();
	            Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE name='testName'");
            assertTrue(resultSet.next());
            assertEquals(inputMailAddress, resultSet.getString("mailaddress"));
            assertEquals(inputLoginId,     resultSet.getInt("login_id"));
	    } catch (SQLException e) {
			e.printStackTrace();
		}
	}
    
	/** @author kk */
	@Test
    public void testLogin() {
		
		// First create a dummy data
		UsersEntity usersEntity = createTestUserEntity();
		libraryService.register(usersEntity); 
        
		// Then Prepare data needed for login
        LoginRequest loginRequest = new LoginRequest(); 
        loginRequest.setLogin_id(usersEntity.getLogin_id());
        loginRequest.setLogin_pw(usersEntity.getPassword());

        // Insert test user into the database
        // If the result is not null, it means the login is successful
        List<UsersEntity> result = libraryService.login(loginRequest);
        assertNotNull(result); 
        
        // Test login where login input information is wrong
        loginRequest = new LoginRequest(); 
        loginRequest.setLogin_id(1001);
        loginRequest.setLogin_pw("WRONG PASSWORD");
        result = libraryService.login(loginRequest);
        assertTrue(result.isEmpty());
    }
    
	/** @author kk */
    private UsersEntity createTestUserEntity() {
    	UsersEntity usersEntity = new UsersEntity();
		usersEntity.setDel_flag(0);
		usersEntity.setDepartment("testDepartment");
		usersEntity.setEdited_date(LocalDate.now());
		usersEntity.setJoined_date(LocalDate.now());
		usersEntity.setLogin_id(0);
		usersEntity.setMailaddress("test@example.com");
		usersEntity.setName("testName");
		usersEntity.setPassword(libraryController.getHashedPassword("testPassword"));
		return usersEntity;
    }
    
    /** @author kk */
    @Test
    public void testUpdateTransaction() throws SQLException {
    	UsersEntity usersEntity1 = createTestUserEntity();
    	UsersEntity usersEntity2 = createTestUserEntity();
    	libraryService.register(usersEntity1);
    	libraryService.register(usersEntity2);
    	try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()) {
    		String sql = "INSERT INTO books " +
    					 "(title, content, exhibitor_user_id, category, limitdate, image, exhibition_flag)" + 
    					 " VALUES " +
    					 "('Book1', 'Content of Book1', 1, 'giving', CURRENT_TIMESTAMP, 'book1.jpg', 1)";
    		statement.executeUpdate(sql);
    	}
    	// Book 1, Lender: entitiy1, Borrower: entity2
    	libraryService.updateTransaction(1, libraryService.getUsers().get(0).getId(), 
    										libraryService.getUsers().get(1).getId());
    	List<TransactionEntity> result = libraryService.displayLogs();
    	assertEquals(1, result.get(0).getBookId());
    	assertEquals(1, result.get(0).getLenderUserId());
    	assertEquals(2, result.get(0).getBorrowerUserId());
    	assertEquals("Book1", libraryService.displayBooks().get(0).getTitle());
    }

}
