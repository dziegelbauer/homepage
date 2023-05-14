package org.ziegelbauer.homepage.services;

import org.springframework.web.multipart.MultipartFile;
import org.ziegelbauer.homepage.models.dto.CatPictureDTO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface CatService {
    List<CatPictureDTO> loadAll();

    void addNew(MultipartFile file) throws IOException;
    void delete(int id) throws FileNotFoundException;
}
