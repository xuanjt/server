package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetFullDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetReservedDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically
 * transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g.,
 * UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for
 * creating information (POST).
 */
@Mapper
public interface DTOMapper {

  DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

  // only define the function convertUserPostDTOtoEntity, but didn't implement it

  @Mapping(source = "username", target = "username")
  @Mapping(source = "pwd", target = "pwd")
  @Mapping(source = "birthdate", target = "birthdate")
  // @Mapping(source = "token", target = "token")
  // @Mapping(source = "status", target = "status")
  User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "status", target = "status")
  @Mapping(source = "creationdate", target = "creationdate")
  @Mapping(source = "birthdate", target = "birthdate")
  UserGetReservedDTO convertEntityToUserGetReservedDTO(User user);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "token", target = "token")
  @Mapping(source = "status", target = "status")
  @Mapping(source = "creationdate", target = "creationdate")
  @Mapping(source = "birthdate", target = "birthdate")
  UserGetFullDTO convertEntityToUserGetFullDTO(User user);
}
