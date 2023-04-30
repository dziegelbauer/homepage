package org.ziegelbauer.homepage.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.ziegelbauer.homepage.data.UserRepository;
import org.ziegelbauer.homepage.models.dto.DeleteUserDTO;
import org.ziegelbauer.homepage.models.dto.ModifyUserDTO;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UsersController {
    private final UserRepository userRepository;

    @GetMapping
    public String index(Model model) {
        var users = userRepository.findAll();
        model.addAttribute("users", users);
        model.addAttribute("dto", new DeleteUserDTO());
        return "users/index";
    }

    @GetMapping("/modify/{id}")
    public String modify(@PathVariable int id, Model model) {
        var searchResult = userRepository.findById(id);
        if (searchResult.isPresent()) {
            var user = searchResult.get();
            var dto = new ModifyUserDTO(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.isAdmin());
            model.addAttribute("dto", dto);
            return "users/modify";
        }
        return "error";
    }
    @PostMapping("/modify")
    public String modify(@ModelAttribute("dto") ModifyUserDTO dto) {
        var searchResult = userRepository.findById(dto.getId());
        if(searchResult.isPresent()) {
            var user = searchResult.get();
            user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());
            user.setAdmin(dto.isAdmin());
            user.setDisplayName(dto.getFirstName() + " " + dto.getLastName());

            userRepository.save(user);

            return "redirect:/users";
        }

        return "errer";
    }
}
