package com.mis7ake7411.tddprojectdemo.repository;

import com.mis7ake7411.tddprojectdemo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface ProductRepositroy extends JpaRepository<Product, Integer> {
  Product getProductById(Integer id);
}
