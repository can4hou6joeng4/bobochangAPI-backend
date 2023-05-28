package com.bobochang.sdk;

import com.bobochang.sdk.client.BobochangApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Data
@ComponentScan
@Configuration
@ConfigurationProperties(prefix = "bobochangapi.client")
public class BobochangApiClientAutoConfiguration {

    /**
     * 用户授权名
     */
    private String accessKey;

    /**
     * 用户授权密钥
     */
    private String secretKey;

    @Bean
    public BobochangApiClient bobochangApiClient() {
        return new BobochangApiClient(accessKey, secretKey);
    }
}
