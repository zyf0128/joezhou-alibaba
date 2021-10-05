package com.joezhou.app.predicate;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author JoeZhou
 */
@Slf4j
@Component
public class AgeRoutePredicateFactory extends AbstractRoutePredicateFactory<AgeRoutePredicateFactory.Config> {

    /**
     * 重写无参构造器，将配置类的类对象传递给父类
     */
    public AgeRoutePredicateFactory() {
        super(Config.class);
    }

    /**
     * 负责读取主配中的Age值，并且对位赋值给Config类的两个属性
     */
    @Override
    public List<String> shortcutFieldOrder() {
        // 将主配中Age属性的第1个值赋值给内部配置类的minAge属性
        // 将主配中Age属性的第2个值赋值给内部配置类的maxAge属性
        return Arrays.asList("minAge", "maxAge");
    }

    /**
     * 配置断言逻辑
     *
     * @param config 内部配置类
     * @return 断言结果
     */
    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return serverWebExchange -> {
            // 从请求中获取第一个age属性
            String ageStr = serverWebExchange.getRequest().getQueryParams().getFirst("age");

            // 如果请求中没有携带age参数，则直接放行
            if (ageStr == null) {
                return true;
            }

            // 判断age的值是否符和要求
            Integer age = Integer.valueOf(ageStr);
            return age >= config.getMinAge() && age <= config.getMaxAge();
        };
    }


    /**
     * 内部类用于接收和存储Age的两个值
     */
    @Data
    static class Config {
        private Integer minAge;
        private Integer maxAge;
    }

}
