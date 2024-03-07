package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;

public class UserPostDTO {

  private String pwd;

  private String username;

  private LocalDate birthdate;

  private UserStatus status;

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

  public LocalDate getBirthdate() {
      return birthdate;
  }

  public void setBirthdate(LocalDate birthdate) {
      this.birthdate = birthdate;
  }

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }
}
