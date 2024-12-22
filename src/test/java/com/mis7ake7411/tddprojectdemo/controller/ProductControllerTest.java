package com.mis7ake7411.tddprojectdemo.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mis7ake7411.tddprojectdemo.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

  @Autowired
  private MockMvc mockMvc;

  private ObjectMapper objectMapper = new ObjectMapper();

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

  @Transactional
  @Test
  public void createProduct_success() throws Exception {
    Product product = Product.builder()
        .productName("Banana")
        .category("FOOD")
        .price(20.0)
        .stock(50)
        .build();

    String jsonObj = objectMapper.writeValueAsString(product);
    // Given
    RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/products")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonObj);

    // When & Then
    mockMvc.perform(requestBuilder)
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", equalTo(4)))
        .andExpect(jsonPath("$.productName", equalTo("Banana")))
        .andExpect(jsonPath("$.category", equalTo("FOOD")))
        .andExpect(jsonPath("$.price", equalTo(20.0)))
        .andExpect(jsonPath("$.stock", equalTo(50)));
  }

  @Transactional
  @Test
  public void createProduct_badRequest() throws Exception {
    Product product = Product.builder()
        .productName("Banana")
        .build();

    String jsonObj = objectMapper.writeValueAsString(product);
    // Given
    RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/products")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonObj);

    // When & Then
    mockMvc.perform(requestBuilder)
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Transactional
  @Test
  public void deleteProduct_success() throws Exception {
    // Given
    RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/products/{id}", 2);
    // When & Then
    mockMvc.perform(requestBuilder)
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Transactional
  @Test
  public void updateProduct_success() throws Exception {
    Product product = Product.builder()
        .id(1)
        .productName("Book Of Life")
        .category("BOOK")
        .price(30.0)
        .stock(10)
        .build();

    String objJson = objectMapper.writeValueAsString(product);
    // Given
    RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/products/{id}", 1)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objJson);
    // When & Then
    mockMvc.perform(requestBuilder)
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", equalTo(1)))
        .andExpect(jsonPath("$.productName", equalTo("Book Of Life")))
        .andExpect(jsonPath("$.category", equalTo("BOOK")))
        .andExpect(jsonPath("$.price", equalTo(30.0)))
        .andExpect(jsonPath("$.stock", equalTo(10)));
  }

  @Transactional
  @Test
  public void updateProduct_notFound() throws Exception {
    Product product = Product.builder()
        .id(100)
        .productName("Book Of Life")
        .build();

    String objJson = objectMapper.writeValueAsString(product);
    // Given
    RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/products/{id}", 100)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objJson);
    // When & Then
    mockMvc.perform(requestBuilder)
        .andDo(print())
        .andExpect(status().isNotFound());
  }
}
