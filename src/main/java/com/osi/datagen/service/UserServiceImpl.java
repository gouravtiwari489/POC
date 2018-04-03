package com.osi.datagen.service;



import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.osi.datagen.domain.Role;
import com.osi.datagen.domain.User;
import com.osi.datagen.intf.IUserService;
import com.osi.datagen.repository.UserRepository;

@Service
public class UserServiceImpl implements IUserService{
	
	public static final String USER = "USER";
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	public User createUser(User user){
		return userRepository.save(buildUserEntity(user));
	}
	
	public User findByEmailId(String emailId) {
		return userRepository.findByEmailId(emailId);
	}
	
	private User buildUserEntity(User user){
		Role roles = new Role();
		roles.setName(USER);
		List<Role> defaultRoles = new ArrayList<>();
		defaultRoles.add(roles);
		if(user.getUsername()=="" || null  == user.getUsername()){
			//user.setEmailId(user.getEmailId());
			user.setUsername(user.getEmailId());
		}
		else/* {
			user.setUsername(user.getUsername());
		}*/
		user.setFullName(user.getFullName());;
		user.setPassword(user.getPassword());
		user.setRoles(defaultRoles);
		return user;
	}
}
