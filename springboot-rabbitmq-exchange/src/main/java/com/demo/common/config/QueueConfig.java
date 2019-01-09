package com.demo.common.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhukai
 * Date: 2019/1/4
 * Time: 10:05
 * Description:队列配置  可以配置多个队列
 */
@Configuration
public class QueueConfig {
    @Bean
    public Queue firstQueue() {
        /**
         durable="true" 持久化 rabbitmq重启的时候不需要创建新的队列
         auto-delete 表示消息队列没有在使用时将被自动删除 默认是false
         exclusive  表示该消息队列是否只在当前connection生效,默认是false
         */
        return new Queue("first-queue",true,false,false);
    }

    @Bean
    public Queue secondQueue() {
        return new Queue("second-queue",true,false,false);
    }

    @Bean
    public Queue topicQueue() {
        return new Queue("topic-queue",true,false,false);
    }

    @Bean
    public Queue topicQueue1() {
        return new Queue("topic-queue1",true,false,false);
    }

    @Bean
    public Queue fanoutQueue1() {
        return new Queue("fanout-queue1",true,false,false);
    }

    @Bean
    public Queue fanoutQueue() {
        return new Queue("fanout-queue",true,false,false);
    }


    /**
     * 实际消费队列
     * @return
     */
    @Bean
    public Queue delayQueue() {
        return new Queue(RabbitMqConfig.DELAY_PROCESS_QUEUE_NAME,true,false,false);
    }

    /**
     * 延迟队列
     * @return
     */
    @Bean
    public Queue delayTTLQueue() {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("x-dead-letter-exchange",RabbitMqConfig.DELAY_EXCHANGE_NAME);
        paramMap.put("x-dead-letter-routing-key",RabbitMqConfig.DELAY_QUEUE_ROUTING_KEY);
        paramMap.put("x-message-ttl",3000);
        return new Queue(RabbitMqConfig.QUEUE_TTL_NAME,true,false,false,paramMap);
    }

    /**
     * 延迟队列，超时时间不一致
     * @return
     */
    @Bean
    public Queue delayTTLQueue2() {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("x-dead-letter-exchange",RabbitMqConfig.DELAY_EXCHANGE_NAME);// 死信交换机的名称
        paramMap.put("x-dead-letter-routing-key",RabbitMqConfig.DELAY_QUEUE_ROUTING_KEY);// 死信交换机的routing_key
        return new Queue(RabbitMqConfig.QUEUE_TTL_NAME2,true,false,false,paramMap);
    }

}
