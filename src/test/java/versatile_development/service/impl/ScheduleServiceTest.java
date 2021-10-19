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
        UserEntity first = new UserEntity();
        first.setActivated(false);
        first.setNickname("user1");

        UserEntity second = new UserEntity();
        second.setActivated(true);
        first.setNickname("user2");

        UserEntity third = new UserEntity();
        first.setActivated(false);
        first.setNickname("user3");

        when(userRepository.findAllByTokenExpirationLessThanCurrentTime()).
                thenReturn(Arrays.asList(first, second, third));
        doNothing().when(userService).deleteAccountByNickname(anyString());

        scheduleService.deleteAllUsersWithExpiredActivation();

        verify(userService, atLeastOnce()).deleteAccountByNickname(anyString());
    }
}