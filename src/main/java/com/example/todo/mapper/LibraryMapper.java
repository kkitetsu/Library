package com.example.todo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.todo.entity.BooksEntity;

@Mapper
public interface LibraryMapper {

	public List<BooksEntity> displayBooks();
}
