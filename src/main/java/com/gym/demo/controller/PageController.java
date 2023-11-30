package com.gym.demo.controller;

import com.gym.demo.model.Client;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("client", new Client());
        return "registration";
    }

    @GetMapping("/success")
    public String success() {
        return "success";
    }

    @GetMapping("/success-edit")
    public String successEdit() {
        return "success-edit";
    }

}
