package versatile_development.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import versatile_development.domain.dto.IntermediateUser;
import versatile_development.domain.dto.UserDTO;
import versatile_development.service.UserService;

@ContextConfiguration(classes = {TemplateController.class})
@ExtendWith(SpringExtension.class)
public class TemplateControllerTest {
    @Autowired
    private TemplateController templateController;

    @MockBean(name = "userServiceImpl")
    private UserService userService;

    @Test
    public void testActivateAccount() throws Exception {
        doNothing().when(this.userService).updateUser(any());
        when(this.userService.findByConfirmationToken(anyString())).thenReturn(new UserDTO());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/confirm").param("token", "foo");
        MockMvcBuilders.standaloneSetup(this.templateController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().<Object>size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/login"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"));
    }

    @Test
    public void testActivateAccountIfUserNull() throws Exception {
        doNothing().when(this.userService).updateUser(any());
        when(this.userService.findByConfirmationToken(anyString())).thenReturn(null);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/confirm").param("token", "foo");
        MockMvcBuilders.standaloneSetup(this.templateController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().<Object>size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/login"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        doNothing().when(this.userService).deleteAccountByNickname(anyString());

        IntermediateUser intermediateUser = new IntermediateUser();
        intermediateUser.setNickname("CommoMegaSator");
        intermediateUser.setToken("nonVersatileProtecting");
        String content = (new ObjectMapper()).writeValueAsString(intermediateUser);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/all")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.templateController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void testGetAllUsers() throws Exception {
        when(this.userService.findAllUsers(any()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/all");
        MockMvcBuilders.standaloneSetup(this.templateController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().<Object>size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("users"))
                .andExpect(MockMvcResultMatchers.view().name("all_users"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("all_users"));
    }

    @Test
    public void testGetLoginView() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login");
        MockMvcBuilders.standaloneSetup(this.templateController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().<Object>size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("userEntity"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/profile"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/profile"));
    }

    @Test
    public void testGetLoginView2() throws Exception {
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/login");
        getResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(this.templateController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().<Object>size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("userEntity"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/profile"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/profile"));
    }

    @Test
    public void testGetMainPageView() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/");
        MockMvcBuilders.standaloneSetup(this.templateController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().<Object>size(0))
                .andExpect(MockMvcResultMatchers.view().name("main"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("main"));
    }

    @Test
    public void testGetMainPageView2() throws Exception {
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/");
        getResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(this.templateController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().<Object>size(0))
                .andExpect(MockMvcResultMatchers.view().name("main"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("main"));
    }

    @Test
    public void testGetMessagePage() throws Exception {
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders.formLogin();
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.templateController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(405));
    }

    @Test
    public void testGetProfileView() throws Exception {
        when(this.userService.findByNickname(anyString())).thenReturn(new UserDTO());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/profile").param("age", "Age");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.templateController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void testGetProfileView2() throws Exception {
        when(this.userService.findByNickname(anyString())).thenReturn(null);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/profile").param("age", "Age");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.templateController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void testGetRegistrationView() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/registration");
        MockMvcBuilders.standaloneSetup(this.templateController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().<Object>size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("userEntity"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/profile"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/profile"));
    }

    @Test
    public void testGetRegistrationView2() throws Exception {
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/registration");
        getResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(this.templateController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().<Object>size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("userEntity"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/profile"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/profile"));
    }
}

