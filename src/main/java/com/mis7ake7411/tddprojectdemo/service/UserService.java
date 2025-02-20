package com.mis7ake7411.tddprojectdemo.service;

import com.mis7ake7411.tddprojectdemo.entity.User;
import com.mis7ake7411.tddprojectdemo.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  @Transactional
  public User createUser(User user) {
    return userRepository.save(user);
  }

  @Transactional(readOnly = true)
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  @Transactional
  public void deleteUser(String username) {
    User user = userRepository.findByUsername(username);
    if (user != null) {
      userRepository.delete(user);
    }
  }

  @Transactional
  public User updateUserEmail(String username, String newEmail) {
    User user = userRepository.findByUsername(username);
    if (user != null) {
      user.setEmail(newEmail);
      return userRepository.save(user);
    }
    return null;
  }

}
