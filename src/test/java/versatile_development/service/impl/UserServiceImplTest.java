package versatile_development.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import versatile_development.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    @Autowired
    UserService userService;

    @Test
    void entityToDTOMapper() {
        assertThrows(NullPointerException.class, () -> userService.entityToDTOMapper(null));
    }

    @Test
    void DTOToEntityMapper() {
        assertThrows(NullPointerException.class, () -> userService.DTOToEntityMapper(null));
    }
}