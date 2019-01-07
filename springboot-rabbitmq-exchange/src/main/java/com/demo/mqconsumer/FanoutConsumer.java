package com.demo.mqconsumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: zhukai
 * Date: 2019/1/7
 * Time: 18:26
 * Description:
 */
@Component
public class FanoutConsumer {
    @RabbitListener(queues = {"fanout-queue"}, containerFactory = "rabbitListenerContainerFactory")
    public void handleMessage(String message) throws Exception {
        // 处理消息
        System.out.println("FanoutConsumer {} handleMessage :"+message);
    }
}
