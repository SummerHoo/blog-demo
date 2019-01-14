package com.demo.mqproduct;

import com.demo.common.config.ExpirationMessagePostProcessor;
import com.demo.common.config.RabbitMqConfig;
import com.demo.mqconsumer.DelayConsumer;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
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
        for (int i = 0; i < 5; i++) {
            rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE, RabbitMqConfig.ROUTINGKEY1,
                    (Object) (String.valueOf(message)+i), correlationId);
        }
    }

    public void send2(String uuid,Object message) {
        CorrelationData correlationId = new CorrelationData(uuid);
        for (int i = 0; i < 5; i++) {
            rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE, RabbitMqConfig.ROUTINGKEY2,
                    (Object) (String.valueOf(message)+i), correlationId);
        }
    }

    /**
     * TopicExchange 生产者
     * @param uuid
     * @param message
     */
    public void sendTopic(String uuid,Object message) {
        CorrelationData correlationId = new CorrelationData(uuid);
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_TOPIC, "aaa.topic.bbb",
                message, correlationId);
    }

    public void sendFanout(String uuid,Object message) {
        CorrelationData correlationId = new CorrelationData(uuid);
        //中间是设置路由规则，由于是广播模式，这个规则会被抛弃
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_FANOUT, "", message);
    }


    public void sendPerQueueTTL(Object message) throws InterruptedException {
        DelayConsumer.latch = new CountDownLatch(3);
        for (int i = 1; i <= 3; i++) {
            rabbitTemplate.convertAndSend(RabbitMqConfig.QUEUE_TTL_EXCHANGE_NAME,RabbitMqConfig.QUEUE_TTL_ROUTING_KEY, message);
        }
        DelayConsumer.latch.await();
    }


    /**
     * 延迟时间不一致
     * @param message
     * @throws InterruptedException
     */
    public void sendPerQueueTTL2(Object message) throws InterruptedException {
        DelayConsumer.latch = new CountDownLatch(3);
        for (int i = 1; i <= 3; i++) {
            long expiration = i * 1000;
            rabbitTemplate.convertAndSend(RabbitMqConfig.QUEUE_TTL_EXCHANGE_NAME,RabbitMqConfig.QUEUE_TTL_ROUTING_KEY2,
                    message+" the expiration time is "+expiration,new ExpirationMessagePostProcessor(expiration));
        }
        DelayConsumer.latch.await();
    }
}
