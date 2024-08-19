package com.mis7ake7411.tddprojectdemo.controller;

import com.mis7ake7411.tddprojectdemo.model.Product;
import com.mis7ake7411.tddprojectdemo.repository.ProductRepositroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
  @Autowired
  ProductRepositroy productRepository;

  @GetMapping("/products/{id}")
  public ResponseEntity<Product> getProduct(@PathVariable Integer id) {
    Product product = productRepository.getProductById(id);
    if (product == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(product);
  }
}
