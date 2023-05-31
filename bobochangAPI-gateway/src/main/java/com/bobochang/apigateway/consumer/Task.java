package com.bobochang.apigateway.consumer;

import com.bobochang.apiplatform.provider.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class Task implements CommandLineRunner {
    @DubboReference
    private DemoService demoService;

    @Override
    public void run(String... args) {
        System.out.println(demoService.sayHello("bobochang"));
    }
}