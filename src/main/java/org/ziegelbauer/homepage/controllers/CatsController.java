package org.ziegelbauer.homepage.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.ziegelbauer.homepage.models.dto.CatPictureDTO;
import org.ziegelbauer.homepage.services.CatService;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/cats")
@RequiredArgsConstructor
@Slf4j
public class CatsController {
    private final CatService catService;

    @GetMapping
    public String index(Model model) {
        List<CatPictureDTO> cats = catService.loadAll();
        model.addAttribute("cats", cats);
        return "cats/index";
    }

    @GetMapping("/upload")
    public String upload() {
        return "cats/upload";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file")MultipartFile file) throws IOException {
        catService.addNew(file);
        return "redirect:/cats";
    }
}
