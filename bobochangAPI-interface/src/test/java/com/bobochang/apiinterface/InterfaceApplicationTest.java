package com.bobochang.apiinterface;

import com.bobochang.sdk.client.BobochangApiClient;
import com.bobochang.sdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class InterfaceApplicationTest {

    @Resource
    private BobochangApiClient bobochangApiClient;

    @Test
    void test() {
        User user = new User();
        user.setUsername("bobochang");
        System.out.println(bobochangApiClient.getUsernameByPost(user));
    }

}