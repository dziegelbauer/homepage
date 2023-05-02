package org.ziegelbauer.homepage.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.ziegelbauer.homepage.data.AWSS3Repository;
import org.ziegelbauer.homepage.data.CatPictureRepository;
import org.ziegelbauer.homepage.models.CatPicture;
import org.ziegelbauer.homepage.models.dto.CatPictureDTO;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Controller
@RequestMapping("/cats")
@RequiredArgsConstructor
@Slf4j
public class CatsController {
    private final CatPictureRepository catPictureRepository;
    private final AWSS3Repository catPictureFileRepository;

    @GetMapping
    public String index(Model model) {
        var catPics = catPictureRepository.findAll();
        List<CatPictureDTO> cats = new ArrayList<>();
        for (var catPic : catPics) {
            cats.add(new CatPictureDTO(catPic.getId(), "https://david-ziegelbauer-cat-images.s3.amazonaws.com/" + catPic.getFileName()));
        }
        model.addAttribute("cats", cats);
        return "cats/index";
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
        var newFileName = UUID.randomUUID().toString();
        if(file.getOriginalFilename().contains(".")) {
            var fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            newFileName = newFileName + "." + fileExtension;
        }
        catPic.setFileName(newFileName);
        catPic.setUploaded(Date.from(Instant.now()));
        try {
            catPictureFileRepository.uploadFile(newFileName, file.getInputStream(), "david-ziegelbauer-cat-images");
            catPictureRepository.save(catPic);
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        return "redirect:/cats";
    }
}
