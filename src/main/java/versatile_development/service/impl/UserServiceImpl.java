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
import versatile_development.utils.ObjectMapperUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Value("${host.url}")
    String hostUrl;
    private UserRepository userRepository;
    private EmailService emailService;
    private PasswordEncoder passwordEncoder;
    private ObjectMapperUtils modelMapper;

    @Autowired
    UserServiceImpl(@Qualifier(value = "userRepository") UserRepository userRepository,
                    @Qualifier(value = "emailServiceImpl") EmailService emailService,
                    @Qualifier(value = "encoder") PasswordEncoder passwordEncoder,
                    @Qualifier(value = "objectMapperUtils") ObjectMapperUtils modelMapper){
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<UserDTO> findAllUsers(Sort sort) {
        List<UserEntity> userEntities = userRepository.findAll(sort);
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
        return userDTO != null &&
                (userRepository.existsByEmailIgnoreCase(userDTO.getEmail()) || userRepository.existsByNickname(userDTO.getNickname()));
    }

    @Override
    public UserDTO findByConfirmationToken(String confirmationToken) {
        return entityToDTOMapper(userRepository.findByConfirmationToken(confirmationToken));
    }

    @Override
    public UserDTO findByNickname(String nickname) {
        return entityToDTOMapper(userRepository.findByNicknameIgnoreCase(nickname));
    }

    @Override
    @Transactional
    public void updateUser(UserDTO userToUpdate) {
        if(findByEmail(userToUpdate.getEmail()) != null){
            userRepository.save(DTOToEntityMapper(userToUpdate));
        }
    }

    @SneakyThrows
    @Override
    public void updateUserInformationFromSettings(UserForUpdating user, String nickname) {
        if (user == null) throw new EmptyUserDataException();
        UserDTO userToUpdate = entityToDTOMapper(userRepository.findByNickname(nickname));
        SimpleDateFormat DateFor = new SimpleDateFormat("yyyy-MM-dd");

        if (user.getFirstname() != null && user.getFirstname() != "")userToUpdate.setFirstname(user.getFirstname());
        if (user.getLastname() != null && user.getLastname() != "")userToUpdate.setLastname(user.getLastname());
        if (user.getEmail() != null && user.getEmail() != "")userToUpdate.setEmail(user.getEmail());
        if (user.getPassword() != null && user.getPassword() != "")userToUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getGender() != null && user.getGender() != "")userToUpdate.setGender(user.getGender());
        if (user.getNationality() != null && user.getNationality() != "")userToUpdate.setNationality(user.getNationality());
        if (user.getAboutUser() != null && user.getAboutUser() != "")userToUpdate.setAboutUser(user.getAboutUser());
        if (user.getBirthday() != null && user.getBirthday() != ""){
            userToUpdate.setBirthday(DateFor.parse(user.getBirthday()));

            Date now = new Date();
            DateTime d1 = new DateTime(DateFor.parse(user.getBirthday()));
            DateTime d2 = new DateTime(now);
            int age = Math.abs(Years.yearsBetween(d2, d1).getYears());
            userToUpdate.setAge(age);
        }

        updateUser(userToUpdate);
    }

    @Override
    @Transactional
    public void deleteAccountByNickname(String nickname) {
        Jedis jedis = new Jedis();

        if (jedis.get(nickname + Constants.USER_LOCALE_EXTENSION) != null)jedis.del(nickname + Constants.USER_LOCALE_EXTENSION);
        userRepository.deleteByNickname(nickname);
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        UserEntity user = userRepository.findByNicknameIgnoreCase(username);

        if (user == null)throw new UsernameNotFoundException("No such user.");
        else return user;
    }

    public UserDTO entityToDTOMapper(UserEntity userEntity){
        if (userEntity != null){
            UserDTO userDTO = modelMapper.map(userEntity, UserDTO.class);
            userDTO.setId(userEntity.getId());
            return userDTO;
        }else return null;
    }

    public UserEntity DTOToEntityMapper(UserDTO userDTO){
        if (userDTO == null)return null;
        else return modelMapper.map(userDTO, UserEntity.class);
    }

    @Transactional
    public HttpStatus register(UserDTO userDTO) {
        try{
            Date creationDate = new Date();

            userDTO.setCreationDate(creationDate);
            userDTO.setTokenExpiration(new Date(creationDate.getTime() + Constants.DAY));
            userDTO.setEmail(userDTO.getEmail().toLowerCase());
            userDTO.setActivated(false);
            userDTO.setConfirmationToken(UUID.randomUUID().toString());
            userDTO.setRoles(Collections.singleton(Role.USER));

            UserEntity userEntity = DTOToEntityMapper(userDTO);
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            userRepository.save(userEntity);

            log.info(userDTO.getNickname() + " has registered.");

            String message = String.format(Constants.EMAIL_MESSAGE, userDTO.getNickname(), hostUrl, userDTO.getConfirmationToken());
            emailService.sendEmail(userDTO.getEmail(), userDTO.getConfirmationToken(), message);

            log.info(String.format(userDTO.getNickname() + " activation link: " + "%sconfirm?token=%s", hostUrl, userDTO.getConfirmationToken()));
            return HttpStatus.CREATED;
        } catch (MailException ex) {
            return HttpStatus.CONFLICT;
        }
    }
}