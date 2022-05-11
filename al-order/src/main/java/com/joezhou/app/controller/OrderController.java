package com.joezhou.app.controller;


import com.joezhou.app.entity.Order;
import com.joezhou.app.service.OrderService;
import com.joezhou.app.util.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping("/select-by-id")
    public JsonResult selectById(@RequestParam("id") Integer id) {
        log.info("接收到一个查询请求，根据订单ID查询订单信息");
        Order order = orderService.getById(id);
        if (order == null) {
            return JsonResult.fail(0, "查询订单失败");
        }
        log.info("查询请求执行成功，数据为 {}", order);
        return JsonResult.ok(order);
    }

    @RequestMapping("/insert")
    public JsonResult insert(@RequestParam("product-id") Integer productId,
                         @RequestParam(value = "number", defaultValue = "1") Integer number) {
        log.info("接收到一个下单请求，请求的商品ID为 {}, 购买数量为 {}", productId, number);
        int result = orderService.insert(productId, number);
        if (result > 0) {
            log.info("下单请求执行成功");
            return JsonResult.ok(1, "下单请求执行成功");
        } else {
            log.info("下单请求执行失败");
            return JsonResult.ok(0, "下单请求执行失败");
        }
    }


}

