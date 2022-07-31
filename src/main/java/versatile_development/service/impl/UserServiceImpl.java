package versatile_development.service.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import versatile_development.constants.Constants;
import versatile_development.domain.Role;
import versatile_development.domain.dto.UserDTO;
import versatile_development.domain.dto.UserForUpdating;
import versatile_development.entity.UserEntity;
import versatile_development.exception.EmptyUserDataException;
import versatile_development.repository.UserRepository;
import versatile_development.service.EmailService;
import versatile_development.service.UserService;
import versatile_development.utils.UserMapper;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Value("${host.url}")
    String hostUrl;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Autowired
    UserServiceImpl(@Qualifier(value = "userRepository") UserRepository userRepository,
                    @Qualifier(value = "emailServiceImpl") EmailService emailService,
                    @Qualifier(value = "encoder") PasswordEncoder passwordEncoder,
                    UserMapper userMapper) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserDTO> findAllUsers(Sort sort) {
        var userEntities = userRepository.findAll(sort);
        var userDTOs = userMapper.toDtoList(userEntities);

        return userDTOs;
    }

    @Override
    public UserDTO findByEmail(String email) {
        return userMapper.toDto(userRepository.findByEmailIgnoreCase(email));
    }

    @Override
    public boolean userExists(UserDTO userDTO) {
        return userDTO != null &&
                (userRepository.existsByEmailIgnoreCase(userDTO.getEmail()) || userRepository.existsByNickname(userDTO.getNickname()));
    }

    @Override
    public UserDTO findByConfirmationToken(String confirmationToken) {
        return userMapper.toDto(userRepository.findByConfirmationToken(confirmationToken));
    }

    @Override
    public UserDTO findByNickname(String nickname) {
        return userMapper.toDto(userRepository.findByNicknameIgnoreCase(nickname));
    }

    @Override
    @Transactional
    public void updateUser(UserDTO userToUpdate) {
        if (findByEmail(userToUpdate.getEmail()) != null) {
            userRepository.save(userMapper.toEntity(userToUpdate));
        }
    }

    @SneakyThrows
    @Override
    @Transactional
    public void updateUserInformationFromSettings(UserForUpdating user, String nickname) {
        if (user == null) throw new EmptyUserDataException();
        var userToUpdate = userMapper.toDto(userRepository.findByNickname(nickname));
        var DateFor = new SimpleDateFormat("yyyy-MM-dd");

        if (user.getFirstname() != null && !user.getFirstname().equals(""))
            userToUpdate.setFirstname(user.getFirstname());
        if (user.getLastname() != null && !user.getLastname().equals("")) userToUpdate.setLastname(user.getLastname());
        if (user.getEmail() != null && !user.getEmail().equals("")) userToUpdate.setEmail(user.getEmail());
        if (user.getPassword() != null && !user.getPassword().equals(""))
            userToUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getGender() != null && !user.getGender().equals("")) userToUpdate.setGender(user.getGender());
        if (user.getNationality() != null && !user.getNationality().equals(""))
            userToUpdate.setNationality(user.getNationality());
        if (user.getAboutUser() != null && !user.getAboutUser().equals(""))
            userToUpdate.setAboutUser(user.getAboutUser());
        if (user.getBirthday() != null && !user.getBirthday().equals("")) {
            userToUpdate.setBirthday(DateFor.parse(user.getBirthday()));

            var now = new Date();
            var d1 = new DateTime(DateFor.parse(user.getBirthday()));
            var d2 = new DateTime(now);
            var age = Math.abs(Years.yearsBetween(d2, d1).getYears());
            userToUpdate.setAge(age);
        }

        updateUser(userToUpdate);
    }

    @Override
    @Transactional
    public void deleteAccountByNickname(String nickname) {
        var jedis = new Jedis();

        if (jedis.get(nickname + Constants.USER_LOCALE_EXTENSION) != null)
            jedis.del(nickname + Constants.USER_LOCALE_EXTENSION);
        userRepository.deleteByNickname(nickname);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        var user = userRepository.findByNicknameIgnoreCase(username);

        if (user == null) throw new UsernameNotFoundException("No such user.");
        else return user;
    }

    @Transactional
    public HttpStatus register(UserDTO userDTO) {
        try {
            var creationDate = new Date();

            userDTO.setCreationDate(creationDate);
            userDTO.setTokenExpiration(new Date(creationDate.getTime() + Constants.DAY));
            userDTO.setEmail(userDTO.getEmail().toLowerCase());
            userDTO.setActivated(false);
            userDTO.setConfirmationToken(UUID.randomUUID().toString());
            userDTO.setRoles(Collections.singleton(Role.USER));

            var userEntity = userMapper.toEntity(userDTO);
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            userRepository.save(userEntity);

            log.info(userDTO.getNickname() + " has registered.");

            var message = String.format(Constants.EMAIL_MESSAGE, userDTO.getNickname(), hostUrl, userDTO.getConfirmationToken());
            emailService.sendEmail(userDTO.getEmail(), userDTO.getConfirmationToken(), message);

            log.info(String.format(userDTO.getNickname() + " activation link: " + "%sconfirm?token=%s", hostUrl, userDTO.getConfirmationToken()));
            return HttpStatus.CREATED;
        } catch (MailException ex) {
            return HttpStatus.CONFLICT;
        }
    }
}