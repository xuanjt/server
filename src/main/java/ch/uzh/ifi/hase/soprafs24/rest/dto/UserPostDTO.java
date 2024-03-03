package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class UserPostDTO {

  private String pwd;

  private String name;

  private String username;

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
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
