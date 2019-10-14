package com.example.controller;

import com.example.domain.User;
import com.example.domain.Views;
import com.example.domain.WsSender;
import com.example.dto.EventType;
import com.example.dto.ObjectType;
import com.example.repos.UserRepo;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.function.BiConsumer;

/**
 * Класс контроллер, реализующий REST API сервера, для взамодействия с доменными объектами
 */

@RestController
@RequestMapping("userList")
public class UserListController {


    private final BiConsumer<EventType, User> wsSender;
    private final UserRepo userRepo;
    private final UserService userService;

    @Autowired
    public UserListController(WsSender wsSender, UserRepo userRepo, UserService userService) {
        this.wsSender = wsSender.getSender(ObjectType.USER, Views.FullProfile.class);
        this.userRepo = userRepo;
        this.userService = userService;
    }




    @PostMapping
    public User create(@RequestBody User user) {
        User updatedUser = userService.addUser(user);
        wsSender.accept(EventType.CREATE, updatedUser);
        return updatedUser;
    }

    @PutMapping("{id}")
    public User update(
            @PathVariable("id") User userFromDb,
            @RequestBody User user
    ) {

        User updatedUser = userService.updateUser(userFromDb,user.getUsername(), user.getPassword(), user.getEmail(),
                user.getFullName(),user.getAddress(),user.getPhone());
        wsSender.accept(EventType.UPDATE, updatedUser);
        return updatedUser;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") User user) {
        userRepo.delete(user);
        wsSender.accept(EventType.REMOVE, user);
    }


}
