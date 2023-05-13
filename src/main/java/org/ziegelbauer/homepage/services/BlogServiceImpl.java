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
public class BlogServiceImpl implements BlogService {
    private final BlogRepository blogRepository;

    @Override
    public List<Blog> loadAll() {
        Iterable<Blog> searchResults = blogRepository.findAll();
        List<Blog> blogs = new ArrayList<>();

        for (Blog blog : searchResults) {
            blogs.add(blog);
        }

        return blogs;
    }

    @Override
    public UpsertBlogDTO buildUpsertDTO(Integer id) {
        UpsertBlogDTO dto = new UpsertBlogDTO();

        if (id == null || id <= 0) {
            dto.setId(0);
            dto.setAuthor("David Ziegelbauer");
        } else {
            Optional<Blog> searchResult = blogRepository.findById(id);
            if (searchResult.isPresent()) {
                Blog blog = searchResult.get();
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

    @Override
    public void addOrUpdate(UpsertBlogDTO dto) {
        Blog blog;
        if (dto.getId() == 0) {
            blog = Blog.builder()
                    .id(null)
                    .title(dto.getTitle())
                    .body(dto.getBody())
                    .author(dto.getAuthor())
                    .created(Date.from(Instant.now()))
                    .modified(Date.from(Instant.now()))
                    .build();
        } else {
            Optional<Blog> searchResult = blogRepository.findById(dto.getId());
            if (searchResult.isPresent()) {
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

    @Override
    public void delete(Integer id) {
        blogRepository.deleteById(id);
    }
}
