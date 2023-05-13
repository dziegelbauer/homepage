package org.ziegelbauer.homepage.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifyUserDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private boolean isAdmin;
}
