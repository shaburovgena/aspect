package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.example.domain.Role;
import com.example.domain.User;
import com.example.repos.UserRepo;

import java.util.*;
import java.util.stream.Collectors;

//Класс будет просканирован как компонент Spring и помещен в контекст приложения
@Service
public class UserService implements UserDetailsService {

    //Аналог конструктора с переданным параметром userRepo
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${server.add}")
    private String serverAddress;

    public UserService() {
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public User addUser(User user) {
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) return user;

        //Указываем что пользователь активен
        user.setActive(true);

        //Устанавливаем роль УЗ USER через Set так как ролей может быть несколько
        user.setRoles(Collections.singleton(Role.USER));

        //Уязвимость
        if (user.getUsername().equalsIgnoreCase("admin")) {
            user.setRoles(Collections.singleton(Role.ADMIN));
        }

        //Генерируем код активации
        String activationCode = UUID.randomUUID().toString();
        user.setActivationCode(activationCode);
        System.out.println(activationCode);

        //Шифруем пароль
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        sendMessage(user);
        //Сохраняем пользователя в базе
        userRepo.save(user);
        //Если поле mail не пустое отправить код активации
//        sendMessage(user);
        return null;
    }

    private void sendMessage(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {//Если строка адреса не пустая
            String message = String.format("Hello, %s! \n" +
                            "Verify your account link: " + serverAddress + "activate/%s",
                    user.getUsername(), user.getActivationCode());
//            mailSender.send(user.getEmail(), "Activation code", message);//Отправляем сообщение
            System.out.println(message);
        }
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);

        if (user == null) return false;//Если пользователь уже активирован, код активации = null

        user.setActivationCode(null);//Удаляем код активации
        userRepo.save(user);

        return true;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public void saveUser(String username, User user, Map<String, String> form) {
        //Изменяем имя пользователя
        user.setUsername(username);

        //Получаем все роли из Enum в коллекцию, преобразуем коллекцию в Set
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        //Удаляем существующие роли пользователя
        user.getRoles().clear();

        //Устанавливаем роли отмеченные чекбоксами полученные в form
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        //Сохраняем пользователя в базе с помощью черной магии
        //Spring не создает нового пользователя, а апдейтит существующего
        userRepo.save(user);

    }

    public void updateUser(User user, String password, String email) {
        String currentUserEmail = user.getEmail();

        boolean isEmailChanged = (email != null && !email.equals(currentUserEmail) ||
                currentUserEmail != null && !currentUserEmail.equals(email));

        if (isEmailChanged) {
            user.setEmail(email);
            if (!StringUtils.isEmpty(email)) {
                user.setActivationCode(UUID.randomUUID().toString());
                System.out.println();
            }
        }

        if (!StringUtils.isEmpty(password)) {
            user.setPassword(password);
        }
        //Если менялся адрес почты перевысылаем код активации
        userRepo.save(user);
        if (isEmailChanged) {
            sendMessage(user);
        }
    }
}
