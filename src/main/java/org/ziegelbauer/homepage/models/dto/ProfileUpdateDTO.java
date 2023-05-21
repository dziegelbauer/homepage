package org.ziegelbauer.homepage.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String password;
    private String confirmPassword;
    private boolean resetPassword;
}
