package org.ziegelbauer.homepage.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.ziegelbauer.homepage.data.UserRepository;
import org.ziegelbauer.homepage.models.authentication.ExternalOAuth2User;
import org.ziegelbauer.homepage.models.authentication.OAuthProvider;
import org.ziegelbauer.homepage.models.authentication.User;
import org.ziegelbauer.homepage.models.dto.ModifyUserDTO;
import org.ziegelbauer.homepage.models.dto.RegisterUserDTO;
import org.ziegelbauer.homepage.models.exceptions.PasswordsDoNotMatchException;
import org.ziegelbauer.homepage.models.exceptions.UserAlreadyExistsException;
import org.ziegelbauer.homepage.models.exceptions.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> searchResult = userRepository.findByUsername(username);
        if (searchResult.isEmpty()) {
            throw new UsernameNotFoundException(username + " not found");
        }
        return searchResult.get();
    }

    @Override
    public List<User> loadAll() {
        List<User> users = new ArrayList<>();

        for (User user : userRepository.findAll()) {
            users.add(user);
        }

        return users;
    }

    @Override
    public ModifyUserDTO loadUserToModify(int id) {
        Optional<User> searchResult = userRepository.findById(id);
        if (searchResult.isEmpty()) {
            throw new UserNotFoundException(Integer.toString(id));
        }
        User user = searchResult.get();
        return ModifyUserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .isAdmin(user.isAdmin())
                .build();
    }

    @Override
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

        userRepository.save(user);
    }

    @Override
    public void registerUser(RegisterUserDTO dto) throws UserAlreadyExistsException {
        if(!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new PasswordsDoNotMatchException();
        }

        Optional<User> searchResult = userRepository.findByUsername(dto.getEmail());

        if(searchResult.isPresent()) {
            throw new UserAlreadyExistsException(dto.getEmail());
        }

        User user = User.builder()
                .id(null)
                .email(dto.getEmail())
                .username(dto.getEmail())
                .hashedPassword(encoder.encode(dto.getPassword()))
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .displayName(String.format("%s %s", dto.getFirstName(), dto.getLastName()))
                .isAdmin(false)
                .build();

        userRepository.save(user);
    }

    @Override
    public User processOAuth2PostLogin(ExternalOAuth2User oAuth2User) {
        Optional<User> searchResult = userRepository.findByUsername(oAuth2User.getEmail());

        if (searchResult.isEmpty()) {
            User newUser = User.builder()
                    .firstName(oAuth2User.getAttribute("given_name"))
                    .lastName(oAuth2User.getAttribute("family_name"))
                    .displayName(oAuth2User.getName())
                    .email(oAuth2User.getEmail())
                    .provider(OAuthProvider.GOOGLE)
                    .build();

            return userRepository.save(newUser);
        }

        return searchResult.get();
    }
}
