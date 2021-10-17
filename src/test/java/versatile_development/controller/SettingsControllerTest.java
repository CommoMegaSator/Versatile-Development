package versatile_development.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import versatile_development.domain.dto.UserDTO;
import versatile_development.domain.dto.UserForUpdating;
import versatile_development.entity.UserEntity;
import versatile_development.exception.EmptyUserDataException;
import versatile_development.service.UserService;

@ContextConfiguration(classes = {SettingsController.class})
@ExtendWith(SpringExtension.class)
public class SettingsControllerTest {
    @Autowired
    private SettingsController settingsController;

    @MockBean(name = "userServiceImpl")
    private UserService userService;

    @Test
    public void testDeleteAccount() throws Exception {
        when(this.userService.findByNickname(anyString())).thenReturn(new UserDTO());
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders.formLogin();
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.settingsController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testUpdateUserInfoWithOk() {
        UserService userService = mock(UserService.class);
        doNothing().when(userService).updateUserInformationFromSettings((UserForUpdating) any(), anyString());
        SettingsController settingsController = new SettingsController(userService);
        UserForUpdating user = new UserForUpdating("Jane", "Doe", "jane.doe@example.org", "iloveyou", "Birthday", "Gender",
                "Nationality", "About User");

        UserEntity userEntity = new UserEntity();
        userEntity.setNickname("42");
        ResponseEntity actualUpdateUserInfoResult = settingsController.updateUserInfo(user, userEntity);
        assertNull(actualUpdateUserInfoResult.getBody());
        assertEquals("<200 OK OK,[]>", actualUpdateUserInfoResult.toString());
        assertEquals(HttpStatus.OK, actualUpdateUserInfoResult.getStatusCode());
        verify(userService).updateUserInformationFromSettings(any(), anyString());
    }

    @Test
    public void testUpdateUserInfoWithNoContent() {
        UserService userService = mock(UserService.class);
        doThrow(new EmptyUserDataException()).when(userService)
                .updateUserInformationFromSettings(any(), anyString());
        SettingsController settingsController = new SettingsController(userService);
        UserForUpdating user = new UserForUpdating("Jane", "Doe", "jane.doe@example.org", "iloveyou", "Birthday", "Gender",
                "Nationality", "About User");

        UserEntity userEntity = new UserEntity();
        userEntity.setNickname("42");
        ResponseEntity actualUpdateUserInfoResult = settingsController.updateUserInfo(user, userEntity);
        assertNull(actualUpdateUserInfoResult.getBody());
        assertEquals("<204 NO_CONTENT No Content,[]>", actualUpdateUserInfoResult.toString());
        assertEquals(HttpStatus.NO_CONTENT, actualUpdateUserInfoResult.getStatusCode());
        verify(userService).updateUserInformationFromSettings(any(), anyString());
    }

    @Test
    public void testGetSettings() throws Exception {
        when(this.userService.findByNickname(anyString())).thenReturn(new UserDTO());
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders.formLogin();
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.settingsController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}

