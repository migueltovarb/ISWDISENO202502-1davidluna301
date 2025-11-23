package com.papeleria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/vendedor")
    public String vendedor() {
        return "vendedor";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }
}