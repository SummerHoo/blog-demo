package com.demo.mqproduct;

import com.demo.common.config.RabbitMqConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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


}
