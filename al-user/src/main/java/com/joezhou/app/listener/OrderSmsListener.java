package com.joezhou.app.listener;

import com.joezhou.app.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 用于接收RMQ的broker投递的消息
 *
 * @author JoeZhou
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "user-consumer-group",
        topic = "order",
        selectorExpression = "sms",
        consumeMode = ConsumeMode.CONCURRENTLY,
        messageModel = MessageModel.CLUSTERING)
public class OrderSmsListener implements RocketMQListener<Order> {

    /**
     * 在broker投递消息时触发
     *
     * @param order 消息体
     */
    @Override
    public void onMessage(Order order) {
        log.info("接收到broker投递的消息：{}", order);
        log.info("准备向用户发短信通知");
    }
}
