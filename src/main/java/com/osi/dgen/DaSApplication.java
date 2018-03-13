package com.osi.dgen;

import com.osi.dgen.domain.CustomUserDetails;
import com.osi.dgen.domain.Role;
import com.osi.dgen.domain.User;
import com.osi.dgen.repository.UserRepository;
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
