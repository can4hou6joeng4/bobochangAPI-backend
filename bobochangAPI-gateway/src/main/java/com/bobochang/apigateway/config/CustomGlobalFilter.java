package com.bobochang.apigateway.config;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * 2023/5/30 - 10:37
 *
 * @author bobochang
 * @description 全局网关过滤器
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {
    // 自定义白名单地址
    public static final List<String> IP_WHITE_LIST = Collections.singletonList("128.0.0.1");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1、全局请求日志
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();// 请求路径
        String method = request.getMethodValue();// 请求方法
        log.info("请求唯一标识：{}", request.getId());
        log.info("请求目标路径：{}", request.getPath().value());
        log.info("请求方法：{}", request.getMethodValue());
        log.info("请求参数：{}", request.getQueryParams());
        log.info("请求来源地址：{}", request.getLocalAddress());
        log.info("custom global filter");
        // 2、访问控制，黑白名单（直接设置状态码将请求拦截掉并返回）
        ServerHttpResponse response = exchange.getResponse();
        if (!IP_WHITE_LIST.contains(request.getLocalAddress())) {
            return noAuthHandler(response);
        }
        // 3、用户鉴权 从请求头中取出指定参数并与其配对校验
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body") == null ? headers.getFirst("body") : null;
        // todo 从数据库中查出 ak 对应的用户并取出对应的 sk
        // todo 4、校验请求的接口是否在数据库中存在，以及请求的方法是否匹配
        // todo 5、请求转发 -> 响应日志
        return responseLog(exchange, chain);
    }

    /**
     * 使用装饰器模式对 response 做功能的增强，如调用日志、调用成功失败后逻辑
     *
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> responseLog(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();

            HttpStatus statusCode = originalResponse.getStatusCode();

            //todo 这里的返回码同样是本地接口，不是第三方的
            if (statusCode == HttpStatus.OK) {
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    // 等调用完转发的接口之后才会响应并执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        //log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 往返回值中写数据
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                // todo 7、调用成功时，可以新开一个线程发送到 MQ 中。接口调用次数 + 1
                                // rpcUserInterfaceInfo.invokeCount(interfaceInfoId,userId);

                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);// 释放掉内存

                                // 构建日志
                                String data = new String(content, StandardCharsets.UTF_8);//data
                                log.info("响应结果：{}", data);
                                return bufferFactory.wrap(content);
                            }));
                        } else {
                            // 8、调用失败，返回一个规范的错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 返回装饰过的response
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange);//降级处理返回数据
        } catch (Exception e) {
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }

    /**
     * 自定义统一 403 处理
     *
     * @param response
     * @return
     */
    private Mono<Void> noAuthHandler(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
