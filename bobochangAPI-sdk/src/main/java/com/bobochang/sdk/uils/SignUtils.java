package com.bobochang.sdk.uils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * 签名工具 生成签名
 */
public class SignUtils {

    public static  String genSign(String body,String secretkey){
        // 使用SHA1摘要算法进行计算
        Digester md5 = new Digester(DigestAlgorithm.SHA1);
        String content = body + "." + secretkey;
        return md5.digestHex(content);
    }
}
