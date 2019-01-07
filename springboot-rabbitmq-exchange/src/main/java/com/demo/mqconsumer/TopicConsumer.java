package com.demo.mqconsumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: zhukai
 * Date: 2019/1/7
 * Time: 18:37
 * Description:
 */
@Component
public class TopicConsumer {
    @RabbitListener(queues = {"topic-queue"}, containerFactory = "rabbitListenerContainerFactory")
    public void handleMessage(String message) throws Exception {
        // 处理消息
        System.out.println("TopicConsumer {} handleMessage :"+message);
    }
}
