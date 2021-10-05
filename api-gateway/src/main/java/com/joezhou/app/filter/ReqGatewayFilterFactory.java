package com.joezhou.app.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author JoeZhou
 */
@Slf4j
@Component
public class ReqGatewayFilterFactory extends AbstractGatewayFilterFactory<ReqGatewayFilterFactory.Config> {

    /**
     * 重写无参构造器，将配置类的类对象传递给父类
     */
    public ReqGatewayFilterFactory() {
        super(Config.class);
    }

    /**
     * 负责读取主配中的Req值，并且对位赋值给Config类的两个属性
     */
    @Override
    public List<String> shortcutFieldOrder() {
        // 将主配中Req属性的第1个值赋值给内部配置类的requestHeader属性
        // 将主配中Req属性的第2个值赋值给内部配置类的requestParam属性
        return Arrays.asList("requestHeader", "requestParam");
    }

    /**
     * 配置过滤逻辑
     *
     * @param config 内部配置类
     * @return 过滤结果
     */
    @Override
    public GatewayFilter apply(Config config) {

        // p1：用于获取请求
        // p2：用于放行请求
        return (exchange, chain) -> {

            // 获取当前请求
            ServerHttpRequest request = exchange.getRequest();

            // 根据主配中Req的值来决定是否对请求头中的a和请求参数中的b进行打印日志
            if (config.getRequestHeader()) {
                log.info("响应头中a的值为: {}", request.getHeaders().get("a"));
            }
            if (config.getRequestParam()) {
                log.info("请求参数中b的值为: {}", request.getQueryParams().get("b"));
            }

            // 放行请求
            return chain.filter(exchange);
        };
    }

    /**
     * 内部类用于接收和存储Reg的两个值：requestHeader和requestParam
     */
    @Data
    static class Config {
        private Boolean requestHeader;
        private Boolean requestParam;
    }
}
