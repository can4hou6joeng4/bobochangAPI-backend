package com.bobochang.sdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.bobochang.sdk.model.AvatarParams;
import com.bobochang.sdk.model.User;
import com.bobochang.sdk.uils.HttpResponseDataUtils;
import com.bobochang.sdk.uils.Params2JsonUtils;
import com.bobochang.sdk.uils.SignUtils;
import com.google.gson.JsonElement;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.HashMap;
import java.util.Map;

/**
 * 调用接口客户端
 */
public class BobochangApiClient {

    private String GATEWAY_HOST = "http://175.178.234.102:9000";
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
                .post(GATEWAY_HOST + "/api/invoke/name/json")
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute();
        return response.body();

    }

    private Map<String, String> getHeaderMap(String body) {
        Map<String, String> hasMap = new HashMap<>();
        String nonce = RandomUtil.randomNumbers(4);
        String currentTime = String.valueOf(System.currentTimeMillis() / 1000);
        hasMap.put("accessKey", accessKey);
        hasMap.put("nonce", nonce);
        hasMap.put("body", body);
        hasMap.put("timestamp", currentTime);
        hasMap.put("sign", SignUtils.genSign(body, secretKey));
        return hasMap;
    }

    /**
     * 随机输出一句冷笑话
     * charset 返回编码类型[gbk|utf-8]默认utf-8
     * encode 返回格式类型[text|js|json]默认text
     *
     * @return 状态码和响应值
     */
    public ImmutablePair<Integer, String> randomMessage(String params) {
        String charset, encode;
        HttpRequest request = HttpRequest.get(GATEWAY_HOST + "/api/invoke/randomMessage");
        if (StringUtils.isNotBlank(params)) {
            JsonElement jsonParams = Params2JsonUtils.getJsonParams(params);
            charset = jsonParams.getAsJsonObject().get("charset").getAsString();
            encode = jsonParams.getAsJsonObject().get("encode").getAsString();
            // 不传参时使用默认参数
            request = StringUtils.isNotBlank(charset) ? request.form("charset", charset) : request.form("charset", "utf-8");
            request = StringUtils.isNotBlank(encode) ? request.form("encode", encode) : request.form("encode", "text");
        }

        HttpResponse response = request.addHeaders(getHeaderMap(params)).execute();
        return HttpResponseDataUtils.resData(response);
    }
}
