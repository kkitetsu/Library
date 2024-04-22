package com.example.todo.entity;

import java.time.LocalDate;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author kk
 * 
 * Data need for users.
 */
@Data
public class UsersEntity {
	
	private Integer id;
	
	@NotEmpty(message="名前は空欄にできません")
	private String name;
	
	@NotEmpty(message="部署は空欄にできません")
	private String department;
	
	@Email(message="正しいメールアドレスの形式ではありません。")
	@Pattern(regexp = "^[a-zA-Z0-9_.+-]+@([a-zA-Z0-9][a-zA-Z0-9-]*[a-zA-Z0-9]*\\.)+[a-zA-Z]{2,}$",
    						message="正しいメールアドレスの形式ではありません。")
	private String mailaddress;
	
	@Digits(integer=Integer.MAX_VALUE, fraction=0, message="ログイン ID は整数である必要があります")
	@Positive(message="ログインID は 0 より大きい必要があります")
	private Integer loginId;
	
	@NotEmpty(message="パスワードは空欄にできません")
    @Size(min=4, max=16, message="パスワードは４から16文字です")
	private String password;
	
	private LocalDate joined_date;
	
	private LocalDate edited_date;
	
	private Integer del_flag;
}
