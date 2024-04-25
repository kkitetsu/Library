package com.example.todo.entity;

import java.time.LocalDate;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
	
	@NotEmpty(message="名前は空欄にできません。")
	@Size(max=20, message="名前は20文字以下でお願いします。")
	private String name;
	
	@NotEmpty(message="部署は空欄にできません。")
	@Size(max=255, message="部署は225文字以下でお願いします。")
    @Pattern(regexp="^(人事総務部|IT本部|マーケティング推進部|その他)$", message="部署名がおかしいです。")
	private String department;
	
	@Email(message="正しいメールアドレスの形式ではありません。")
	@Pattern(regexp = "^[a-zA-Z0-9_.+-]+@([a-zA-Z0-9][a-zA-Z0-9-]*[a-zA-Z0-9]*\\.)+[a-zA-Z]{2,}$",
    						message="正しいメールアドレスの形式ではありません。")
	@NotEmpty(message="メールアドレスは空欄にできません。")
	@Size(max=255, message="メールアドレスは225文字以下でお願いします。")
	private String mailaddress;
	
	@Digits(integer=Integer.MAX_VALUE, fraction=0, message="ログイン ID は整数である必要があります。")
	@Positive(message="ログインID は 0 より大きい必要があります。")
	@NotNull(message="ログインIDは空欄にできません。")
	@Max(value=999999, message="ログインIDは7桁以上にできません。")
	private Integer loginId;
	
	@NotEmpty(message="パスワードは空欄にできません。")
    @Size(min=4, max=16, message="パスワードは４文字から16文字の英数字です。")
	@Pattern(regexp = "^[a-zA-Z0-9]*$", message="パスワードは英数字で入力してください。")
	private String password;
	
	private LocalDate joined_date;
	
	private LocalDate edited_date;
	
	private Integer del_flag;
}
