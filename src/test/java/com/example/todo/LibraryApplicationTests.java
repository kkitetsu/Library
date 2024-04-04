package com.example.todo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.Model;

import com.example.todo.controller.LibraryController;
import com.example.todo.utils.HashGenerator;

@SpringBootTest
class LibraryApplicationTests {

	@Test
	public void testHashing() {
		String inputPW = "test";
		String hashedPassword = "";
		try {
			hashedPassword = HashGenerator.generateHash(inputPW);
		} catch (NoSuchAlgorithmException e) {
			fail("Expected NO Exception to be thrown");
		}
		assertEquals("9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08", hashedPassword);
	}
	
	@Test
    public void testGetLoginPage() {
        LibraryController controller = new LibraryController();
        Model model = mock(Model.class);
        String viewName = controller.getLoginPage(model);
        assertEquals("/login", viewName);
	}
	
	@Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testDatabaseConnectivity() {
        // Attempt to execute a simple query to verify database connectivity
        boolean isConnected = false;
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            isConnected = true;
        } catch (Exception e) {
            // Connection failed
            e.printStackTrace();
        }

        // Verify that the connection was successful
        assertTrue(isConnected, "Failed to connect to the database");
    }

}
