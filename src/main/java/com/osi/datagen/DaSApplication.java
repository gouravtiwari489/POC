package com.osi.datagen;

import com.osi.datagen.constant.DasConstants;
import com.osi.datagen.domain.CustomUserDetails;
import com.osi.datagen.domain.Role;
import com.osi.datagen.domain.User;
import com.osi.datagen.repository.UserRepository;
import java.io.File;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

@SpringBootApplication
public class DaSApplication {

  private static String fileDownloadPath;

  @Value("${file.download.path}")
  public void setPath(String path) {
    fileDownloadPath = path;
  }

  public static void main(String[] args) {
    System.out.println("Application Starting...... check the logs in logs/das.log");
    SpringApplication.run(DaSApplication.class, args);
  }

  @Autowired
  public void authenticationManager(AuthenticationManagerBuilder builder, UserRepository repo)
      throws Exception {
    if (repo.count() == 0) {
      repo.save(
          new User(
              DasConstants.DEFAULT_USER,
              DasConstants.DEFAULT_PASSWORD,
              Arrays.asList(new Role("USER"), new Role("ACTUATOR"))));
    }

    File outputFolder = new File(fileDownloadPath);
    if (!outputFolder.exists()) {
      new File(fileDownloadPath).mkdir();
    }

    builder.userDetailsService(name -> new CustomUserDetails(repo.findByUsername(name)));
  }
}
