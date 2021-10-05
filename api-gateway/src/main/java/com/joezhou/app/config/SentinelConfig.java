package com.joezhou.app.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author JoeZhou
 */
@Configuration
public class SentinelConfig {

    /**
     * 视图解析器列表，用于展示限流后的响应内容
     */
    private List<ViewResolver> viewResolvers;

    /**
     * 请求读写器，负责操作请求和响应
     */
    private ServerCodecConfigurer serverCodecConfigurer;

    public SentinelConfig(ObjectProvider<List<ViewResolver>> provider,
                          ServerCodecConfigurer serverCodecConfigurer) {

        // Collections::emptyList 用于创建一个空的List
        this.viewResolvers = provider.getIfAvailable(Collections::emptyList);
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    /**
     * 管理一个全局限流过滤器，配置最高优先级
     * <bean id="sentinelGatewayFilter"
     * class="org.springframework.cloud.gateway.filter.GlobalFilter">
     *
     * @return 全局限流过滤器的实例
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public GlobalFilter sentinelGatewayFilter() {
        return new SentinelGatewayFilter();
    }

    /**
     * 管理一个全局限流异常处理器，配置最高优先级
     * <bean id="sentinelGatewayBlockExceptionHandler"
     * class="com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;"/>
     *
     * @return 全局限流异常处理器的实例
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
        // 需要传入视图解析器列表实例和请求读写器实例
        return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
    }

    /**
     * 在执行过滤器之前，对API网关发生限流时的响应内容进行配置
     */
    @PostConstruct
    public void initExceptionResponse() {
        // p1：用于获取请求对象
        // p2：异常对象
        GatewayCallbackManager.setBlockHandler((exchange, throwable) -> {

            // 设置一个map数据作为返回
            Map<String, Object> map = new HashMap<>(2);
            map.put("code", 0);
            map.put("msg", "QPS过高，API网关执行限流操作");

            // 设置响应码为200，响应数据格式为JSON，响应数据为map（转换为JSON）
            return ServerResponse.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(BodyInserters.fromObject(map));
        });
    }

    /**
     * 在执行过滤器之前，对API请求进行分组
     */
    @PostConstruct
    public void initApiDefinition(){

        // 用于存放API分组信息
        Set<ApiDefinition> definitions = new HashSet<>();

        // 凡是 `/user-service/api/user/**` 开头的请求，都默认归于userA分组
        ApiDefinition userA = new ApiDefinition("userA").setPredicateItems(new HashSet<ApiPredicateItem>(){{
            add(new ApiPathPredicateItem().setPattern("/user-service/api/user/**")
                    .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
        }});

        // 凡是 `/user-service/api/user/api-test` 请求，都默认归于userB分组
        ApiDefinition userB = new ApiDefinition("userB").setPredicateItems(new HashSet<ApiPredicateItem>(){{
            add(new ApiPathPredicateItem().setPattern("/user-service/api2/user/test"));
        }});

        definitions.add(userA);
        definitions.add(userB);

        // 将自定义分组信息加载到API网关的API分组中
        GatewayApiDefinitionManager.loadApiDefinitions(definitions);

    }

    /**
     * 在执行过滤器之前，对API的每一组设置限流规则
     */
    @PostConstruct
    public void initGatewayRules(){
        // 用于存放API限流规则
        Set<GatewayFlowRule> rules = new HashSet<>();

        // 对userA组的请求限流：QPS阈值为1，限流3秒钟
        // 对userB组的请求限流：QPS阈值为9，限流3秒钟
        rules.add(new GatewayFlowRule("userA").setCount(1).setIntervalSec(3));
        rules.add(new GatewayFlowRule("userB").setCount(5).setIntervalSec(3));

        // 将限流规则的set集合加载到API网关的规则管理器中
        GatewayRuleManager.loadRules(rules);
    }

}
