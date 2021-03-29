package versatile_development.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import versatile_development.repository.UserRepository;
import versatile_development.service.UserService;
import versatile_development.utils.ObjectMapperUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
        userService.findAllUsers();
        verify(userRepository).findAll();
    }
}