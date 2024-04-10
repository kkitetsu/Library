package com.example.todo.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LibraryControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	DemoService mockService;

	@Test
	public void shoppingTest01() throws Exception {

		// 使用する商品情報の準備
		Goods goods = createGoods();

		// リクエストの準備
		ObjectMapper objectMapper = new ObjectMapper();
		String requestJson = objectMapper.writeValueAsString(goods);

		// モックの設定
		when(mockService.decide(any(Goods.class))).thenReturn(true);

		// execute
		String responseJson = this.mockMvc
				.perform(post("/demo/shopping").contentType(MediaType.APPLICATION_JSON).content(requestJson))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		Boolean result = objectMapper.readValue(responseJson, Boolean.class);
		verify(mockService, times(1)).decide(any(Goods.class));
		assertTrue(result);

	}

}
