package com.example.todo.forms;

import java.io.Serializable;

import jakarta.validation.constraints.Digits;
import lombok.Data;

@Data
public class LoginRequest implements Serializable {
	
	@Digits(integer=Integer.MAX_VALUE, fraction=0, message="Login ID must be an integer")
	private int login_id;
	
	private String login_pw;
}
