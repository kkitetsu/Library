package com.example.todo.forms;

import lombok.Data;

/**
 * @author shunsukekuzawa
 *
 * The book name searched by user.
 *
 */

@Data
public class SearchBooksRequest {

	private String book_name;
	private String category;
	
}
