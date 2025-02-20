package com.mis7ake7411.tddprojectdemo.repository;


import com.mis7ake7411.tddprojectdemo.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {
  User findByUsername(String username);
}
