package com.joezhou.rmq;

import com.joezhou.app.UserApp;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author JoeZhou
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApp.class)
public class MessageTypeTest {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 测试RMQ消息的同步发送效果
     */
    @Test
    public void testSyncSend() {
        SendResult sendResult = rocketMQTemplate.syncSend("msg-type:sync",
                "同步消息测试",
                3000L);
        log.info("同步发送完毕，返回结果：{}", sendResult.getSendStatus());
    }

    /**
     * 测试RMQ消息的异步发送效果
     */
    @Test
    public void testAsyncSend() {
        rocketMQTemplate.asyncSend("msg-type:async",
                "异步消息测试",
                new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        log.info("异步发送成功，返回结果：{}", sendResult.getSendStatus());
                    }

                    @Override
                    public void onException(Throwable e) {
                        log.error("异步发送失败", e);
                    }
                }
        );
        log.info("如果我先输出，证明是异步发送");
    }

    /**
     * 测试RMQ消息的单项发送效果
     */
    @Test
    public void testSendOneWay() {
        rocketMQTemplate.sendOneWay("msg-type:one-way", "单项消息测试");
        log.info("单项发送完毕");
    }


    /**
     * 测试RMQ消息的单项顺序发送效果
     */
    @Test
    public void testSendOneWayOrderly() {
        for (int i = 0; i < 10; i++) {
            // 根据最后一个字符串参数进行hash()取余
            rocketMQTemplate.sendOneWayOrderly("msg-type:one-way-orderly", "单项顺序消息测试","xx");
        }

        log.info("单项顺序发送完毕");
    }


}
