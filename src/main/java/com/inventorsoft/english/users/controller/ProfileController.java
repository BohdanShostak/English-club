package com.inventorsoft.english.users.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    @GetMapping("/{id}")
    public String displayProfile(@PathVariable("id") Long id, Model model) {
        model.addAttribute("current_id", id);
        return "profile/profile";
    }

    @GetMapping("/{id}/edit")
    public String editProfile(@PathVariable("id") Long id, Model model) {
        model.addAttribute("current_id", id);
        return "profile/edit_profile";
    }
}
