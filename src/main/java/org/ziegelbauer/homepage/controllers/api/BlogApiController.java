package org.ziegelbauer.homepage.controllers.api;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ziegelbauer.homepage.data.BlogRepository;
import org.ziegelbauer.homepage.models.Blog;
import org.ziegelbauer.homepage.models.dto.UpsertBlogDTO;
import org.ziegelbauer.homepage.utility.BlogModelAssembler;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/blog")
@RequiredArgsConstructor
public class BlogApiController {
    private final BlogRepository blogRepository;
    private final BlogModelAssembler assembler;

    @GetMapping()
    public ResponseEntity<?> get() {
        var searchResults = blogRepository.findAll();
        var blogs = new ArrayList<Blog>();

        for(var blog : searchResults) {
            blogs.add(blog);
        }
        var entityModel = blogs
             .stream()
             .map(assembler::toModel)
             .toList();
        return ResponseEntity.ok(CollectionModel.of(entityModel, linkTo(methodOn(BlogApiController.class).get()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable int id) {
        var searchResult = blogRepository.findById(id);
        if(searchResult.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        EntityModel<Blog> entityModel = assembler.toModel(searchResult.get());

        return ResponseEntity.ok(entityModel);
    }

    @PostMapping()
    public ResponseEntity<?> post(@RequestBody UpsertBlogDTO dto) {
        var blog = new Blog();
        blog.setTitle(dto.getTitle());
        blog.setBody(dto.getBody());
        blog.setAuthor(dto.getAuthor());
        blog.setCreated(Date.from(Instant.now()));
        blog.setModified(Date.from(Instant.now()));

        EntityModel<Blog> entityModel = assembler.toModel(blogRepository.save(blog));

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@RequestBody UpsertBlogDTO dto, @PathVariable int id) {
        Blog updatedBlog = blogRepository.findById(id)
                .map(blog -> {
                    blog.setTitle(dto.getTitle());
                    blog.setBody(dto.getBody());
                    blog.setModified(Date.from(Instant.now()));
                    return blogRepository.save(blog);
                })
                .orElseGet(() -> {
                    Blog newBlog = new Blog();
                    newBlog.setId(id);
                    newBlog.setTitle(dto.getTitle());
                    newBlog.setBody(dto.getBody());
                    newBlog.setAuthor(dto.getAuthor());
                    newBlog.setCreated(Date.from(Instant.now()));
                    newBlog.setModified(Date.from(Instant.now()));
                    return blogRepository.save(newBlog);
                });

        EntityModel<Blog> entityModel = assembler.toModel(blogRepository.save(updatedBlog));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        blogRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
