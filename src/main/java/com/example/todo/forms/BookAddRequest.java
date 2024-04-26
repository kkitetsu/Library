package com.example.todo.forms;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.example.todo.validation.FileSize;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BookAddRequest implements Serializable {
	
	private Integer id;
	
	private String image;
	
	@Size(max=200, message="タイトルが長すぎます200文字以内で勘弁してください。")
	@NotEmpty(message="タイトルは必須です。")
	private String title;
	
	@NotEmpty(message="区分は必須です。")
	@Pattern(regexp="^(貸出|譲渡)$", message="区分がおかしいです。")
	private String category;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@Future(message="過去の日付は選択できません。")
	private LocalDate limitdate;
		
	private Integer userId;
	
	@FileSize(max = 1048576, message="ファイルサイズが大きすぎます。")
	private List<MultipartFile> multipartFile;
	
	private String imgPath;
	
	private int exhibition_flag;
	
}

