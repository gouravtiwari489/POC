package com.poc.gourav;

import com.poc.gourav.constant.PocConstants;
import com.poc.gourav.domain.CustomUserDetails;

import com.poc.gourav.domain.User;
import com.poc.gourav.repository.UserRepository;
import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

@SpringBootApplication
public class PocApplication {

  private static String fileDownloadPath;

  @Value("${file.download.path}")
  public void setPath(String path) {
    fileDownloadPath = path;
  }

  public static void main(String[] args) {
    //System.out.println("Application Starting...... check the logs in logs/das.log");
    SpringApplication.run(PocApplication.class, args);
  }

  @Autowired
  public void authenticationManager(AuthenticationManagerBuilder builder, UserRepository repo)
      throws Exception {
    if (repo.count() == 0) {
      repo.save(new User(PocConstants.DEFAULT_USER, PocConstants.DEFAULT_PASSWORD /*,
              Arrays.asList(new Role("USER"), new Role("ACTUATOR"))*/));
    }

    File outputFolder = new File(fileDownloadPath);
    if (!outputFolder.exists()) {
      new File(fileDownloadPath).mkdir();
    }

    builder.userDetailsService(name -> new CustomUserDetails(repo.findByUsername(name)));
  }
}
