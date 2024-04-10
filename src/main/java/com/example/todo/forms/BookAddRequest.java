package com.example.todo.forms;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class BookAddRequest implements Serializable {
	
	private String image;
	
	@NotEmpty(message="タイトルは必須です")
	private String title;
	
	private String category;
	
	@Future(message="過去の日付は選択できません")
	private LocalDate limitdate;
		
	private Integer userId;
	
	private List<MultipartFile> multipartFile;
	
	private String imgPath;
	
}
