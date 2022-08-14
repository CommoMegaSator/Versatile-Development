package versatile_development.service.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailSendException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import redis.clients.jedis.Jedis;
import versatile_development.constants.Constants;
import versatile_development.domain.dto.UserDTO;
import versatile_development.domain.dto.UserForUpdating;
import versatile_development.entity.UserEntity;
import versatile_development.repository.UserRepository;
import versatile_development.service.EmailService;
import versatile_development.service.UserService;
import versatile_development.mapper.UserMapper;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    private UserRepository userRepository;
    private EmailService emailService;


    private PasswordEncoder passwordEncoder;
    private UserMapper userMapper;

    @BeforeEach
    void initService() {
        userRepository = mock(UserRepository.class);
        emailService = mock(EmailService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userMapper = mock(UserMapper.class);
        userService = new UserServiceImpl(userRepository, emailService, passwordEncoder, userMapper);
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
    void userExistsByEmail() {
        var userDTO = new UserDTO();
        userDTO.setEmail("email");
        userDTO.setNickname("nickname");

        when(userRepository.existsByEmailIgnoreCase(anyString())).thenReturn(true);
        when(userRepository.existsByNickname(anyString())).thenReturn(false);
        assertTrue(userService.userExists(userDTO));
    }

    @Test
    void userExistsByNickname() {
        var userDTO = new UserDTO();
        userDTO.setEmail("email");
        userDTO.setNickname("nickname");

        when(userRepository.existsByEmailIgnoreCase(anyString())).thenReturn(false);
        when(userRepository.existsByNickname(anyString())).thenReturn(true);
        assertTrue(userService.userExists(userDTO));
    }

    @Test
    void userNotExists() {
        var userDTO = new UserDTO();
        userDTO.setEmail("email");
        userDTO.setNickname("nickname");

        when(userRepository.existsByEmailIgnoreCase(anyString())).thenReturn(false);
        when(userRepository.existsByNickname(anyString())).thenReturn(false);
        assertFalse(userService.userExists(userDTO));

        assertFalse(userService.userExists(null));
    }

    @Test
    void registrationTest() {
        var userDTO = new UserDTO();
        userDTO.setNickname("nickname");
        userDTO.setEmail("test@test.com");
        userDTO.setPassword("passWord1!");

        var userEntity = new UserEntity();
        userEntity.setNickname("nickname");
        userEntity.setEmail("test@test.com");
        userEntity.setPassword("passWord1!");

        when(userMapper.dtoToEntity(any())).thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(null);
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        assertEquals(HttpStatus.CREATED, userService.register(userDTO));

        verify(userRepository).save(any(UserEntity.class));
        verify(emailService).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void registrationWithException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setNickname("nickname");
        userDTO.setEmail("test@test.com");
        userDTO.setPassword("passWord1!");

        UserEntity userEntity = new UserEntity();
        userEntity.setNickname("nickname");
        userEntity.setEmail("test@test.com");
        userEntity.setPassword("passWord1!");

        when(userMapper.dtoToEntity(any())).thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(null);
        doThrow(new MailSendException("Can`t send email")).when(emailService).sendEmail(anyString(), anyString(), anyString());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, userService.register(userDTO));
    }

    @Test
    void loadUserByUsernameNotExists() {
        when(userRepository.findByNicknameIgnoreCase(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("nickname"));
        verify(userRepository).findByNicknameIgnoreCase(anyString());
    }

    @Test
    void updateUserInformationFromSettings() {
        var user = new UserForUpdating();
        user.setEmail("some@email.com");

        var userEntity = new UserEntity();
        userEntity.setNickname("nickname");
        userEntity.setEmail("some@email.com");

        var userDTO = new UserDTO();
        userDTO.setNickname("nickname");
        userDTO.setEmail("some@email.com");

        when(userMapper.entityToDto(any())).thenReturn(userDTO);
        when(userRepository.save(any(UserEntity.class))).thenReturn(null);
        when(userRepository.findByNickname(anyString())).thenReturn(userEntity);

        userService.updateUserInformationFromSettings(user, "nickname");

        verify(userRepository).findByNickname(any());
        verify(userRepository).findByEmailIgnoreCase(any());
    }

    @Test
    void updateUserTest() {
        var userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setNickname("nickname");
        userDTO.setEmail("some@email.com");

        var userEntity = new UserEntity();
        userEntity.setNickname("nickname");
        userEntity.setEmail("some@email.com");

        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(null);
        when(userMapper.entityToDto(userEntity)).thenReturn(userDTO);
        when(userMapper.dtoToEntity(any(UserDTO.class))).thenReturn(userEntity);

        userService.updateUser(userDTO);

        verify(userRepository).save(any());
    }

    @Test
    @SneakyThrows
    void testUpdateUserAge() {
        var userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setNickname("nickname");
        userDTO.setEmail("some@email.com");

        var userEntity = new UserEntity();
        userEntity.setNickname("nickname");
        userEntity.setEmail("some@email.com");

        var userForUpdating = new UserForUpdating();
        userForUpdating.setEmail(userDTO.getEmail());
        userForUpdating.setBirthday("1998-01-01");

        var DateFor = new SimpleDateFormat("yyyy-MM-dd");
        var now = new Date();
        var date1 = new DateTime(DateFor.parse(userForUpdating.getBirthday()));
        var date2 = new DateTime(now);

        when(userRepository.findByNickname(anyString())).thenReturn(userEntity);
        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(userEntity);
        when(userMapper.entityToDto(userEntity)).thenReturn(userDTO);
        when(userMapper.dtoToEntity(any(UserDTO.class))).thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(null);

        userService.updateUserInformationFromSettings(userForUpdating, userDTO.getNickname());

        assertEquals(Math.abs(Years.yearsBetween(date1, date2).getYears()), userDTO.getAge());
        verify(userRepository).save(any());
    }

    @Test
    void testLoadUserByUsernameExists() {
        var userEntity = new UserEntity();
        userEntity.setNickname("nickname");
        userEntity.setEmail("some@email.com");

        when(userRepository.findByNicknameIgnoreCase(anyString())).thenReturn(userEntity);

        assertEquals(userEntity, userService.loadUserByUsername("nickname"));
    }

    @Test
    void findByConfirmationTokenTest() {
        var userDTO = new UserDTO();
        userDTO.setConfirmationToken("token");
        userDTO.setNickname("nickname");
        userDTO.setEmail("some@email.com");

        var userEntity = new UserEntity();
        userEntity.setConfirmationToken("token");
        userEntity.setNickname("nickname");
        userEntity.setEmail("some@email.com");

        when(userRepository.findByConfirmationToken(anyString())).thenReturn(userEntity);
        when(userMapper.entityToDto(userEntity)).thenReturn(userDTO);
        when(userMapper.dtoToEntity(any(UserDTO.class))).thenReturn(userEntity);

        userService.findByConfirmationToken(userDTO.getNickname());

        verify(userRepository).findByConfirmationToken(anyString());
    }

    @Test
    void findByNicknameTest() {
        var userDTO = new UserDTO();
        userDTO.setNickname("nickname");
        userDTO.setEmail("some@email.com");

        var userEntity = new UserEntity();
        userEntity.setNickname("nickname");
        userEntity.setEmail("some@email.com");

        when(userRepository.findByNicknameIgnoreCase(anyString())).thenReturn(userEntity);
        when(userMapper.entityToDto(userEntity)).thenReturn(userDTO);
        when(userMapper.dtoToEntity(any(UserDTO.class))).thenReturn(userEntity);

        userService.findByNickname(userDTO.getNickname());

        verify(userRepository).findByNicknameIgnoreCase(anyString());
    }
}