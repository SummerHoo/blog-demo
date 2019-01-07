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


}
