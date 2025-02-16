package com.mis7ake7411.tddprojectdemo.repository;


import com.mis7ake7411.tddprojectdemo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {
  User findByUsername(String username);
}
