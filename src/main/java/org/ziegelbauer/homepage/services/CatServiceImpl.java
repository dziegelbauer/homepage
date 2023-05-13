package org.ziegelbauer.homepage.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.ziegelbauer.homepage.data.AWSS3Repository;
import org.ziegelbauer.homepage.data.CatPictureRepository;
import org.ziegelbauer.homepage.models.CatPicture;
import org.ziegelbauer.homepage.models.dto.CatPictureDTO;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CatServiceImpl implements CatService {
    private final CatPictureRepository catPictureRepository;
    private final AWSS3Repository catPictureFileRepository;

    @Override
    public List<CatPictureDTO> loadAll() {
        Iterable<CatPicture> catPics = catPictureRepository.findAll();
        List<CatPictureDTO> cats = new ArrayList<>();
        for (var catPic : catPics) {
            cats.add(new CatPictureDTO(catPic.getId(), "https://david-ziegelbauer-cat-images.s3.amazonaws.com/" + catPic.getFileName()));
        }
        return cats;
    }

    @Override
    public void addNew(MultipartFile file) throws IOException {
        if(file.isEmpty()) {
            throw new InvalidObjectException("File upload failed");
        }

        String newFileName = UUID.randomUUID().toString();
        if(Objects.requireNonNull(file.getOriginalFilename()).contains(".")) {
            var fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            newFileName = newFileName + "." + fileExtension;
        }

        CatPicture catPic = new CatPicture(
                null,
                "",
                Date.from(Instant.now()),
                file.getContentType(),
                newFileName);

        try {
            catPictureFileRepository.uploadFile(newFileName, file.getInputStream(), "david-ziegelbauer-cat-images");
            catPictureRepository.save(catPic);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
