package org.ziegelbauer.homepage.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.ziegelbauer.homepage.data.BlogRepository;
import org.ziegelbauer.homepage.models.Blog;
import org.ziegelbauer.homepage.models.dto.DeleteBlogDTO;
import org.ziegelbauer.homepage.models.dto.UpsertBlogDTO;

import java.time.Instant;
import java.util.Date;

@Controller
@RequestMapping("/blogs")
@RequiredArgsConstructor
public class BlogsController {
    private final BlogRepository blogRepository;

    @GetMapping
    public String index(Model model) {
        var blogs = blogRepository.findAll();
        model.addAttribute("blogs", blogs);
        return "blogs/index";
    }

    @GetMapping("/manage")
    public String manage(Model model) {
        var blogs = blogRepository.findAll();
        model.addAttribute("blogs", blogs);
        model.addAttribute("dto", new DeleteBlogDTO(0));
        return "blogs/manage";
    }

    @GetMapping("/upsert")
    public String upsert(@RequestParam(required = false) Integer id, Model model) {
        var dto = new UpsertBlogDTO();

        if(id == null || id <= 0) {
            dto.setId(0);
            dto.setAuthor("David Ziegelbauer");
        } else {
            var blog = blogRepository.findById(id);
            if(blog.isPresent()) {
                dto.setId(blog.get().getId());
                dto.setTitle(blog.get().getTitle());
                dto.setBody(blog.get().getBody());
                dto.setAuthor(blog.get().getAuthor());
            } else {
                return "error";
            }
        }

        model.addAttribute("dto", dto);

        return "blogs/upsert";
    }

    @PostMapping(value = "/upsert", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String upsert(@ModelAttribute("dto") UpsertBlogDTO dto) {
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
                return "error";
            }
        }
        blogRepository.save(blog);

        return "redirect:/blogs/manage";
    }

    @PostMapping("/delete")
    public String delete(@ModelAttribute("dto") DeleteBlogDTO dto) {
        blogRepository.deleteById(dto.getId());
        return "redirect:/blogs/manage";
    }
}
