package org.ziegelbauer.homepage.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.ziegelbauer.homepage.models.authentication.User;
import org.ziegelbauer.homepage.models.dto.ModifyUserDTO;
import org.ziegelbauer.homepage.models.dto.ProfileUpdateDTO;
import org.ziegelbauer.homepage.models.dto.RegisterUserDTO;
import org.ziegelbauer.homepage.models.exceptions.UserAlreadyExistsException;

import java.util.List;

public interface UserService extends UserDetailsService {
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    List<User> loadAll();

    ModifyUserDTO loadUserToModify(int id);

    void modifyUser(ModifyUserDTO dto);

    ProfileUpdateDTO loadProfileToUpdate(String username);

    void updateProfile(ProfileUpdateDTO dto);

    void registerUser(RegisterUserDTO dto) throws UserAlreadyExistsException;
}
