package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;

public class UserGetDTO {

  private Long id;
  private String pwd;
  private String username;
  private UserStatus status;

  private LocalDate creationdate;

  private LocalDate birthdate;

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

    // 构造函数、Getter和Setter
  public LocalDate getCreationdate() {
      return creationdate;
  }

  public void setCreationdate(LocalDate creationdate) {
      this.creationdate = creationdate;
  }

  public LocalDate getBirthdate() {
      return birthdate;
  }

  public void setBirthdate(LocalDate birthdate) {
      this.birthdate = birthdate;
  }
}
