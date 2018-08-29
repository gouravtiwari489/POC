package com.osi.datagen.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class Role implements Serializable {

  private Integer roleId;
  private String userRole;

  public Role() {}

  public Role(String userRole, Integer roleId) {
    this.userRole = userRole;
    this.roleId = roleId;
  }

  @Id
  @Column(name = "role_id")
  public Integer getRoleId() {
    return roleId;
  }

  public void setRoleId(Integer roleId) {
    this.roleId = roleId;
  }

  @Column(name = "user_role")
  public String getUserRole() {
    return userRole;
  }

  public void setUserRole(String userRole) {
    this.userRole = userRole;
  }
}
