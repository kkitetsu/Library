package com.example.todo.dto;

import java.time.LocalDate;

import lombok.Data;
@Data
/** 
 * @author Lee
 * 取引のログをまとめ情報 Entity
 * TransanctionEntityのid表記を
 * 名前表記にする Entity
 **/
public class SearchLogsDTO {
	/** 取引 ID */
	private Integer   TransId;
	/** 本のタイトル */
	private String    BookTitle;
	/** 貸し手のID */
	private Integer   LenderId;
	/** 貸し手の名前*/
	private String    LenderName;
	/**　借り手のID */
	private Integer   BorrowerId;
	/**  借り手の名前　*/
	private String    BorrowerName;
	/** 貸し借りの日付　*/
	private LocalDate TradedDate;
	/** 本が本サービスに登録された日付 */
	private LocalDate RegisterBookDate;
	/** 本の返却期限　*/
	private LocalDate LimitDate;
}
