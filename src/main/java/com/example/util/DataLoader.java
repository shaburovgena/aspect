package com.example.util;

import com.example.domain.User;
import com.example.repos.UserRepo;
import com.example.service.UserService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
/**
 * Класс загрузки исходных данных после старта приложения
 * используется API сервиса randomuser.me
 */
@Component
public class DataLoader implements ApplicationRunner {
    private static String url = "https://randomuser.me/api/?results=20";
    private final UserService userService;
    private final UserRepo userRepo;
    private final RestTemplate restTemplate;

    @Autowired
    public DataLoader(UserService userService, UserRepo userRepo, RestTemplate restTemplate, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepo = userRepo;
        this.restTemplate = restTemplate;
    }

    public void run(ApplicationArguments args) {

        if (userRepo.findAll().size() < 10) {
            String response = restTemplate.getForObject(url, String.class);
            JSONObject responseJson = new JSONObject(response);
            JSONArray array = responseJson.getJSONArray("results");
            for (int i = 0; i < array.length(); i++) {
                User user = new User();
                JSONObject userJson = array.getJSONObject(i);
                JSONObject loginJson = userJson.getJSONObject("login");
                JSONObject fullnameJson = userJson.getJSONObject("name");
                JSONObject addressJson = userJson.getJSONObject("location");
                JSONObject pictureJson = userJson.getJSONObject("picture");


                user.setUsername(loginJson.getString("username"));
                user.setEmail(userJson.getString("email"));
                user.setPhone(userJson.getString("phone"));
                user.setUserpic(pictureJson.getString("medium"));
                user.setFullName(fullnameJson.getString("first") + " " + fullnameJson.getString("last"));
                user.setAddress(addressJson.getString("state")
                        + ", " + addressJson.getString("city"));
                user.setPassword("1");
                userService.addUser(user);
            }
        }
    }


}
