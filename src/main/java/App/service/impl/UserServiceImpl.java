package App.service.impl;

import App.entity.UserEntity;
import App.repository.UserRepository;
import App.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public void createUser(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    @Override
    public List<UserEntity> findAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll();
        return userEntities;
    }

    @Override
    public UserEntity findUserByNickname(String nickname) {
        UserEntity userEntity = userRepository.findUserByNicknameIgnoreCase(nickname);
        return userEntity;
    }

    @Override
    public UserEntity findByEmail(String email){return userRepository.findByEmailIgnoreCase(email);}
}