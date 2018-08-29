package com.poc.gourav.service;

import com.poc.gourav.domain.Role;
import com.poc.gourav.domain.User;
import com.poc.gourav.intf.IUserService;
import com.poc.gourav.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

  public static final String USER = "USER";

  @Autowired UserRepository userRepository;

  @Override
  public User createUser(User user) {
    return userRepository.save(buildUserEntity(user));
  }

 /* public User findByEmailId(String emailId) {
    return userRepository.findByEmailId(emailId);
  }*/

  private User buildUserEntity(User user) {
    Role roles = new Role();
    roles.setUserRole(USER);
    List<Role> defaultRoles = new ArrayList<>();
    defaultRoles.add(roles);
    if (user.getUsername() == "" || null == user.getUsername()) {
      //user.setEmailId(user.getEmailId());
      user.setUsername(user.getUsername());
    } else /* {
           	user.setUsername(user.getUsername());
           }*/ user.setUsername(user.getUsername());
    ;
    user.setPassword(user.getPassword());
    user.setRole(defaultRoles.get(0));
    return user;
  }

@Override
public User findByUserName(String userName) {
	return userRepository.findByUsername(userName);
}
}
