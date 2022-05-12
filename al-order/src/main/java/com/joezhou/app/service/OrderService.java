package com.joezhou.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.joezhou.app.entity.Order;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author ${author}
 * @since 2021-09-15
 */
public interface OrderService extends IService<Order> {

    /**
     * 添加一个订单
     *
     * @param order 订单实体
     * @return 正数表示成功，零或负数表示失败
     */
    int addOrder(Order order);
}
