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
            log.info("QPS过高或线程数过多，sentinel熔断降级");
            return JacksonUtil.build(0, "QPS过高或线程数过多，sentinel熔断降级");
        } else if (e instanceof DegradeException) {
            log.info("方法RT时间/异常比例超出阈值，sentinel熔断降级");
            return JacksonUtil.build(0, "方法RT时间/异常比例超出阈值，sentinel熔断降级");
        } else if (e instanceof AuthorityException) {
            log.info("请求来源权限不足，sentinel熔断降级");
            return JacksonUtil.build(0, "请求来源权限不足，sentinel熔断降级");
        } else if (e instanceof SystemBlockException) {
            log.info("系统限制，sentinel熔断降级");
            return JacksonUtil.build(0, "系统限制，sentinel熔断降级");
        }
        return JacksonUtil.build(0, e.getMessage());
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
