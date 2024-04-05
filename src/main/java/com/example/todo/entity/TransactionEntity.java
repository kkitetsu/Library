package com.example.todo.entity;

import java.time.LocalDate;

import lombok.Data;
@Data
public class TransactionEntity {

	private Integer id; 
	
	private Integer bookId;
	
	private Integer lenderUserId;
	
	private Integer borrowerUserId;
	
	private LocalDate tradedDate;
}
