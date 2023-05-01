package org.ziegelbauer.homepage.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyUserDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private boolean isAdmin;
}