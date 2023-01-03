package com.inventorsoft.english.users.controller;


import com.inventorsoft.english.users.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {

    @GetMapping
    public String displayRegistration(User user, Model model) {
        model.addAttribute("user", user);
        return "registration";
    }
}
