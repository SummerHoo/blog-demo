package com.demo.mqproduct;

import com.demo.common.config.ExpirationMessagePostProcessor;
import com.demo.common.config.RabbitMqConfig;
import com.demo.mqconsumer.DelayConsumer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * Created with IntelliJ IDEA.
 * User: zhukai
 * Date: 2019/1/4
 * Time: 10:13
 * Description:
 */
@Component
public class FirstProduct {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * DirectExchange 生产者 发送消息
     * @param uuid
     * @param message  消息
     */
    public void send(String uuid,Object message) {
        CorrelationData correlationId = new CorrelationData(uuid);
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE, RabbitMqConfig.ROUTINGKEY2,
                message, correlationId);
    }


    /**
     * TopicExchange 生产者
     * @param uuid
     * @param message
     */
    public void sendThird(String uuid,Object message) {
        CorrelationData correlationId = new CorrelationData(uuid);
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_TOPIC, "aa.topic.bbb",
                message, correlationId);
    }

}
