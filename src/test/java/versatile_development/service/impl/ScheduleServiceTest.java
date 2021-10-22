package versatile_development.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import versatile_development.entity.UserEntity;
import versatile_development.repository.UserRepository;
import versatile_development.service.UserService;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    private ScheduleService scheduleService;

    @BeforeEach
    void initService() {
        userRepository = mock(UserRepository.class);
        userService = mock(UserService.class);
        scheduleService = new ScheduleService(userRepository, userService);
    }

    @Test
    void deleteAllUsersWithExpiredActivation() {
        var firstUser = new UserEntity();
        firstUser.setActivated(false);
        firstUser.setNickname("user1");

        var secondUser = new UserEntity();
        secondUser.setActivated(true);
        secondUser.setNickname("user2");

        var thirdUser = new UserEntity();
        thirdUser.setActivated(false);
        thirdUser.setNickname("user3");

        when(userRepository.findAllByTokenExpirationLessThanCurrentTime()).
                thenReturn(Arrays.asList(firstUser, secondUser, thirdUser));
        doNothing().when(userService).deleteAccountByNickname(anyString());

        scheduleService.deleteAllUsersWithExpiredActivation();

        verify(userService, atLeastOnce()).deleteAccountByNickname(anyString());
    }

    @Test
    void ageIncrementer() {
        var userAge = 18;
        var userEntity = new UserEntity();
        userEntity.setAge(userAge);

        when(userRepository.save(any(UserEntity.class))).thenReturn(null);
        when(userRepository.findAllByBirthday(anyInt(), anyInt())).thenReturn(Arrays.asList(userEntity));

        scheduleService.ageIncrementer();

        assertEquals(++userAge, userEntity.getAge());
        verify(userRepository).findAllByBirthday(anyInt(), anyInt());
        verify(userRepository).save(any(UserEntity.class));
    }
}