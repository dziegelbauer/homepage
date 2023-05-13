package org.ziegelbauer.homepage.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.ziegelbauer.homepage.models.dto.LoginRequestDTO;
import org.ziegelbauer.homepage.models.dto.RegisterUserDTO;
import org.ziegelbauer.homepage.models.exceptions.PasswordsDoNotMatchException;
import org.ziegelbauer.homepage.models.exceptions.UserAlreadyExistsException;
import org.ziegelbauer.homepage.services.UserService;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("dto", new RegisterUserDTO());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("dto") RegisterUserDTO dto, Model model, BindingResult result)
    {
        try {
            userService.registerUser(dto);
        } catch (PasswordsDoNotMatchException e) {
            ObjectError passwordError = new ObjectError("password", e.getMessage());
            result.addError(passwordError);
            model.addAttribute("dto", dto);
            return "auth/register";
        } catch (UserAlreadyExistsException e) {
            ObjectError usernameError = new ObjectError("email", e.getMessage());
            result.addError(usernameError);
            model.addAttribute("dto", dto);
            return "auth/register";
        }

        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("dto", new LoginRequestDTO());
        return "auth/login";
    }

    @GetMapping("/logout")
    public String logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/auth/login";
    }
}
