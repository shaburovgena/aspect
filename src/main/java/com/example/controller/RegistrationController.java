package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.domain.User;
import com.example.service.UserService;

import javax.validation.Valid;
import java.util.HashMap;

@Controller
public class RegistrationController {
    @Value("${spring.profiles.active}")
    private String profile;
    @Autowired
    private UserService userService;

    @Autowired
    public RegistrationController(
            UserService userService) {
        this.userService = userService;

    }
    @PostMapping("/registration")
    public String register(@RequestParam("passwordConfirm") String passwordConfirm,
                           @Valid User user,
                           Model model) {
        HashMap<Object, Object> data = new HashMap<>();
        if (user.getPassword() == null || passwordConfirm == null ||
                !user.getPassword().equals(passwordConfirm)) {
            data.put("message", "Check your credentials");
            data.put("isRegisterForm", "true");
        }else if (userService.addUser(user) != null) {
            data.put("isRegisterForm", "true");
            data.put("message", "User already exist");
        }else{
            data.put("isLoginForm", "true");
            data.put("message", "Welcome");
        }
        model.addAttribute("profile", "null");
        model.addAttribute("users", "[]");
        model.addAttribute("frontendData", data);
        model.addAttribute("isDevMode", "dev".equals(profile));
        return "index";
    }

    @GetMapping("/registration")
    public String register(Model model) {
        model.addAttribute("profile", "null");
        model.addAttribute("users", "[]");
        model.addAttribute("isDevMode", "dev".equals(profile));
        System.out.println("Redirect");
        return "index";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);//Активация пользователя по коду из письма

        if (isActivated) {
            System.out.println("Активация аккаунта прошла успешно");
            return "redirect:/";
        } else {
            System.out.println("Активация аккаунта не выполнена");
            return "redirect:/login?error";
        }
    }
}
