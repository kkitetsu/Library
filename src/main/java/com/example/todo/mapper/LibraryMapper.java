package com.example.todo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.todo.entity.UsersEntity;
import com.example.todo.forms.LoginRequest;

@Mapper
public interface LibraryMapper {
	public List<UsersEntity> login(LoginRequest loginRequest);
}
