package com.mis7ake7411.tddprojectdemo.controller;

import com.mis7ake7411.tddprojectdemo.model.Product;
import com.mis7ake7411.tddprojectdemo.repository.ProductRepositroy;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  @GetMapping("/products")
  public ResponseEntity<List<Product>> getProducts() {
    List<Product> products = productRepository.findAll();
    return ResponseEntity.ok(products);
  }

  @PostMapping("/products")
  public ResponseEntity<Product> createProduct(@RequestBody @Valid Product product) {
    Product newProduct = productRepository.save(product);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(newProduct);
  }

  @DeleteMapping("/products/{id}")
  public ResponseEntity<Product> deleteProduct(@PathVariable Integer id) {
    Product product = productRepository.getProductById(id);

    productRepository.delete(product);
    return ResponseEntity.ok()
        .build();
  }
}
