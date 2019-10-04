package com.example.restController;

import com.example.domain.User;
import com.example.domain.Views;
import com.example.domain.WcSender;
import com.example.dto.EventType;
import com.example.dto.ObjectType;
import com.example.repos.UserRepo;
import org.jsoup.nodes.Element;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;

@RestController
@RequestMapping("userList")
public class UserListController {


    private final BiConsumer<EventType, User> wcSender;
    private UserRepo userRepo;

    @Autowired
    public UserListController(WcSender wcSender, UserRepo userRepo) {
        this.wcSender = wcSender.getSender(ObjectType.USER, Views.IdName.class);
        this.userRepo = userRepo;
    }


    @GetMapping
    public List<User> list() {
        return userRepo.findAll();
    }

    @GetMapping("{id}")
    public User getOne(@PathVariable("id") User user) {
        return user;
    }


    @PostMapping
    public User create(@RequestBody User user)  {
        user.setLastVisit(LocalDateTime.now());
        User updatedUser = userRepo.save(user);
        wcSender.accept(EventType.CREATE, updatedUser);
        System.out.println(user.getId());
        return updatedUser;
    }

    @PutMapping("{id}")
    public User update(
            @PathVariable("id") User userFromDb,
            @RequestBody User user
    ) {
        //Скопирует все данные из user в userFromDb, кроме id
        BeanUtils.copyProperties(user, userFromDb, "id");
        User updatedUser = userRepo.save(userFromDb);
        wcSender.accept(EventType.UPDATE, updatedUser);
        return updatedUser;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") User user) {
        userRepo.delete(user);
        wcSender.accept(EventType.REMOVE, user);
    }


@MessageMapping("/changeUser")
@SendTo("topic/acivity")
public User user (User user){
        return userRepo.save(user);
}
    private String getContent(Element element) {
        return element == null ? "" : element.attr("content");
    }
}
