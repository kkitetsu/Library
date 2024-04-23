package com.example.todo.forms;

import java.io.Serializable;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
	
	@Digits(integer=Integer.MAX_VALUE, fraction=0, message="ログイン ID は整数である必要があります")
	@Positive(message="ログインID は 0 より大きい必要があります")
	@NotNull(message="ログインIDは空欄にできません")
	private int login_id;
	
	@NotEmpty(message="パスワードは空欄にできません")
	private String login_pw;
}
