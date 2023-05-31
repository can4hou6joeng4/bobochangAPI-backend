package com.bobochang.apiinterface.controller;

import com.bobochang.apiinterface.uils.SignUtils;
import com.bobochang.sdk.model.User;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
    public String getUserNameByPost(@RequestBody User user, HttpServletRequest request) {
        // 下述操作都已经在网关拦截器中实现
        /*String accessKey = request.getHeader("accessKey");
        *//*
         String nonce = request.getHeader("nonce");
         String timestamp = request.getHeader("timestamp");
        if (Long.parseLong(nonce) > 10000) {
            throw new RuntimeException("无权限");
        }
        // 时间和当前时间不能超过 5 分钟
        if (System.currentTimeMillis() - Long.parseLong(timestamp) > TimeUnit.MINUTES.toMillis(5)) {
            throw new RuntimeException("无权限");
        }*//*
        String sign = request.getHeader("sign");
        String body = request.getHeader("body");
        // 查询用户是否有 accessKey
        // todo 实际情况中是从数据库中查出 accessKey
        if (!accessKey.equals("6b5b9a2e096f54afa2538952f3c1425f")) {
            throw new RuntimeException("无权限");
        }
        // todo 实际情况中是从数据库中查出 secretKey
        String serverSign = SignUtils.genSign(body, "337377b1423e5c473827b98eeae4eda2");
        if (!sign.equals(serverSign)) {
            throw new RuntimeException("无权限");
        }*/
        return "【POST请求方式 - JSON传参】 你的名字是：" + user.getUsername();
    }
}
