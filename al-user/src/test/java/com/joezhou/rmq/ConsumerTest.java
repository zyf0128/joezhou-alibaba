package com.joezhou.rmq;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * 模拟RMQ中的一个消费者，消费broker中的消息
 * 为了防止主线程退出，使用main替代junit方法
 *
 * @author JoeZhou
 */
@Slf4j
public class ConsumerTest {

    @SneakyThrows
    public static void main(String[] args) {

        // 创建一个消费者实例
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test-consumer-group");

        // 指定RMQ邮局地址，集群逗号分隔
        consumer.setNamesrvAddr("localhost:9876");

        // 消费者订阅主题和标签：订阅test-topic主题下的所有标签
        consumer.subscribe("test-topic", "*");

        // 消费者设置监听
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            try {
                for (MessageExt msg : msgs) {
                    log.info("消费到消息：{}", new String(msg.getBody()));
                }
            } catch (Exception e) {
                log.error("消费失败", e);

                // 稍后重新尝试
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
            // 返回消费成功
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        // 启动生产者
        consumer.start();
    }

}
