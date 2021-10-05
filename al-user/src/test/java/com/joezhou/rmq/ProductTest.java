package com.joezhou.rmq;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * @author JoeZhou
 */
@Slf4j
public class ProductTest {

    /**
     * 模拟RMQ中的一个生产者，生产消息到broker
     */
    @SneakyThrows
    public static void main(String[] args) {
        // 创建一个生产者实例
        DefaultMQProducer producer = new DefaultMQProducer("test-producer-group");

        // 指定RMQ邮局地址，集群逗号分隔
        producer.setNamesrvAddr("localhost:9876");

        // 启动生产者
        producer.start();

        // 创建一个消息实例
        Message message = new Message("test-topic", "test-tag", "你好3".getBytes(RemotingHelper.DEFAULT_CHARSET));

        // 发送消息到broker
        SendResult sendResult = producer.send(message, 3000);

        // 查看发送是否成功
        log.info("获取消息的ID：{}", sendResult.getMsgId());
        log.info("获取消息的状态：{}", sendResult.getSendStatus());
    }
}
