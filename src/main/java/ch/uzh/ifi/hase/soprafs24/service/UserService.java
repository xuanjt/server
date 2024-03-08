package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
// @Transactional: Specifies that a method or class should be executed within a transactional context.
// representing a single, logical unit of work that executes a series of operations on a database or another resource. 
@Transactional
public class UserService {

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;

  // The @Autowired annotation in Spring Framework is used for automatic dependency injection. 
  @Autowired
  public UserService(@Qualifier("userRepository") UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<User> getUsers() {
    return this.userRepository.findAll();
  }

  // create user
  public User createUser(User newUser) {
    checkIfUserExists(newUser);
    newUser.setToken(UUID.randomUUID().toString());
    newUser.setStatus(UserStatus.ONLINE);
    newUser = userRepository.save(newUser);
    userRepository.flush();
    log.debug("Created Information for User: {}", newUser);
    return newUser;
  }

  //authenticate user token
  public boolean authenticateUser(String authentication, User visitedUser) {
    if (authentication == ""){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Current user with empty token.");}
    if (authentication.equals(visitedUser.getToken())){
      return true;
    }
    else return false;
  }

  // validate user when login
  public User validateUser(User toBeValidatedUser){
    User possibleUser = userRepository.findByUsernameAndPwd(toBeValidatedUser.getUsername(), toBeValidatedUser.getPwd());
    if (possibleUser != null){
      possibleUser.setToken(UUID.randomUUID().toString());
      return possibleUser;
    }else{
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid username or password.");
    }
  }

  // get user by userId
  public User getUserById(Long userId) {
    return userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
  }

  // update user profile
  public void updateUserInfo(User user, User userInput){
    if (userInput.getUsername() != null) {
      User userByUsername = userRepository.findByUsername(userInput.getUsername());
      if (userByUsername != null && userByUsername.getId() != user.getId()){
        throw new ResponseStatusException(HttpStatus.CONFLICT, "The username provided is not unique. Therefore, the user could not be created!");
      }
      user.setUsername(userInput.getUsername());}
    if (userInput.getBirthdate() != null) {user.setBirthdate(userInput.getBirthdate());}
    if (userInput.getStatus() != null) {
      user.setStatus(userInput.getStatus());}
    user = userRepository.save(user);
    userRepository.flush();
  }

  //manage user status
  public User offLineUser(User user){
    user.setToken(null);
    user.setStatus(UserStatus.OFFLINE);
    return user;
  }

  public User onlineUser(User user){
    user.setStatus(UserStatus.ONLINE);
    return user;
  }

  /**
   * This is a helper method that will check the uniqueness criteria of the
   * username and the name
   * defined in the User entity. The method will do nothing if the input is unique
   * and throw an error otherwise.
   *
   * @param userToBeCreated
   * @throws org.springframework.web.server.ResponseStatusException
   * @see User
   */
  private void checkIfUserExists(User userToBeCreated) {
    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
    if (userByUsername != null) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "The username provided is not unique. Therefore, the user could not be created!");
    }
  }


}
