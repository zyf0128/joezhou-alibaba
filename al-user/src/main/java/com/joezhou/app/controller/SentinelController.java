package com.joezhou.app.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.joezhou.app.fallback.SentinelFallback;
import com.joezhou.app.feign.ProductFeign;
import com.joezhou.app.util.JsonResult;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author JoeZhou
 */
@Slf4j
@RestController
@RequestMapping("/api/sentinel")
public class SentinelController {

    @Qualifier("productFeign")
    @Autowired
    private ProductFeign productFeign;

    /**
     * 测试 QPS/线程数 流控
     *
     * @return 响应数据
     */
    @SentinelResource(value = "execute",
            fallbackClass = SentinelFallback.class,
            fallback = "fallback")
    @RequestMapping("execute")
    public JsonResult execute() {
        log.info("接收到请求：调用execute()");
        return JsonResult.ok(1, "execute()调用成功");
    }

    /**
     * 测试 QPS 关联流控
     *
     * @return 响应数据
     */
    @SentinelResource(value = "execute2",
            fallbackClass = SentinelFallback.class,
            fallback = "commNoArgFallBack")
    @RequestMapping("execute2")
    public JsonResult execute2() {
        log.info("接收到请求：调用execute2()");
        return JsonResult.ok(1, "execute2()调用成功");
    }

    /**
     * 测试RT降级
     *
     * @return 响应数据
     */
    @SentinelResource(value = "rt",
            fallbackClass = SentinelFallback.class,
            fallback = "fallback")
    @RequestMapping("rt")
    @SneakyThrows
    public JsonResult rt() {
        log.info("接收到请求：调用rt()");
        TimeUnit.SECONDS.sleep(1L);
        return JsonResult.ok(1, "rt()调用成功");
    }

    /**
     * 测试异常比例的计数器
     */
    private int exNum = 0;

    /**
     * 测试异常比例降级
     *
     * @return 响应数据
     */
    @SentinelResource(value = "ex",
            fallbackClass = SentinelFallback.class,
            fallback = "fallback")
    @RequestMapping("ex")
    @SneakyThrows
    public JsonResult ex() {
        log.info("接收到请求：调用ex()");
        // 模拟33.333333%的异常率
        if (exNum++ % 3 == 0) {
            throw new RuntimeException("每3次请求，手动抛出一个异常");
        }
        return JsonResult.ok(1, "ex()调用成功");
    }

    /**
     * 测试黑白名单
     *
     * @return 响应数据
     */
    @SentinelResource(value = "auth",
            fallbackClass = SentinelFallback.class,
            fallback = "fallback")
    @RequestMapping("auth")
    public JsonResult auth() {
        log.info("接收到请求：调用auth()");
        return JsonResult.ok(1, "auth()调用成功");
    }

    /**
     * 测试热点降级
     *
     * @return 响应数据
     */
    @SentinelResource(value = "param",
            fallbackClass = SentinelFallback.class,
            fallback = "paramFallback")
    @RequestMapping("param")
    public JsonResult param(@RequestParam(value = "name", required = false) String name,
                        @RequestParam(value = "age", required = false) Integer age) {
        log.info("接收到请求：调用param()，参数为 {} - {}", name, age);
        return JsonResult.ok(1, "param()调用成功");
    }

    @RequestMapping("open-feign")
    public JsonResult openFeign(@RequestParam("product-id") Integer productId) {
        log.info("接收到一个查询商品的请求，商品ID为：{}", productId);
        return productFeign.selectById(productId);
    }

}
