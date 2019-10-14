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

/**
 * Класс сервис для работы с объектом User
 */

@Service
public class UserService implements UserDetailsService{

    private final UserRepo userRepo;

    private final MailService mailService;

    private final PasswordEncoder passwordEncoder;

    @Value("${server.add}")
    private String serverAddress;

    @Autowired
    public UserService(UserRepo userRepo, MailService mailService, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username)  {
        User user = userRepo.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    /**
     * Добавление нового пользователя,
     * установка роли USER для дальнейшего разделения пользовательских УЗ и привилегированных,
     * генерация кода активации для подтверждения почтового ящика
     * @param user
     * @return
     */
    public User addUser(User user) {
        User userFromDb = userRepo.findByUsername(user.getUsername());
        if (userFromDb != null) {
            return null;
        }
        //Указываем что пользователь активен
        user.setActive(true);
        //Устанавливаем роль УЗ USER, ролей может быть несколько
        user.setRoles(Collections.singleton(Role.USER));
        //Генерируем код активации
        String activationCode = UUID.randomUUID().toString();
        user.setActivationCode(activationCode);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        sendMessage(user);
        return user;
    }

    /**
     * Отправка кода активации на почтовый ящик пользователя
     * {@link  MailService#send(String, String, String)} ()}
     * @param user
     */
    private void sendMessage(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {//Если строка адреса не пустая
            String message = String.format("Hello, %s! \n" +
                            "Verify your account link: " + serverAddress + "activate/%s",
                    user.getUsername(), user.getActivationCode());
            mailService.send(user.getEmail(), "Activation code", message);//Отправляем сообщение
        }
    }

    /**
     * Активация учетной записи пользователя
     * @param code
     * @return
     */
    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);

        if (user == null) return false;//Если пользователь уже активирован, код активации = null

        user.setActivationCode(null);//Удаляем код активации
        userRepo.save(user);

        return true;
    }

    /**
     * Обновление данных пользователя.
     * Если поля ввода не пустые, обновляем данные пользователя.
     * Если менялся адрес почты, перевысылаем код активации для подтверждения
     * @param user
     * @param username
     * @param password
     * @param email
     * @param fullName
     * @param address
     * @param phone
     * @return
     */
    public User updateUser(User user,String username, String password, String email, String fullName, String address, String phone) {
        String currentUserEmail = user.getEmail();

        boolean isEmailChanged = (email != null && !email.equals(currentUserEmail) ||
                currentUserEmail != null && !currentUserEmail.equals(email));

        if (isEmailChanged&&!StringUtils.isEmpty(email)) {
                user.setEmail(email);
                user.setActivationCode(UUID.randomUUID().toString());
                sendMessage(user);
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

        userRepo.save(user);
        return user;
    }
}
