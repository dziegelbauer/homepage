package org.ziegelbauer.homepage.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDTO {
    private String email;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
}
