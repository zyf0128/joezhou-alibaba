package com.joezhou.app.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author JoeZhou
 */
@Slf4j
//@Component
public class TokenGlobalFilter implements GlobalFilter, Ordered {

    /**
     * 配置过滤逻辑
     *
     * @param exchange 用于获取请求
     * @param chain    过滤器链对象，用于放行请求
     * @return 过滤结果
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("进入全局过滤器TokenGlobalFilter");
        // 获取请求和响应对象
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String token = request.getQueryParams().getFirst("token");
        if (token == null || "".equals(token.trim())) {
            log.error("token为空，全局过滤器阻止请求...");
            // 设置响应状态码为401未通过验证
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            // 直接执行响应操作，相当于阻止程序向下运行
            return response.setComplete();
        }

        // todo: 验证token是否有效
        log.info("token有效，全局过滤器通过请求...");
        // 放行请求
        return chain.filter(exchange);
    }

    /**
     * 配置过滤器的优先级
     *
     * @return 值越小，优先级越高
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
