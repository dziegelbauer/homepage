package org.ziegelbauer.homepage.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CatPictureDTO {
    private int id;
    private String path;
}
