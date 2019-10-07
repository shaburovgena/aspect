package com.example.service;

import com.example.domain.Role;
import com.example.domain.User;
import com.example.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.UUID;

//Класс будет просканирован как компонент Spring и помещен в контекст приложения
@Service
public class UserService implements UserDetailsService {

    //Аналог конструктора с переданным параметром userRepo
    private final UserRepo userRepo;

    private final MailSender mailSender;

    private final PasswordEncoder passwordEncoder;

    @Value("${server.add}")
    private String serverAddress;

    @Autowired
    public UserService(UserRepo userRepo, MailSender mailSender, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
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
        System.out.println(user.getUsername());
        if (userFromDb != null) {
            return null;
        }

        //Указываем что пользователь активен
        user.setActive(true);

        //Устанавливаем роль УЗ USER через Set так как ролей может быть несколько
        user.setRoles(Collections.singleton(Role.USER));



        //Генерируем код активации
        String activationCode = UUID.randomUUID().toString();
        user.setActivationCode(activationCode);

        //Шифруем пароль
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        sendMessage(user);
        //Сохраняем пользователя в базе
        userRepo.save(user);
        //Если поле mail не пустое отправить код активации
        sendMessage(user);
        return user;
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


    public User updateUser(User user,String username, String password, String email, String fullName, String address, String phone) {
        String currentUserEmail = user.getEmail();

        boolean isEmailChanged = (email != null && !email.equals(currentUserEmail) ||
                currentUserEmail != null && !currentUserEmail.equals(email));

        if (isEmailChanged) {
            if (!StringUtils.isEmpty(email)) {
                user.setEmail(email);
                user.setActivationCode(UUID.randomUUID().toString());
                sendMessage(user);
            }
        }
        if (!StringUtils.isEmpty(username)) {
            user.setUsername(username);
        }
        if (!StringUtils.isEmpty(password)) {
            user.setPassword(passwordEncoder.encode(password));
        }
        if (!StringUtils.isEmpty(fullName)) {
            user.setFullName(fullName);
        }
        if (!StringUtils.isEmpty(address)) {
            user.setAddress(address);
        }
        if (!StringUtils.isEmpty(phone)) {
            user.setPhone(phone);
        }

        //Если менялся адрес почты перевысылаем код активации
        userRepo.save(user);

        return user;
    }
}
