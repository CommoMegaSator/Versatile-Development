package versatile_development.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import versatile_development.domain.dto.UserDTO;
import versatile_development.entity.UserEntity;
import versatile_development.repository.UserRepository;
import versatile_development.service.EmailService;
import versatile_development.service.UserService;
import versatile_development.utils.ObjectMapperUtils;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@Slf4j
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    private UserRepository userRepository;
    private EmailService emailService;
    private PasswordEncoder passwordEncoder;
    private ObjectMapperUtils modelMapper;

    private UserEntity user1;
    private UserEntity user2;

    @BeforeEach
    void initService() {
        userRepository = mock(UserRepository.class);
        emailService = mock(EmailService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        modelMapper = mock(ObjectMapperUtils.class);
        userService = new UserServiceImpl(userRepository, emailService, passwordEncoder, modelMapper);
    }

    @Test
    void verifyMethodFindAllUsersUsed() {
        userService.findAllUsers(Sort.by("id"));
        verify(userRepository).findAll(Sort.by("id"));
    }

    @Test
    void findByEmailTest() {
        userService.findByEmail(anyString());
        verify(userRepository, times(1)).findByEmailIgnoreCase(anyString());
    }

    @Test
    public void testEmptyUserExists() {
        when(this.userRepository.existsByNickname(anyString())).thenReturn(true);
        when(this.userRepository.existsByEmailIgnoreCase(anyString())).thenReturn(false);
        assertFalse(this.userService.userExists(null));
    }

    @Test
    void findAllUsers() {
        final int expectedUserCount = 2;
        when(userRepository.findAll(Sort.by("id"))).thenReturn(Arrays.asList(user1, user2));
        when(modelMapper.map(UserEntity.class, UserDTO.class)).thenReturn(new UserDTO());

        int userDtoCount = userService.findAllUsers(Sort.by("id")).size();
        assertEquals(expectedUserCount, userDtoCount);
    }
}