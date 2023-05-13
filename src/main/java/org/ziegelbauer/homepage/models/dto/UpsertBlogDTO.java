package org.ziegelbauer.homepage.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpsertBlogDTO {
    private Integer id;
    private String title;
    private String author;
    private String body;
}
