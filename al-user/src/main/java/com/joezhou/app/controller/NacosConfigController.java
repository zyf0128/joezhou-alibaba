package com.joezhou.app.controller;

import com.joezhou.app.util.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JoeZhou
 */
@RestController
@RequestMapping("api/nacos-config")
@RefreshScope
@Slf4j
public class NacosConfigController {

    @Value("${env}")
    private String env;

    @RequestMapping("get-env")
    public String getEnv() {
        log.info("接到一个查询请求：获取到当前nacos配置文件的env属性为 {}", env);
        return JacksonUtil.build(env);
    }
}
