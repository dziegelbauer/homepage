package org.ziegelbauer.homepage.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.ziegelbauer.homepage.data.BlogRepository;
import org.ziegelbauer.homepage.models.Blog;
import org.ziegelbauer.homepage.models.dto.UpsertBlogDTO;
import org.ziegelbauer.homepage.models.exceptions.BlogNotFoundException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlogService {
    private final BlogRepository blogRepository;

    public List<Blog> loadAll() {
        Iterable<Blog> searchResults = blogRepository.findAll();
        List<Blog> blogs = new ArrayList<>();

        for(Blog blog : searchResults) {
            blogs.add(blog);
        }

        return blogs;
    }

    public UpsertBlogDTO buildUpsertDTO(Integer id) {
        UpsertBlogDTO dto = new UpsertBlogDTO();

        if(id == null || id <= 0) {
            dto.setId(0);
            dto.setAuthor("David Ziegelbauer");
        } else {
            Optional<Blog> searchResult = blogRepository.findById(id);
            if(searchResult.isPresent()) {
                var blog = searchResult.get();
                dto.setId(blog.getId());
                dto.setTitle(blog.getTitle());
                dto.setBody(blog.getBody());
                dto.setAuthor(blog.getAuthor());
            } else {
                throw new BlogNotFoundException(id);
            }
        }
        return dto;
    }

    public void addOrUpdate(UpsertBlogDTO dto) {
        Blog blog;
        if(dto.getId() == 0)
        {
            blog = new Blog(
                    null,
                    dto.getTitle(),
                    dto.getBody(),
                    dto.getAuthor(),
                    Date.from(Instant.now()),
                    Date.from(Instant.now()));
        } else {
            var searchResult = blogRepository.findById(dto.getId());
            if(searchResult.isPresent()) {
                blog = searchResult.get();
                blog.setTitle(dto.getTitle());
                blog.setBody(dto.getBody());
                blog.setModified(Date.from(Instant.now()));
            } else {
                throw new BlogNotFoundException(dto.getId());
            }
        }
        blogRepository.save(blog);
    }

    public void delete(Integer id) {
        blogRepository.deleteById(id);
    }
}
