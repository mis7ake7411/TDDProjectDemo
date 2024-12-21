package com.mis7ake7411.tddprojectdemo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mis7ake7411.tddprojectdemo.model.User;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {

  @Autowired
  private UserService userService;

  @Test
  public void testCreateAndRetrieveUser() {

    // 創建用戶
    User newUser = new User();
    newUser.setUsername("testUser");
    newUser.setEmail("testuser@example.com");

    // 保存用戶
    User savedUser = userService.createUser(newUser);

    // 驗證
    assertNotNull(savedUser.getId());
    assertEquals("testUser", savedUser.getUsername());
    assertEquals("testuser@example.com", savedUser.getEmail());
  }

  @Test
  public void testUpdateUserEmail() {
    // 創建初始用戶
    User user = new User();
    user.setUsername("updateuser");
    user.setEmail("original@example.com");
    userService.createUser(user);

    // 更新郵箱
    User updatedUser = userService.updateUserEmail("updateUser", "updated@example.com");

    // 驗證
    assertNotNull(updatedUser);
    assertEquals("updated@example.com", updatedUser.getEmail());
  }

  @Test
  public void testDeleteUser() {
    // 創建用戶
    User user = new User();
    user.setUsername("deleteUser");
    user.setEmail("delete@example.com");
    userService.createUser(user);

    // 刪除用戶
    userService.deleteUser("deleteuser");

    // 獲取所有用戶並驗證
    List<User> remainingUsers = userService.getAllUsers();
    assertFalse(remainingUsers.stream()
        .anyMatch(u -> "deleteuser".equals(u.getUsername())));
  }
}