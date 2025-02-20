package com.mis7ake7411.tddprojectdemo.repository;

import com.mis7ake7411.tddprojectdemo.entity.Product;
import org.springframework.stereotype.Component;

@Component
public interface ProductRepository extends BaseRepository<Product, Integer> {
  Product getProductById(Integer id);
}
