package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

//Класс будет просканирован как компонент Spring и помещен в контекст приложения
//Аннотация для того чтобы spring создал этот класс автоматически без вызова из кода
@Service
public class MailSender {

    private final JavaMailSender mailSender;

    @Autowired
    public MailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    //Метод будет асинхронно запущен в отдельном потоке
    @Async
    public void send(String emailTo, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom("shaburovgena@yandex.ru");
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);


        mailSender.send(mailMessage);
    }
}
