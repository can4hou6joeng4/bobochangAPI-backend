package com.bobochang.apiinterface.controller;

import com.bobochang.sdk.model.User;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.web.bind.annotation.*;

/**
 * 名称 API
 * 查询名称接口
 *
 * @author 啵啵肠
 */
@RestController
@RequestMapping("/name")
public class NameController {
    @GetMapping
    public String getNameByGet(String name) {
        return "【GET请求方式】 你的名字是：" + name;
    }

    @PostMapping("/param")
    public String getNameByPost(@RequestParam String name) {
        return "【POST请求方式 - URL传参】 你的名字是：" + name;
    }

    @PostMapping("/json")
    public String getUserNameByPost(@RequestBody User user) {
        return "【POST请求方式 - JSON传参】 你的名字是：" + user.getUsername();
    }
}
