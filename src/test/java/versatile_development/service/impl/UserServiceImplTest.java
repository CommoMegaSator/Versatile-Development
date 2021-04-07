package versatile_development.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import versatile_development.entity.UserEntity;
import versatile_development.repository.UserRepository;
import versatile_development.service.UserService;
import versatile_development.utils.ObjectMapperUtils;

import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Slf4j
class UserServiceImplTest {

    @MockBean
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ObjectMapperUtils modelMapper;

    @BeforeEach
    void initService() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository, passwordEncoder, modelMapper);
    }

    @Test
    void verifyMethodFindAllUsersUsed() {
        userService.findAllUsers(Sort.by("id"));
        verify(userRepository).findAll(Sort.by("id"));
    }
}