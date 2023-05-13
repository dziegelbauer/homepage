package org.ziegelbauer.homepage.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.ziegelbauer.homepage.configuration.WebSecurityConfig;
import org.ziegelbauer.homepage.models.Blog;
import org.ziegelbauer.homepage.services.BlogService;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BlogsController.class)
@Import(WebSecurityConfig.class)
public class BlogsControllerUnitTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlogService blogService;

    @Test
    void indexReturnsRootPage() throws Exception {
        when(blogService.loadAll()).thenReturn(List.of(
                new Blog(1, "Blog1", "Blog1 Text", "Blog1 Author", Date.from(Instant.now()), Date.from(Instant.now()))
        ));

        mockMvc.perform(get("/blogs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(containsString("<!DOCTYPE html>")));
    }

    @Test
    void manageRequiresAuthentication() throws Exception {
        when(blogService.loadAll()).thenReturn(List.of(
                new Blog(1, "Blog1", "Blog1 Text", "Blog1 Author", Date.from(Instant.now()), Date.from(Instant.now()))
        ));

        mockMvc.perform(get("/blogs/manage"))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser
    void manageRequiresAdminRole() throws Exception {
        when(blogService.loadAll()).thenReturn(List.of(
                new Blog(1, "Blog1", "Blog1 Text", "Blog1 Author", Date.from(Instant.now()), Date.from(Instant.now()))
        ));

        mockMvc.perform(get("/blogs/manage"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void manageAllowsAdmin() throws Exception {
        when(blogService.loadAll()).thenReturn(List.of(
                new Blog(1, "Blog1", "Blog1 Text", "Blog1 Author", Date.from(Instant.now()), Date.from(Instant.now()))
        ));

        mockMvc.perform(get("/blogs/manage"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(containsString("<!DOCTYPE html>")));
    }
}
