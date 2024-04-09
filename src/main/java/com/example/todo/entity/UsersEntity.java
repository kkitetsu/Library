package com.example.todo.entity;

import java.time.LocalDate;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * @author kk
 * 
 * Data need for users.
 */
@Data
public class UsersEntity {
	
	private Integer id;
	
	private String name;
	
	private String department;
	
	@Email(message="Invalid email address")
	private String mailaddress;
	
	@Digits(integer=Integer.MAX_VALUE, fraction=0, message="Login ID must be an integer")
	@Positive(message="ID must be a positive number")
	private Integer loginId;
	
	private String password;
	
	private LocalDate joined_date;
	
	private LocalDate edited_date;
	
	private Integer del_flag;
}
