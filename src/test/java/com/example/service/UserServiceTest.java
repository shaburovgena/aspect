package com.example.service;

import com.example.domain.User;
import com.example.repos.UserRepo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private MailService mailService;

    @MockBean
    private PasswordEncoder passwordEncoder;


    @Test
    public void addNewUserTest() {
        User user = new User();

        User isUserAdded = userService.addUser(user);

        Assert.assertNotNull(isUserAdded);


    }

    @Test
    public void addExistUserTest() {
        User user = new User();
        user.setUsername("admin");

        Mockito.doReturn(new User())
                .when(userRepo).findByUsername("admin");

        User isUserAdded = userService.addUser(user);

        Assert.assertNull(isUserAdded);
        Mockito.verify(userRepo, Mockito.times(0)).save(user);


    }

    @Test
    public void activateUserTest() {
        User user = new User();
        user.setActivationCode("activation_code");

        Mockito.doReturn(user).when(userRepo).findByActivationCode("activate");

        boolean isUserActivate = userService.activateUser("activate");

        Assert.assertTrue(isUserActivate);
        Mockito.verify(userRepo, Mockito.times(1)).save(user);


    }
 @Test
    public void activateUserFailTest() {
        User user = new User();
        boolean isUserActivate = userService.activateUser("activate");
        Assert.assertFalse(isUserActivate);
        Mockito.verify(userRepo, Mockito.times(0)).save(user);
    }

    @Test
    public void updateUser() {
    }

    @Test(expected = ArithmeticException.class)
    public void errorExpected(){
        int i = 0;
        int j = 10/i;
    }
}