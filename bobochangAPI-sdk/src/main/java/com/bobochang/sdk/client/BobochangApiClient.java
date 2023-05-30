package com.bobochang.sdk.client;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.bobochang.sdk.model.User;
import com.bobochang.sdk.uils.SignUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 调用接口客户端
 */
public class BobochangApiClient {

    private final String GATEWAY_HOST = "http://localhost:9000";
    /**
     * 用户授权名
     */
    private final String accessKey;

    /**
     * 用户授权密钥
     */
    private final String secretKey;


    public BobochangApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    /**
     * 测试接口（Post 且 Json 格式）
     *
     * @param requestParams
     * @return
     */
    public String testUser(Object requestParams) {
        String json = JSONUtil.toJsonStr(requestParams);

        HttpResponse response = HttpRequest
                .post(GATEWAY_HOST + "/api/name/json")
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute();
        return response.body();
    }

    public String getUsernameByPost(User user) {
        String json = JSONUtil.toJsonStr(user);

        HttpResponse response = HttpRequest
                .post(GATEWAY_HOST + "/api/name/json")
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute();
        return response.body();

    }

    /**
     * 添加请求头
     *
     * @return
     */
    private Map<String, String> getHeaderMap(String body) {
        HashMap<String, String> map = new HashMap<>();
        map.put("accessKey", accessKey);// 授权名
        // map.put("nonce", RandomUtil.randomNumbers(4));// 随机数
        map.put("body", body);// 用户传递的参数
        map.put("sign", SignUtils.genSign(body, secretKey));// 生成的签名

        return map;
    }
}
