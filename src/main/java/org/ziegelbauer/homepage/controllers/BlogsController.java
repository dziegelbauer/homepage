package org.ziegelbauer.homepage.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.ziegelbauer.homepage.models.Blog;
import org.ziegelbauer.homepage.models.dto.DeleteBlogDTO;
import org.ziegelbauer.homepage.models.dto.UpsertBlogDTO;
import org.ziegelbauer.homepage.services.BlogService;

import java.util.List;

@Controller
@RequestMapping("/blogs")
@RequiredArgsConstructor
public class BlogsController {
    private final BlogService blogService;

    @GetMapping
    public String index(Model model) {
        List<Blog> blogs = blogService.loadAll();
        model.addAttribute("blogs", blogs);
        return "blogs/index";
    }

    @GetMapping("/manage")
    public String manage(Model model) {
        List<Blog> blogs = blogService.loadAll();
        model.addAttribute("blogs", blogs);
        model.addAttribute("dto", new DeleteBlogDTO(0));
        return "blogs/manage";
    }

    @GetMapping("/upsert")
    public String upsert(@RequestParam(required = false) Integer id, Model model) {
        UpsertBlogDTO dto = blogService.buildUpsertDTO(id);
        model.addAttribute("dto", dto);
        return "blogs/upsert";
    }

    @PostMapping(value = "/upsert", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String upsert(@ModelAttribute("dto") UpsertBlogDTO dto) {
        blogService.addOrUpdate(dto);
        return "redirect:/blogs/manage";
    }

    @PostMapping("/delete")
    public String delete(@ModelAttribute("dto") DeleteBlogDTO dto) {
        blogService.delete(dto.getId());
        return "redirect:/blogs/manage";
    }
}
