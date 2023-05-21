package org.ziegelbauer.homepage.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.ziegelbauer.homepage.models.dto.DeleteUserDTO;
import org.ziegelbauer.homepage.models.dto.ModifyUserDTO;
import org.ziegelbauer.homepage.models.dto.ProfileUpdateDTO;
import org.ziegelbauer.homepage.services.UserService;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UsersController {
    private final UserService userService;

    @GetMapping
    public String index(Model model) {
        var users = userService.loadAll();
        model.addAttribute("users", users);
        model.addAttribute("dto", new DeleteUserDTO());
        return "users/index";
    }

    @GetMapping("/modify/{id}")
    public String modify(@PathVariable int id, Model model) {
        ModifyUserDTO dto = userService.loadUserToModify(id);
        model.addAttribute("dto", dto);
        return "users/modify";
    }

    @PostMapping("/modify")
    public String modify(@ModelAttribute("dto") ModifyUserDTO dto) {
        userService.modifyUser(dto);
        return "redirect:/users";
    }

    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication) {
        ProfileUpdateDTO dto = userService.loadProfileToUpdate(authentication.getName());
        model.addAttribute("dto", dto);
        return "users/profile";
    }

    @PostMapping("/profile")
    public String profile(@ModelAttribute("dto") ProfileUpdateDTO dto) {
        userService.updateProfile(dto);
        return "redirect:/";
    }
}
