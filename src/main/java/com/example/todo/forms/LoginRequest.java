package com.example.todo.forms;

import java.io.Serializable;

import lombok.Data;

@Data
public class LoginRequest implements Serializable {
	private int login_id;
	private String login_pw;
}
