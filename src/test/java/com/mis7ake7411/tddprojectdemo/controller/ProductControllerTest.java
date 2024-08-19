package com.mis7ake7411.tddprojectdemo.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Test
  public void getProduct_success() throws Exception {
    // Given
    RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products/{id}", 1);
    // When & Then
    mockMvc.perform(requestBuilder)
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.productName", equalTo("Apple")))
        .andExpect(jsonPath("$.category", equalTo("FOOD")))
        .andExpect(jsonPath("$.price", notNullValue()))
        .andExpect(jsonPath("$.stock", notNullValue()));
  }

  @Test
  public void getProduct_notFound() throws Exception {
    // Given
    RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products/{id}", 999);
    // When & Then
    mockMvc.perform(requestBuilder)
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  public void getProducts_success() throws Exception {
    // Given
    RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products");
    // When & Then
    mockMvc.perform(requestBuilder)
        .andDo(print())
        .andExpect(status().isOk());
  }
}
