package com.bobochang.sdk.uils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;

/**
 * 字符串转换为 Json 对象
 */
public class Params2JsonUtils {
    @SneakyThrows
    public static JsonElement getJsonParams(String params) {
        // 如果参数格式错误不为 Json，会自动抛出异常，异常交给调用者捕获处理
        return JsonParser.parseString(params);
    }
}
