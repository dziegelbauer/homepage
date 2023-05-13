package org.ziegelbauer.homepage.services;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.ziegelbauer.homepage.data.BlogRepository;
import org.ziegelbauer.homepage.models.Blog;
import org.ziegelbauer.homepage.models.dto.UpsertBlogDTO;
import org.ziegelbauer.homepage.models.exceptions.BlogNotFoundException;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BlogServiceUnitTest {
    @MockBean
    private BlogRepository blogRepository;

    @Autowired
    private BlogService blogService;

    @Test
    public void loadAllReturnsBlogList() {
        when(blogRepository.findAll())
                .thenReturn(new LinkedList<>(
                        Arrays.asList(
                                new Blog(1, "Blog1", "This is a test", "Dave", Date.from(Instant.now()), Date.from(Instant.now())),
                                new Blog(2, "Blog2", "This is also a test", "Sally", Date.from(Instant.now()), Date.from(Instant.now())),
                                new Blog(3, "Blog3", "This is a third test", "Emily", Date.from(Instant.now()), Date.from(Instant.now())))));

        assertThat(blogService.loadAll())
                .isNotNull()
                .hasSize(3)
                .element(0).hasFieldOrPropertyWithValue("title", "Blog1");
    }

    @Test
    public void buildUpsertDTOReturnsDTOFromValidId() {
        when(blogRepository.findById(5))
                .thenReturn(Optional.of(Blog.builder()
                        .id(5)
                        .author("Mike")
                        .title("Blog4")
                        .body("This is the text of a blog")
                        .created(Date.from(Instant.now()))
                        .modified(Date.from(Instant.now()))
                        .build()));

        assertThat(blogService.buildUpsertDTO(5))
                .isNotNull()
                .hasFieldOrPropertyWithValue("author", "Mike")
                .isInstanceOf(UpsertBlogDTO.class);
    }

    @Test
    public void buildUpsertDTOReturnsDTOFromZeroId() {
        assertThat(blogService.buildUpsertDTO(0))
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 0)
                .hasFieldOrPropertyWithValue("author", "David Ziegelbauer")
                .isInstanceOf(UpsertBlogDTO.class);
    }

    @Test
    public void buildUpsertDTOThrowsOnInvalidId() {
        when(blogRepository.findById(7))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(BlogNotFoundException.class, () -> {
            blogService.buildUpsertDTO(7);
        });

        assertTrue(exception.getMessage().contains("Blog with Id"));
    }

    @Test
    public void addOrUpdateReturnsBlogFromValidId() {
        when(blogRepository.findById(5))
                .thenReturn(Optional.of(Blog.builder()
                        .id(5)
                        .author("Mike")
                        .title("Blog4")
                        .body("This is the text of a blog")
                        .created(Date.from(Instant.now()))
                        .modified(Date.from(Instant.now()))
                        .build()));

        UpsertBlogDTO dto = UpsertBlogDTO.builder()
                .id(5)
                .author("Tom")
                .title("Blog5")
                .body("Blog update!")
                .build();

        blogService.addOrUpdate(dto);

        ArgumentCaptor<Blog> blogCaptor = ArgumentCaptor.forClass(Blog.class);

        verify(blogRepository).save(blogCaptor.capture());

        assertThat(blogCaptor.getValue())
                .isNotNull()
                .hasFieldOrPropertyWithValue("author", "Mike")
                .hasFieldOrPropertyWithValue("title", "Blog5")
                .isInstanceOf(Blog.class);
    }

    @Test
    public void addOrUpdateReturnsBlogFromZeroId() {
        UpsertBlogDTO dto = UpsertBlogDTO.builder()
                .id(0)
                .author("Jim")
                .title("Blog6")
                .body("New Blog!")
                .build();

        blogService.addOrUpdate(dto);

        ArgumentCaptor<Blog> blogCaptor = ArgumentCaptor.forClass(Blog.class);

        verify(blogRepository).save(blogCaptor.capture());

        assertThat(blogCaptor.getValue())
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", null)
                .hasFieldOrPropertyWithValue("author", "Jim")
                .hasFieldOrPropertyWithValue("title", "Blog6")
                .isInstanceOf(Blog.class);
    }

    @Test
    public void addOrUpdateThrowsOnInvalidId() {
        when(blogRepository.findById(7))
                .thenReturn(Optional.empty());

        UpsertBlogDTO dto = UpsertBlogDTO.builder()
                .id(7)
                .author("Jim")
                .title("Blog6")
                .body("New Blog!")
                .build();

        Exception exception = assertThrows(BlogNotFoundException.class, () -> {
            blogService.addOrUpdate(dto);
        });

        assertTrue(exception.getMessage().contains("Blog with Id"));
    }
}
