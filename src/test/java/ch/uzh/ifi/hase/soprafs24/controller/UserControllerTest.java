package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */
// @DataJpaTest
@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @Test
  public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
    // given
    User user = new User();
    user.setPwd("pppwwwddd");
    user.setUsername("firstname@lastname");
    user.setStatus(UserStatus.OFFLINE);

    List<User> allUsers = Collections.singletonList(user);

    // this mocks the UserService -> we define above what the userService should
    // return when getUsers() is called
    given(userService.getUsers()).willReturn(allUsers);

    // when
    MockHttpServletRequestBuilder getRequest = get("/users").contentType(MediaType.APPLICATION_JSON);

    // then
    mockMvc.perform(getRequest).andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].username", is(user.getUsername())))
        .andExpect(jsonPath("$[0].status", is(user.getStatus().toString())));
  }

  @Test
  public void createUser_validInput_ReturnUser() throws Exception {
    // given
    User user = new User();
    user.setId(1L);
    user.setPwd("testPwd");
    user.setUsername("testUsername");
    user.setToken("1");
    user.setStatus(UserStatus.ONLINE);
    UserPostDTO userPostDTO = new UserPostDTO();
    userPostDTO.setPwd("testPwd");
    userPostDTO.setUsername("testUsername");

    given(userService.createUser(Mockito.any())).willReturn(user);

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userPostDTO));

    // then
    mockMvc.perform(postRequest)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(user.getId().intValue())))
        .andExpect(jsonPath("$.token", is(user.getToken())))
        .andExpect(jsonPath("$.username", is(user.getUsername())))
        .andExpect(jsonPath("$.creationdate", is(user.getCreationdate())))
        .andExpect(jsonPath("$.birthdate", is(user.getBirthdate())))
        .andExpect(jsonPath("$.status", is(user.getStatus().toString())));
  }

  @Test
  public void createUser_whenUsernameExists_thenThrowConflictException() throws Exception {
      UserPostDTO userPostDTO = new UserPostDTO();
      userPostDTO.setUsername("existingUsername");
      userPostDTO.setPwd("password");

      when(userService.createUser(Mockito.any())).thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists."));

      MockHttpServletRequestBuilder postRequest = post("/users")
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(userPostDTO));

      mockMvc.perform(postRequest)
              .andExpect(status().isConflict())
              .andExpect(status().reason( "Username already exists." ))
              .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));
  }

  @Test
  public void getUserProfile_validinput_returnFullUser() throws Exception {
  // given
  User user = new User();
  user.setId(1L);
  user.setPwd("testpwd");
  user.setToken("1");
  user.setUsername("testUsername");
  user.setStatus(UserStatus.ONLINE);
  String token = "1";
  Mockito.when(userService.getUserById(1L)).thenReturn(user);
  Mockito.when(userService.authenticateUser(Mockito.any(), Mockito.any())).thenReturn(true);

  // when
  MockHttpServletRequestBuilder getRequest = get("/users/{userId}", "1")
  .header("authentication", token)  
  .accept(MediaType.APPLICATION_JSON);

  // then
  mockMvc.perform(getRequest)
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(user.getId().intValue())))
      .andExpect(jsonPath("$.username", is(user.getUsername())))
      .andExpect(jsonPath("$.token", is(user.getToken())))
      .andExpect(jsonPath("$.creationdate", is(user.getCreationdate())))
      .andExpect(jsonPath("$.birthdate", is(user.getBirthdate())))
      .andExpect(jsonPath("$.status", is(user.getStatus().toString())));
  }  

  @Test
  public void getUserProfile_invalidinput_thenThrowNotFoundException() throws Exception {
  // given
  User user = new User();
  user.setId(1L);
  user.setPwd("testpwd");
  user.setToken("1");
  user.setUsername("testUsername");
  user.setStatus(UserStatus.ONLINE);
  String token = "1";

  Mockito.when(userService.getUserById(1L)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "UserId cannot be found."));

  // when
  MockHttpServletRequestBuilder getRequest = get("/users/{userId}", "1")
  .header("authentication", token)
  .accept(MediaType.APPLICATION_JSON);

  // then
  mockMvc.perform(getRequest)
  .andExpect(status().isNotFound())
  .andExpect(status().reason( "UserId cannot be found." ))
  .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));
  } 

  @Test
  public void updateUserProfile_validinput_thenRenturnUser() throws Exception {
    String token = "1";
    UserPostDTO userPostDTO = new UserPostDTO();
    Mockito.when(userService.authenticateUser(Mockito.any(),Mockito.any())).thenReturn(true);
    Mockito.doNothing().when(userService).updateUserInfo(Mockito.any(), Mockito.any());
    mockMvc.perform(put("/users/{userId}", 1)
    .header("authentication", token)
    .contentType(MediaType.APPLICATION_JSON)
    .content(asJsonString(userPostDTO))) 
    .andExpect(status().isNoContent()); 
  } 

  @Test
  public void updateUserProfile_invalidinput_thenThrowNotFoundException() throws Exception {

    UserPostDTO userPostDTO = new UserPostDTO();
    String token = "1";
    Mockito.when(userService.getUserById(1L)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "UserId cannot be found."));
    mockMvc.perform(put("/users/{userId}", 1) // 假设 URL 中 {userId} 是要更新的用户 ID
    .header("authentication", token)    
    .contentType(MediaType.APPLICATION_JSON)
    .content(asJsonString(userPostDTO))) // 设置请求的内容类型和请求体
    .andExpect(status().isNotFound())
    .andExpect(status().reason( "UserId cannot be found." ))
    .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));
  } 

  @Test
  public void updateUserProfile_unauthorizeduser_thenThrowUnauthorizedException() throws Exception {

    UserPostDTO userPostDTO = new UserPostDTO();
    User user = new User();
    Mockito.when(userService.getUserById(1L)).thenReturn(user);
    Mockito.when(userService.authenticateUser(Mockito.any(),Mockito.any()))
    .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Current user with empty token."));
    mockMvc.perform(put("/users/{userId}", 1)
    .header("authentication", "")    
    .contentType(MediaType.APPLICATION_JSON)
    .content(asJsonString(userPostDTO)))
    .andExpect(status().isUnauthorized())
    .andExpect(status().reason( "Current user with empty token." ))
    .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));
  } 

  @Test
  public void updateUserProfile_nonauthentication_thenThrowUnauthorizedException() throws Exception {

    UserPostDTO userPostDTO = new UserPostDTO();
    User user = new User();
    Mockito.when(userService.getUserById(1L)).thenReturn(user);
    Mockito.when(userService.authenticateUser(Mockito.any(),Mockito.any())).thenReturn(false);
    mockMvc.perform(put("/users/{userId}", 1)
    .header("authentication", "1")    
    .contentType(MediaType.APPLICATION_JSON)
    .content(asJsonString(userPostDTO)))
    .andExpect(status().isForbidden())
    .andExpect(status().reason( "No access to edit user profile!" ))
    .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));
  }

  @Test
  public void updateUserProfile_missingheader_thenThrowUnauthorizedException() throws Exception {

    UserPostDTO userPostDTO = new UserPostDTO();
    User user = new User();
    mockMvc.perform(put("/users/{userId}", 1) 
    .contentType(MediaType.APPLICATION_JSON)
    .content(asJsonString(userPostDTO)))
    .andExpect(status().isBadRequest());
  }


  /**
   * Helper Method to convert userPostDTO into a JSON string such that the input
   * can be processed
   * Input will look like this: {"name": "Test User", "username": "testUsername"}
   * 
   * @param object
   * @return string
   */
  private String asJsonString(final Object object) {
    try {
      return new ObjectMapper().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          String.format("The request body could not be created.%s", e.toString()));
    }
  }
}