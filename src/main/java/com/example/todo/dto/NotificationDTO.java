package com.example.todo.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class NotificationDTO {
	
	private Integer TransId;
	
	private String message;
	
	private String  BookTitle;
	
	private Integer LenderId;
	
	private String LenderName;
	
	private Integer BorrowerId;
	
	private String BorrowerName;
	
	private LocalDate NotificationDate;
	
	private String category;
	
	// added by kk
	private String newDateRequested;
	
	// added by kk
	private Integer bookId;

}
