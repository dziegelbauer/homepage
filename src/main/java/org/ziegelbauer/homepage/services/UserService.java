package org.ziegelbauer.homepage.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.ziegelbauer.homepage.data.UserRepository;
import org.ziegelbauer.homepage.models.User;
import org.ziegelbauer.homepage.models.dto.ModifyUserDTO;
import org.ziegelbauer.homepage.models.exceptions.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> searchResult = userRepository.findByUsername(username);
        if (searchResult.isEmpty()) {
            throw new UsernameNotFoundException(username + " not found");
        }
        return searchResult.get();
    }

    public List<User> loadAll() {
        List<User> users = new ArrayList<>();

        for (User user : userRepository.findAll()) {
            users.add(user);
        }

        return users;
    }

    public ModifyUserDTO loadUserToModify(int id) {
        Optional<User> searchResult = userRepository.findById(id);
        if (searchResult.isEmpty()) {
            throw new UserNotFoundException(Integer.toString(id));
        }
        User user = searchResult.get();
        return new ModifyUserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.isAdmin());
    }

    public void modifyUser(ModifyUserDTO dto) {
        Optional<User> searchResult = userRepository.findById(dto.getId());
        if (searchResult.isEmpty()) {
            throw new UserNotFoundException(Integer.toString(dto.getId()));
        }
        User user = searchResult.get();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setAdmin(dto.isAdmin());
        user.setDisplayName(dto.getFirstName() + " " + dto.getLastName());
    }
}
