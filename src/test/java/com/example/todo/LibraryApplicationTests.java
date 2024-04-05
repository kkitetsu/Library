package com.example.todo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;

import com.example.todo.controller.LibraryController;
import com.example.todo.forms.LoginRequest;
import com.example.todo.mapper.LibraryMapper;
import com.example.todo.service.LibraryService;
import com.example.todo.utils.HashGenerator;

import jakarta.servlet.http.HttpSession;

@RunWith(MockitoJUnitRunner.class)
class LibraryApplicationTests {
	
	@Mock
    private LibraryService libraryService;
	
	@Mock
    private LibraryMapper libraryMapper;

    @InjectMocks
    private LibraryController libraryController;
    
    @BeforeEach
    public void setUp() {
        libraryController = new LibraryController(); 
    }

	@Test
	public void testHashing() {
		String inputPW = "test";
		String hashedPassword = libraryController.getHashedPassword(inputPW);
		assertEquals("9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08", hashedPassword);
	}
	
	@Test
    public void testGetLoginPage() {
        Model model = mock(Model.class);
        String viewName = libraryController.getLoginPage(model);
        assertEquals("login", viewName);
	}
    
    @Test
    public void testLogin() {
        int userId = 1;
        String password = "testpassword";
        
        Model model = Mockito.mock(Model.class);
        HttpSession session = Mockito.mock(HttpSession.class);
        
        LoginRequest loginRequest = new LoginRequest();
        
        loginRequest.setLogin_id(userId);
        loginRequest.setLogin_pw(password);

        // Insert test user into the database
        try {
			insertTestUser(userId, HashGenerator.generateHash(password));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        
        String result = libraryController.doLogin(model, session, loginRequest);

        // Assert that login was successful
        assertEquals("/home", result);
    }
    
    private void insertTestUser(int id, String password) {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "password");
            stmt = conn.createStatement();
            String sql = "INSERT INTO users (\n" +
                    "    name, \n" +
                    "    department, \n" +
                    "    mailaddress, \n" +
                    "    login_id, \n" +
                    "    password, \n" +
                    "    joined_date, \n" +
                    "    edited_date, \n" +
                    "    del_flag \n" +
                    ") VALUES (\n" +
                    "    'NULL', \n" +
                    "    'NULL', \n" +
                    "    'NULL', \n" +
                    "    '" + id + "', \n" +
                    "    '" + password + "', \n" +
                    "    CURRENT_TIMESTAMP, \n" +
                    "    'NULL', \n" +
                    "    0 \n" +
                    ")";
            System.out.println(sql);
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
