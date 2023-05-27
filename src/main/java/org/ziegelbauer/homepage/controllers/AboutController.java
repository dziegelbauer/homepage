package org.ziegelbauer.homepage.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping("/about")
@RequiredArgsConstructor
public class AboutController {
    @GetMapping("/certifications")
    public String certifications() {
        return "about/certifications";
    }

    @GetMapping("/bio")
    public String bio() {
        return "about/bio";
    }

    @GetMapping("/resume")
    public String resume() {
        return "about/resume";
    }
}
