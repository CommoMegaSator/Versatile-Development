package versatile_development.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import versatile_development.repository.UserRepository;
import versatile_development.service.EmailService;
import versatile_development.service.UserService;
import versatile_development.utils.ObjectMapperUtils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@Slf4j
class UserServiceImplTest {

    @MockBean
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Mock
    private EmailService emailService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ObjectMapperUtils modelMapper;

    @BeforeEach
    void initService() {
        MockitoAnnotations.initMocks(this);
        userRepository = mock(UserRepository.class);
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
}