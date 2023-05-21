package org.ziegelbauer.homepage.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.ziegelbauer.homepage.configuration.WebSecurityConfig;
import org.ziegelbauer.homepage.models.dto.ModifyUserDTO;
import org.ziegelbauer.homepage.services.UserService;
import org.ziegelbauer.homepage.services.UserServiceImpl;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UsersController.class)
@Import({WebSecurityConfig.class, UserServiceImpl.class})
public class UsersControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void indexReturnsMultipleUsers() {

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void modifyReturnsUserInformation() throws Exception {
        when(userService.loadUserToModify(5))
                .thenReturn(ModifyUserDTO.builder()
                        .id(5)
                        .firstName("Mike")
                        .lastName("Smith")
                        .isAdmin(false)
                        .build());

        mockMvc.perform(get("/users/modify/5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(containsString("<!DOCTYPE html>")));
    }
}
