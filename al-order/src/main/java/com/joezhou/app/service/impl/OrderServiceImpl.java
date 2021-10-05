package com.joezhou.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joezhou.app.entity.Order;
import com.joezhou.app.feign.ProductFeign;
import com.joezhou.app.mapper.OrderMapper;
import com.joezhou.app.service.OrderService;
import com.joezhou.app.util.JacksonUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

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
    public int insert(Integer productId, Integer number) {

        // 通过远程接口ProductFeign调用根据主键查询商品信息的方法
        log.info("远程调用商品微服务：根据商品主键查询商品信息");
        String productData = productFeign.selectById(productId);

        // 将响应数据解析为Map数据类型
        Map productDataMap = JacksonUtil.parseToMap(productData);
        if ((Integer) productDataMap.get("code") <= 0) {
            log.info("远程调用商品微服务失败：未查寻到该商品");
            return 0;
        }

        // 获取响应数据中的data数据
        Map dataMap = (Map) productDataMap.get("data");
        log.info("远程调用商品微服务成功：查询到商品：{}", dataMap);

        // 获取data数据中的商品名productName
        String productName = (String) dataMap.get("productName");
        log.info("获取到商品名：{}", productName);

        // 准备一个Order实体，购买人暂时写死，商品名从远程商品数据中获取
        log.info("开始准备订单数据");
        Order order = new Order();
        order.setUsername("admin");
        order.setProductId(productId);
        order.setNumber(number);
        order.setProductName(productName);
        log.info("订单数据准备完成：{}", order);

        log.info("开始进行添加订单业务");
        int insertResult = orderMapper.insert(order);
        if (insertResult > 0) {
            log.info("下单成功");
            // 下单成功后，扣减库存
            String reduceResult = productFeign.reduceInventory(productId, number);
            Map reduceResultDataMap = JacksonUtil.parseToMap(reduceResult);
            if((Integer)reduceResultDataMap.get("code") <= 0){
                log.info("远程调用商品微服务失败：扣减库存失败");
                return 0;
            }
            log.info("远程调用商品微服务成功：扣减库存成功");

            // 使用RMQ向broker投递一个下单成功的消息
            rmqTemplate.convertAndSend("order:sms", order);

            return 1;
        } else {
            log.info("下单失败");
            return 0;
        }
    }
}
