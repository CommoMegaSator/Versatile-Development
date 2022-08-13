package versatile_development.mapper;

import org.mapstruct.Mapper;
import versatile_development.domain.dto.UserDTO;
import versatile_development.entity.UserEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity dtoToEntity(UserDTO userDTO);

    UserDTO entityToDto(UserEntity userEntity);

    List<UserDTO> entityListToDtoList(List<UserEntity> userEntityList);

    List<UserEntity> dtoListToEntityList(List<UserDTO> userDTOList);
}
