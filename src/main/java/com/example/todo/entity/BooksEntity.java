package com.example.todo.entity;

import lombok.Data;

/**
 * 
 * @author kk
 * 
 * Information for books.
 *
 */
@Data
public class BooksEntity {

	private Integer id;
	
	private String title;
	
	private String content;
	
	private String image;
	
	private String category;
	
	private String limitdate;
	
}
