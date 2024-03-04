package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.time.LocalDateTime;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;

public class UserGetDTO {

  private Long id;
  private String name;
  private String pwd;
  private String username;
  private UserStatus status;
  private LocalDateTime creationdate;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPwd() {
    return pwd;
  }

  public void setPwd(String pwd) {
    this.pwd = pwd;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

    // 构造函数、Getter和Setter
  public LocalDateTime getCreationdate() {
      return creationdate;
  }

  public void setCreationdate(LocalDateTime creationdate) {
      this.creationdate = creationdate;
  }
}
