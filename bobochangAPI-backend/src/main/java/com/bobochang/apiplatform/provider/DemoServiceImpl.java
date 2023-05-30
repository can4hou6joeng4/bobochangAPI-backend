package com.bobochang.apiplatform.provider;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;

@Slf4j
@DubboService
public class DemoServiceImpl implements DemoService {
    @Override
    public String sayHello(String name) {
        log.info("request from consume："+ RpcContext.getContext().getRemoteAddress());
        return "hello " + name;
    }
}
