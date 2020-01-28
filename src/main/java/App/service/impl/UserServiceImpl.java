package App.service.impl;

import App.domain.UserDTO;
import App.entity.UserEntity;
import App.repository.UserRepository;
import App.service.UserService;
import App.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapperUtils modelMapper;

    @Override
    public void createUser(UserDTO userDTO) {
        UserEntity userEntity = DTOToEntityMapper(userDTO);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userRepository.save(userEntity);
    }

    @Override
    public List<UserDTO> findAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll();
        List<UserDTO> userDTOs = new ArrayList<>();

        for (UserEntity userEntity : userEntities){
            userDTOs.add(entityToDTOMapper(userEntity));
        }

        return userDTOs;
    }

    @Override
    public UserDTO findByEmail(String email){
        return entityToDTOMapper(userRepository.findByEmailIgnoreCase(email));
    }

    @Override
    public boolean userExists(UserDTO userDTO) {
        return userRepository.existsByEmailIgnoreCase(userDTO.getEmail()) || userRepository.existsByNickname(userDTO.getNickname());
    }

    @Override
    public UserDTO findByConfirmationToken(String confirmationToken) {
        return entityToDTOMapper(userRepository.findByConfirmationToken(confirmationToken));
    }

    @Override
    public void updateUser(UserDTO userToUpdate) {
        if(findByEmail(userToUpdate.getEmail()) != null){
            userRepository.save(DTOToEntityMapper(userToUpdate));
        }
    }

    public UserDTO entityToDTOMapper(UserEntity userEntity){
        try{
            UserDTO userDTO = modelMapper.map(userEntity, UserDTO.class);
            userDTO.setId(userEntity.getId());
            return userDTO;
        }catch (IllegalArgumentException iae){
            return null;
        }
    }

    public UserEntity DTOToEntityMapper(UserDTO userDTO){
        return modelMapper.map(userDTO, UserEntity.class);
    }
}