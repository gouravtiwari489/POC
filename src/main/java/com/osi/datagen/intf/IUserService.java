package com.osi.datagen.intf;

import com.osi.datagen.domain.User;

public interface IUserService {

  public User createUser(User user);

  public User findByUserName(String userName);
}
