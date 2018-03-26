package com.osi.datagen;

import com.osi.datagen.domain.CustomUserDetails;
import com.osi.datagen.domain.Role;
import com.osi.datagen.domain.User;
import com.osi.datagen.repository.UserRepository;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

@SpringBootApplication
public class DaSApplication {

  public static void main(String[] args) {
    System.out.println("Application Starting...... check the logs in logs/das.log");
    SpringApplication.run(DaSApplication.class, args);
  }

  @Autowired
  public void authenticationManager(AuthenticationManagerBuilder builder, UserRepository repo)
      throws Exception {
    if (repo.count() == 0) {
      repo.save(new User("user", "user", Arrays.asList(new Role("USER"), new Role("ACTUATOR"))));
    }

    builder.userDetailsService(name -> new CustomUserDetails(repo.findByUsername(name)));
  }
}
