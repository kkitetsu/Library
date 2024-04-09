package com.example.todo.forms;

import lombok.Data;

@Data
public class BookAddRequest {
	
	public String image;
	
	public String title;
	
	public String category;
	
	public String limitdate;
}
