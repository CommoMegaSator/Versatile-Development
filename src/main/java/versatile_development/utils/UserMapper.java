package versatile_development.utils;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import versatile_development.domain.dto.UserDTO;
import versatile_development.entity.UserEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toEntity(UserDTO userDTO);

    UserDTO toDto(UserEntity userEntity);

    List<UserDTO> toDtoList(List<UserEntity> userEntityList);

    List<UserEntity> toEntityList(List<UserDTO> userDTOList);
}
