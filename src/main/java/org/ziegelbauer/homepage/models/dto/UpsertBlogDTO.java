package org.ziegelbauer.homepage.models.dto;

import lombok.Data;

@Data
public class UpsertBlogDTO {
    private Integer id;
    private String title;
    private String author;
    private String body;
}
