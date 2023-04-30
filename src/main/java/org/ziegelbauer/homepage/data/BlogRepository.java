package org.ziegelbauer.homepage.data;

import org.springframework.data.repository.CrudRepository;
import org.ziegelbauer.homepage.models.Blog;

public interface BlogRepository extends CrudRepository<Blog, Integer> {
}
