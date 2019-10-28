package com.example.controller;

import com.example.domain.User;
import com.example.domain.WsSender;
import com.example.repos.UserRepo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Класс контроллер, реализующий REST API сервера, для взамодействия с доменными объектами
 */

@RestController
@RequestMapping("userList")
public class UserListController extends AbstractRestController<User, UserRepo> {

    public UserListController(UserRepo repo, WsSender wsSender) {
        super(repo, wsSender);
    }

}
