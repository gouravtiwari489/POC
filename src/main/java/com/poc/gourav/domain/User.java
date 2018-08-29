package com.poc.gourav.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User implements Serializable {

  private String id;
  private String username;
  private String password;
  //private List<Role> roles;
  private Role role;

  public User() {}

  public User(String username, String password /*, List<Role> roles*/) {
    this.username = username;
    this.password = password;
    //this.roles = roles;
  }

  @Id
  @Column(name = "id", unique = true, nullable = false)
  //@GeneratedValue(strategy=GenerationType.IDENTITY)
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Column(name = "user_name")
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Column(name = "password")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  /*@Column(name = "role_id")
  public Integer getRoleId() {
  	return roleId;
  }

  public void setRoleId(Integer roleId) {
  	this.roleId = roleId;
  }*/

  /*@Transient
  public List<Role> getRoles() {
  	return roles;
  }

  public void setRoles(List<Role> roles) {
  	this.roles = roles;
  }*/

  @ManyToOne
  @JoinColumn(name = "role_id")
  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }
}
