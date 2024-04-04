package com.example.todo.entity;

import java.time.LocalDate;

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
	
	private String mailaddress;
	
	private Integer login_id;
	
	private String password;
	
	private LocalDate joined_date;
	
	private LocalDate edited_date;
	
	private Integer del_flag;
}
