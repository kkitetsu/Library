package com.example.todo.forms;

import java.io.Serializable;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * @author kk
 * 
 * Data need for login process.
 *
 */
@Data
public class LoginRequest implements Serializable {
	
	@Digits(integer=Integer.MAX_VALUE, fraction=0, message="Login ID must be an integer")
	@Positive(message="ID must be a positive number")
	private int login_id;
	
	private String login_pw;
}
