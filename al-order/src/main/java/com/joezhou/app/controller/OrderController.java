package com.joezhou.app.controller;


import com.joezhou.app.entity.Order;
import com.joezhou.app.service.OrderService;
import com.joezhou.app.util.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2021-09-15
 */
@Slf4j
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 按主键查询订单记录
     * cli: http://localhost:8083/api/order/select-by-id?id=1
     *
     * @param id 订单记录主键
     * @return 成功返回对应订单记录，失败返回失败信息
     */
    @RequestMapping("select-by-id")
    public JsonResult selectById(@RequestParam("id") Integer id) {
        log.info("接收到按主键查询订单记录请求，参数id：{}", id);
        Order order = orderService.getById(id);
        log.info("根据主键查询到订单记录：{}", order);
        return order != null ? JsonResult.ok(order) : JsonResult.fail();
    }

    /**
     * 按主键修改订单记录
     * cli: http://localhost:8083/api/order/update-by-id?id=1&...
     *
     * @param order 订单实体
     * @return 响应操作成功/失败信息
     */
    @RequestMapping("update-by-id")
    public JsonResult updateById(Order order) {
        log.info("接收到按主键修改订单记录请求，参数order：{}", order);
        return orderService.updateById(order) ? JsonResult.ok() : JsonResult.fail();
    }

    /**
     * 按主键删除订单记录
     * cli: http://localhost:8083/api/order/delete-by-id?id=1
     *
     * @param id 订单记录主键
     * @return 响应操作成功/失败信息
     */
    @RequestMapping("delete-by-id")
    public JsonResult deleteById(@RequestParam Integer id) {
        log.info("接收到按主键删除订单记录请求，参数id：{}", id);
        return orderService.removeById(id) ? JsonResult.ok() : JsonResult.fail();
    }

    /**
     * 全查订单记录
     * cli: http://localhost:8083/api/user/list
     *
     * @return 成功返回全部订单记录，失败返回失败信息
     */
    @RequestMapping("list")
    public JsonResult list() {
        log.info("接收到按全查订单记录的请求");
        List<Order> orders = orderService.list();
        log.info("全查到全部订单记录：{}", orders);
        return orders != null && !orders.isEmpty() ? JsonResult.ok(orders) : JsonResult.fail();
    }

    /**
     * 添加订单记录
     * cli: http://localhost:8083/api/order/insert?username=a&
     *
     * @param order 订单实体
     * @return 响应操作成功/失败信息
     */
    @RequestMapping("insert")
    public JsonResult insert(Order order) {
        log.info("接收到添加订单记录请求，参数order：{}", order);
        return orderService.save(order) ? JsonResult.ok() : JsonResult.fail();
    }

    @RequestMapping("add-order")
    public JsonResult addOrder(@RequestParam("order-id") Integer orderId,
                         @RequestParam(value = "number", defaultValue = "1") Integer number) {
        log.info("接收到一个下单请求，请求的订单ID为 {}, 购买数量为 {}", orderId, number);
        int result = orderService.insert(orderId, number);
        if (result > 0) {
            log.info("下单请求执行成功");
            return JsonResult.ok(1, "下单请求执行成功");
        } else {
            log.info("下单请求执行失败");
            return JsonResult.ok(0, "下单请求执行失败");
        }
    }


}

