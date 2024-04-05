package com.example.todo.DTO;

import java.time.LocalDate;

import lombok.Data;
@Data
public class SearchLogsDTO {
	private Integer   TransId;
	private String    BookTitle;
	private Integer   LenderId;
	private String    LenderName;
	private Integer   BorrowerId;
	private String    BorrowerName;
	private LocalDate TradedDate;
	private LocalDate RegisterBookDate;
	private LocalDate LimitDate;
}
