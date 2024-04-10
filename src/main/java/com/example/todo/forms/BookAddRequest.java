package com.example.todo.forms;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class BookAddRequest implements Serializable {
	
	private Integer id;
	
	private String image;
	
	private String title;
	
	private String category;
	
	private LocalDate limitdate;
	
	private Integer userId;
	
	private List<MultipartFile> multipartFile;
	
	private String imgPath;
	
}
