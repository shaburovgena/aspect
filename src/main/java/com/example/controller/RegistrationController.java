package com.example.controller;

import com.example.domain.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.HashMap;

/**
 * Класс-контроллер, реализующий функционал регистрации и активации аккаунта на сервере
 */
@Controller
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(
            UserService userService) {
        this.userService = userService;


    }
    @PostMapping("/registration")
    public String registration(
                           @Valid User user,
                           @RequestParam ("passwordConfirm") String passwordConfirm,
                           Model model) {
        HashMap<Object, Object> data = new HashMap<>();
        if (StringUtils.isEmpty(user.getPassword())||StringUtils.isEmpty(passwordConfirm)) {
            data.put("message", "Check your credentials");
            data.put("isRegisterForm", "true");
        }else if (userService.addUser(user) == null) {
            data.put("isRegisterForm", "true");
            data.put("message", "User already exist");
        }else{
            data.put("isLoginForm", "true");
            data.put("message", "Welcome, " + user.getUsername()+ ". Sign in please.");
        }
        model.addAttribute("profile", "null");
        model.addAttribute("users", "[]");
        model.addAttribute("frontendData", data);
        return "index";
    }

    @GetMapping("/activate/{code}")
    public String activate(@PathVariable String code) {
        boolean isActivated = userService.activateUser(code);//Активация пользователя по коду из письма
        if (isActivated) {
            return "redirect:/";
        } else {
            return "redirect:/login?error";
        }
    }
}
