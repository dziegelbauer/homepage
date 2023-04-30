package org.ziegelbauer.homepage.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.ziegelbauer.homepage.data.CatPictureRepository;
import org.ziegelbauer.homepage.models.CatPicture;
import org.ziegelbauer.homepage.models.dto.CatPictureDTO;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/cats")
@RequiredArgsConstructor
public class CatsController {
    private final CatPictureRepository catPictureRepository;

    @GetMapping
    public String index(Model model) {
        var ids = catPictureRepository.findAllId();
        List<CatPictureDTO> cats = ids.stream().map(CatPictureDTO::new).toList();
        model.addAttribute("cats", cats);
        return "cats/index";
    }

    @GetMapping(path = "/picture/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void picture(@PathVariable int id, HttpServletResponse response) throws IOException {
        var searchResult = catPictureRepository.findById(id);
        if(searchResult.isPresent()) {
            var catPic = searchResult.get();
            response.setContentType(catPic.getFileType());
            response.setHeader("Content-Disposition", "attachment; filename=" + catPic.getId());
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.getOutputStream().write(catPic.getData());
        } else {
            response.setStatus(404);
        }
    }

    @GetMapping("/upload")
    public String upload() {
        return "cats/upload";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file")MultipartFile file) throws IOException {
        CatPicture catPic = new CatPicture();
        catPic.setId(null);
        catPic.setTitle("");
        catPic.setFileType(file.getContentType());
        catPic.setData(file.getBytes());
        catPic.setUploaded(Date.from(Instant.now()));

        catPictureRepository.save(catPic);

        return "redirect:/cats";
    }
}
