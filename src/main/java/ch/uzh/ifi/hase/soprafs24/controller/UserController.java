package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetFullDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetReservedDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
@RestController
public class UserController {

  private final UserService userService;

  UserController(UserService userService) {
    this.userService = userService;
  }

  // update user profile
  @PutMapping("/users/{userId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateUserProfile(@PathVariable Long userId, @RequestBody UserPostDTO userPostDTO, @RequestHeader String authentication) {
    User user = userService.getUserById(userId);
    boolean isAuthenticated = userService.authenticateUser(authentication, user);
    if (!isAuthenticated){
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to edit user profile!");
    }
    User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
    userService.updateUserInfo(user, userInput);
  }


  // get user profile
  @GetMapping("/users/{userId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public ResponseEntity<?> getUserProfile(@PathVariable Long userId, @RequestHeader String authentication) {
      User user = userService.getUserById(userId);
      if (userService.authenticateUser(authentication, user)){
        UserGetFullDTO fullDTO = DTOMapper.INSTANCE.convertEntityToUserGetFullDTO(user);
        return ResponseEntity.ok(fullDTO);
      }else{
        UserGetReservedDTO reservedDTO = DTOMapper.INSTANCE.convertEntityToUserGetReservedDTO(user);
        return ResponseEntity.ok(reservedDTO);
      }
  }


  // get user list
  @GetMapping("/users")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<UserGetReservedDTO> getAllUsers() {
    List<User> users = userService.getUsers();
    List<UserGetReservedDTO> userGetDTOs = new ArrayList<>();
    for (User user : users) {
      userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetReservedDTO(user));
    }
    return userGetDTOs;
  }


  // create user
  @PostMapping("/users")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public UserGetFullDTO createUser(@RequestBody UserPostDTO userPostDTO) {
    User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
    User createdUser = userService.createUser(userInput);
    return DTOMapper.INSTANCE.convertEntityToUserGetFullDTO(createdUser);
  }

  // user login
  @PostMapping("/users/loggers")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserGetFullDTO validateUser(@RequestBody UserPostDTO userPostDTO) {
      User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
      User validUser = userService.validateUser(userInput);
      validUser = userService.onlineUser(validUser);
      return DTOMapper.INSTANCE.convertEntityToUserGetFullDTO(validUser);
  }
}
