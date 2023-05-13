package org.ziegelbauer.homepage.services;

import org.ziegelbauer.homepage.models.Blog;
import org.ziegelbauer.homepage.models.dto.UpsertBlogDTO;

import java.util.List;

public interface BlogService {
    List<Blog> loadAll();

    UpsertBlogDTO buildUpsertDTO(Integer id);

    void addOrUpdate(UpsertBlogDTO dto);

    void delete(Integer id);
}
