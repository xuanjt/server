package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
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
  // 定义映射接口方法而不提供实现体是一种常见且预期的做法。原因在于MapStruct框架的工作机制和设计目的。
  // MapStruct是一个编译时代码生成工具，旨在简化对象之间的转换工作。
  // 当你使用MapStruct定义了一个映射接口和其方法时，MapStruct会在编译期间自动生成这些方法的实现代码。
  @Mapping(source = "username", target = "username")
  @Mapping(source = "pwd", target = "pwd")
  @Mapping(source = "birthdate", target = "birthdate")
  User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "pwd", target = "pwd")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "status", target = "status")
  @Mapping(source = "creationdate", target = "creationdate")
  UserGetDTO convertEntityToUserGetDTO(User user);
}
