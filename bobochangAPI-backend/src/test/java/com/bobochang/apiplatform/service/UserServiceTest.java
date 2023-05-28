package com.bobochang.apiplatform.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    void userRegister() {
        System.out.println(userService.userRegister("bobochangTest", "Chy19990914", "Chy19990914"));
    }
}