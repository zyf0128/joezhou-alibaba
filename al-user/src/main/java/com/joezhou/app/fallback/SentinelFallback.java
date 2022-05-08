package com.joezhou.app.fallback;

import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.joezhou.app.util.JacksonUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author JoeZhou
 */
@Slf4j
public class SentinelFallback {


    /**
     * 通用的无参控制方法的降级方法
     *
     * @param e 控制方法爆发的异常实例
     * @return 兜底数据
     */
    public static String commNoArgFallBack(Throwable e) {
        if (e instanceof FlowException) {
            log.error("流控降级", e);
            return JacksonUtil.build(0, "您访问的太快/或当前访问人数过多，请稍后重试...");
        } else if (e instanceof DegradeException) {
            log.error("熔断降级", e);
            return JacksonUtil.build(0, "服务器响应超时或失败次数过多，请稍后重试...");
        } else if (e instanceof AuthorityException) {
            log.info("授权降级");
            return JacksonUtil.build(0, "您无权访问该服务，请联系管理员...");
        } else if (e instanceof SystemBlockException) {
            log.info("热点降级");
            return JacksonUtil.build(0, "系统限制，sentinel熔断降级");
        }
        return JacksonUtil.build(0, "服务器未知异常，请稍后重试...");
    }


    /**
     * 专门针对SentinelController.param()设置的降级方法
     *
     * @param e 控制方法爆发的异常实例
     * @return 兜底数据
     */
    public static String paramFallback(String name, Integer age, Throwable e) {
        if (e instanceof ParamFlowException) {
            log.info("触发热点降级规则，sentinel熔断降级");
            return JacksonUtil.build(0, "触发热点降级规则，sentinel熔断降级");
        }
        return JacksonUtil.build(0, e.getMessage());
    }

}
