package com.example.todo.entity;

import java.time.LocalDate;

import lombok.Data;
/** 
 * 
 * @author Lee
 * トランザクション情報 Entity
 **/
@Data
public class TransactionEntity {
	
	/** ID */
	private Integer id; 
	/** 本のID */	
	private Integer bookId;
	/** 貸し手のID */		
	private Integer lenderUserId;
	/** 借り手のID */
	private Integer borrowerUserId;
	/** 貸し借りの日付 */
	private LocalDate tradedDate;
	
	private String newDateRequested;
}
