package org.ziegelbauer.homepage.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.ziegelbauer.homepage.data.UserRepository;
import org.ziegelbauer.homepage.models.User;
import org.ziegelbauer.homepage.models.dto.LoginRequestDTO;
import org.ziegelbauer.homepage.models.dto.RegisterUserDTO;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("dto", new RegisterUserDTO());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("dto") RegisterUserDTO dto) {
        if(!dto.getPassword().equals(dto.getConfirmPassword())) {
            return "auth/register";
        }

        var user = new User();
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getEmail());
        user.setHashedPassword(encoder.encode(dto.getPassword()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setDisplayName(dto.getFirstName() + " " + dto.getLastName());
        user.setAdmin(false);
        user.setId(null);

        userRepository.save(user);

        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("dto", new LoginRequestDTO());
        return "auth/login";
    }

//    @PostMapping("/login")
//    public String login(@ModelAttribute("dto") LoginRequestDTO dto) {
//        return "index";
//    }

    @GetMapping("/logout")
    public String logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/auth/login";
    }
}
