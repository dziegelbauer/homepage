package org.ziegelbauer.homepage.services;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.ziegelbauer.homepage.data.UserRepository;
import org.ziegelbauer.homepage.models.authentication.User;
import org.ziegelbauer.homepage.models.dto.ModifyUserDTO;
import org.ziegelbauer.homepage.models.dto.RegisterUserDTO;
import org.ziegelbauer.homepage.models.exceptions.PasswordsDoNotMatchException;
import org.ziegelbauer.homepage.models.exceptions.UserAlreadyExistsException;
import org.ziegelbauer.homepage.models.exceptions.UserNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceUnitTest {
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void loadUserToModifyReturnsDTOFromValidId() {
        when(userRepository.findById(5))
                .thenReturn(Optional.of(
                        User.builder()
                                .id(5)
                                .email("mike@test.com")
                                .username("mike@test.com")
                                .firstName("Mike")
                                .lastName("Smith")
                                .displayName("Mike Smith")
                                .hashedPassword("@$%#RTFF^%BV&#$S%")
                                .isAdmin(false)
                                .build()
                ));

        assertThat(userService.loadUserToModify(5))
                .isNotNull()
                .hasFieldOrPropertyWithValue("firstName", "Mike")
                .isInstanceOf(ModifyUserDTO.class);
    }

    @Test
    public void loadUserToModifyThrowsOnInvalidId() {
        when(userRepository.findById(7))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.loadUserToModify(7);
        });

        assertTrue(exception.getMessage().contains("No user matching"));
    }

    @Test
    public void modifyUserReturnsUserFromValidDTO() {
        when(userRepository.findById(5))
                .thenReturn(Optional.of(
                        User.builder()
                                .id(5)
                                .email("mike@test.com")
                                .username("mike@test.com")
                                .firstName("Mike")
                                .lastName("Smith")
                                .displayName("Mike Smith")
                                .hashedPassword("@$%#RTFF^%BV&#$S%")
                                .isAdmin(false)
                                .build()
                ));

        ModifyUserDTO dto = ModifyUserDTO.builder()
                .id(5)
                .firstName("Tom")
                .lastName("Jones")
                .isAdmin(true)
                .build();

        userService.modifyUser(dto);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userCaptor.capture());

        assertThat(userCaptor.getValue())
                .isNotNull()
                .hasFieldOrPropertyWithValue("displayName", "Tom Jones")
                .hasFieldOrPropertyWithValue("email", "mike@test.com")
                .isInstanceOf(User.class);
    }

    @Test
    public void modifyUserThrowsOnInvalidDTO() {
        when(userRepository.findById(7))
                .thenReturn(Optional.empty());

        ModifyUserDTO dto = ModifyUserDTO.builder()
                .id(7)
                .firstName("Tom")
                .lastName("Jones")
                .isAdmin(true)
                .build();

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.modifyUser(dto);
        });

        assertTrue(exception.getMessage().contains("No user matching"));
    }

    @Test
    public void registerUserReturnsUserFromNewUsername() throws UserAlreadyExistsException {
        when(userRepository.findByUsername("mike@test.com"))
                .thenReturn(Optional.empty());

        RegisterUserDTO dto = RegisterUserDTO.builder()
                .email("mike@test.com")
                .firstName("Mike")
                .lastName("Smith")
                .password("password")
                .confirmPassword("password")
                .build();

        userService.registerUser(dto);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userCaptor.capture());

        assertThat(userCaptor.getValue())
                .isNotNull()
                .hasFieldOrPropertyWithValue("displayName", "Mike Smith")
                .hasFieldOrPropertyWithValue("email", "mike@test.com")
                .isInstanceOf(User.class);
    }

    @Test
    public void registerUserThrowsOnDuplicateUsername() {
        when(userRepository.findByUsername("tom@test.com"))
                .thenReturn(Optional.of(
                        User.builder()
                                .username("tom@test.com")
                                .email("tom@test.com")
                                .firstName("Tom")
                                .lastName("Jones")
                                .displayName("Tom Jones")
                                .isAdmin(true)
                                .hashedPassword("#@C$%^$%V^#X")
                                .build()
                ));

        RegisterUserDTO dto = RegisterUserDTO.builder()
                .email("tom@test.com")
                .firstName("Tom")
                .lastName("Smith")
                .password("password")
                .confirmPassword("password")
                .build();

        Exception exception = assertThrows(UserAlreadyExistsException.class, () -> {
            userService.registerUser(dto);
        });

        assertTrue(exception.getMessage().contains("A user with username:"));
    }

    @Test
    public void registerUserThrowsOnMismatchedPasswords() {
        RegisterUserDTO dto = RegisterUserDTO.builder()
                .email("tom@test.com")
                .firstName("Tom")
                .lastName("Smith")
                .password("password")
                .confirmPassword("passweird")
                .build();

        Exception exception = assertThrows(PasswordsDoNotMatchException.class, () -> {
            userService.registerUser(dto);
        });

        assertTrue(exception.getMessage().contains("Provided passwords do not match"));
    }
}
