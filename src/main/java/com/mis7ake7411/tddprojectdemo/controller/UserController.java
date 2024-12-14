package com.mis7ake7411.tddprojectdemo.controller;

import com.mis7ake7411.tddprojectdemo.model.User;
import com.mis7ake7411.tddprojectdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  @Autowired
  private UserService userService;

  //controller methods user create, update, delete, get
  //create user
  @PostMapping("/users")
  public void createUser() {
    // 創建用戶
    User newUser = new User();
    newUser.setUsername("testuser");
    newUser.setEmail("testuser@example.com");

    // 保存用戶
    User savedUser = userService.createUser(newUser);
  }
  //update user
  @PutMapping("/users/{id}")
  public void updateUser() {
    // 更新郵箱
    User updatedUser = userService.updateUserEmail("updateuser", "updated@example.com");

  }
  //delete user
  @DeleteMapping("/users/{id}")
  public void deleteUser() {
    // 刪除用戶
    userService.deleteUser("deleteuser");
  }
  //get user

}
