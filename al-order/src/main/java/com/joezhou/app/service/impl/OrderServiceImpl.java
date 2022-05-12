package com.joezhou.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joezhou.app.entity.Order;
import com.joezhou.app.entity.Product;
import com.joezhou.app.feign.ProductFeign;
import com.joezhou.app.mapper.OrderMapper;
import com.joezhou.app.service.OrderService;
import com.joezhou.app.util.JsonResult;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2021-09-15
 */
@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private ProductFeign productFeign;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RocketMQTemplate rmqTemplate;

    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public int addOrder(Order order) {

        // 获取商品ID和购买数量
        Integer productId = order.getProductId();
        Integer number = order.getNumber();

        // 商品ID为null，订单失败
        if (productId == null || number == null) {
            throw new RuntimeException("订单中商品的ID或购买数量为null，下单失败..");
        }

        // 通过远程接口ProductFeign调用根据主键查询商品信息的方法
        log.info("远程调用商品微服务：根据商品主键查询商品信息");
        JsonResult productResult = productFeign.selectById(productId);

        // 将响应数据解析为Map数据类型
        if (productResult.getCode() <= 0) {
            throw new RuntimeException("远程调用商品微服务失败：商品不存在..");
        }

        // 获取响应数据中的data数据
        Product product = JsonResult.parseData(productResult.getData(), Product.class);
        log.info("远程调用商品微服务成功：查询到商品：{}", product);

        // 获取data数据中的商品名并设置到order实体中
        order.setProductName(product.getProductName());

        log.info("开始进行添加订单业务");
        if (orderMapper.insert(order) <= 0) {
            throw new RuntimeException("数据库添加Order操作失败，下单失败..");
        }

        // 下单成功后，扣减库存
        log.info("下单成功");
        JsonResult reduceResult = productFeign.reduceInventory(productId, number);
        if (reduceResult.getCode() <= 0) {
            throw new RuntimeException("远程调用商品微服务失败：扣减库存失败..");
        }
        log.info("远程调用商品微服务成功：扣减库存成功");

        // 使用RMQ向broker投递一个下单成功的消息
        rmqTemplate.convertAndSend("order:sms", order);

        return 1;
    }
}
