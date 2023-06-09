package com.bobochang.apiinterface.controller;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.bobochang.sdk.model.AvatarParams;
import com.bobochang.sdk.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * 名称 API
 * 查询名称接口
 *
 * @author 啵啵肠
 */
@RestController
@RequestMapping("/invoke")
public class InterfaceController {
    @GetMapping
    public String getNameByGet(String name) {
        return "【GET请求方式】 你的名字是：" + name;
    }

    @PostMapping("/param")
    public String getNameByPost(@RequestParam String name) {
        return "【POST请求方式 - URL传参】 你的名字是：" + name;
    }

    @PostMapping("/name/json")
    public String getUserNameByPost(@RequestBody User user) {
        return "【POST请求方式 - JSON传参】 你的名字是：" + user.getUsername();
    }

    /**
     * 随机输出一句冷笑话
     *
     * @param charset 返回编码类型[gbk|utf-8]默认utf-8
     * @param encode  返回格式类型[text|js|json]默认text
     * @return 接口封装的返回值，格式为{"状态码":"返回信息"}，并将结果返回给 SDK，交由 SDK 进行解析
     */
    @GetMapping("/randomMessage")
    public ImmutablePair<Integer, String> randomMessage(String charset, String encode) {
        HttpRequest request = HttpRequest.get("https://api.btstu.cn/yan/api.php");
        if (StringUtils.isNotBlank(charset)) {
            request.form(charset, charset);
        }

        if (StringUtils.isNotBlank(encode)) {
            request.form(encode, encode);
        }
        HttpResponse response = request.execute();
        return ImmutablePair.of(response.getStatus(), response.body());
    }

}
