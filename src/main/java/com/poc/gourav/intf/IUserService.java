package com.poc.gourav.intf;

import com.poc.gourav.domain.User;

public interface IUserService {

  public User createUser(User user);

  public User findByUserName(String userName);
}
