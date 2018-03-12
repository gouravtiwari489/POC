package com.datagenerator.das.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

  private static final long serialVersionUID = 1L;

  private String username;

  private String password;

  Collection<? extends GrantedAuthority> authorities;

  private String mappedData;

  private Map<String, Integer> map;

  Map<Integer, List<String>> orderedFKListMap;

  List<LinkedHashMap<String, LinkedHashMap<String, String>>> mappedTables;

  public CustomUserDetails(User byUserName) {
    this.username = byUserName.getUsername();
    this.password = byUserName.getPassword();

    List<GrantedAuthority> auths = new ArrayList<>();
    for (Role role : byUserName.getRoles()) {
      auths.add(new SimpleGrantedAuthority(role.getName().toUpperCase()));
    }
    this.authorities = auths;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public String getMappedData() {
    return mappedData;
  }

  public void setMappedData(String mappedData) {
    this.mappedData = mappedData;
  }

  public Map<String, Integer> getMap() {
    return map;
  }

  public void setMap(Map<String, Integer> map) {
    this.map = map;
  }

  public Map<Integer, List<String>> getOrderedFKListMap() {
    return orderedFKListMap;
  }

  public void setOrderedFKListMap(Map<Integer, List<String>> orderedFKListMap) {
    this.orderedFKListMap = orderedFKListMap;
  }

  public List<LinkedHashMap<String, LinkedHashMap<String, String>>> getMappedTables() {
    return mappedTables;
  }

  public void setMappedTables(
      List<LinkedHashMap<String, LinkedHashMap<String, String>>> mappedTables) {
    this.mappedTables = mappedTables;
  }
}
