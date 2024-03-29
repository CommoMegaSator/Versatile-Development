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
import versatile_development.utils.ObjectMapperUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
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

        when(modelMapper.map(any(), any())).thenReturn(userEntity);
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

        when(modelMapper.map(any(), any())).thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(null);
        doThrow(new MailSendException("Can`t send email")).when(emailService).sendEmail(anyString(), anyString(), anyString());

        assertEquals(HttpStatus.CONFLICT, userService.register(userDTO));
    }

    @Test
    void deleteAccountByNickname() {
        final var jedis = new Jedis();
        final var nickname = "nicknameForJedisTest";
        final var localeOption = "en_US";
        final var jedisKey = nickname + Constants.USER_LOCALE_EXTENSION;

        doNothing().when(userRepository).deleteByNickname(anyString());

        jedis.set(jedisKey, localeOption);
        assertNotNull(jedis.get(jedisKey));
        userService.deleteAccountByNickname(nickname);

        assertNull(jedis.get(jedisKey));
        verify(userRepository).deleteByNickname(anyString());
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

        when(modelMapper.map(any(), any())).thenReturn(userDTO);
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
        when(modelMapper.map(userEntity, UserDTO.class)).thenReturn(userDTO);
        when(modelMapper.map(any(UserDTO.class), any(UserEntity.class))).thenReturn(userEntity);

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
        when(modelMapper.map(userEntity, UserDTO.class)).thenReturn(userDTO);
        when(modelMapper.map(any(UserDTO.class), any(UserEntity.class))).thenReturn(userEntity);
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
        when(modelMapper.map(userEntity, UserDTO.class)).thenReturn(userDTO);
        when(modelMapper.map(any(UserDTO.class), any(UserEntity.class))).thenReturn(userEntity);

        userService.findByConfirmationToken(userDTO.getNickname());

        verify(userRepository).findByConfirmationToken(anyString());
        verify(modelMapper).map(userEntity, UserDTO.class);
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
        when(modelMapper.map(userEntity, UserDTO.class)).thenReturn(userDTO);
        when(modelMapper.map(any(UserDTO.class), any(UserEntity.class))).thenReturn(userEntity);

        userService.findByNickname(userDTO.getNickname());

        verify(userRepository).findByNicknameIgnoreCase(anyString());
        verify(modelMapper).map(userEntity, UserDTO.class);
    }
}