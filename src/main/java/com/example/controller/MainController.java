package com.example.controller;

import com.example.domain.User;
import com.example.domain.Views;
import com.example.repos.UserRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@Controller
@RequestMapping("/")
public class MainController {

    @Value("${spring.profiles.active}")
    private String profile;
    private final ObjectWriter usersWriter;
    private final ObjectWriter profileWriter;
    private final UserRepo userRepo;

    @Autowired
    public MainController(UserRepo userRepo, ObjectMapper mapper) {
        this.userRepo = userRepo;

        ObjectMapper objectMapper = mapper
                .setConfig(mapper.getSerializationConfig());

        this.profileWriter = objectMapper
                .writerWithView(Views.IdName.class);
 this.usersWriter = objectMapper
                .writerWithView(Views.IdName.class);

    }

    @GetMapping
    public String main(
            Model model,
            @AuthenticationPrincipal User user
    ) throws JsonProcessingException {
        HashMap<Object, Object> data = new HashMap<>();
        if (user != null && user.getActivationCode() == null) {
            model.addAttribute("profile", profileWriter.writeValueAsString(user));
            model.addAttribute("users", usersWriter.writeValueAsString(userRepo.findAll()));
            data.put("message", "Hello");
            model.addAttribute("frontendData", data);
        } else {
            model.addAttribute("profile", "null");
            model.addAttribute("users", "[]");
            data.put("message", "Login please");
            data.put("isLoginForm", "true");
            model.addAttribute("frontendData", data);
        }

        model.addAttribute("isDevMode", "dev".equals(profile));
        return "index";
    }


}