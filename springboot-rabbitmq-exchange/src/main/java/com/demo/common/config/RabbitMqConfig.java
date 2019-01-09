package com.demo.common.config;

import com.demo.mqcallback.MsgSendConfirmCallBack;
import com.demo.mqconsumer.DelayConsumer;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 * User: zhukai
 * Date: 2019/1/4
 * Time: 10:09
 * Description:
 */
@Configuration
public class RabbitMqConfig {
    /** 消息交换机的名字*/
    public static final String EXCHANGE = "exchangeTest";
    /** 消息交换机的名字*/
    public static final String EXCHANGE_TOPIC = "exchangeTopic";
    public static final String EXCHANGE_FANOUT = "exchangeFanout";

    /** 队列key1*/
    public static final String ROUTINGKEY1 = "queue_one_key1";
    /** 队列key2*/
    public static final String ROUTINGKEY2 = "queue_one_key2";
    public static final String ROUTINGKEY3 = "*.topic.*";
    public static final String ROUTINGKEY_TOPIC = "aaa.topic.*";

    /**
     * DLX死信交换机名字
     */
    public final static String DELAY_EXCHANGE_NAME = "delay_exchange";

    /**
     * message失效后进入的队列，也就是实际的消费队列
     */
    public final static String DELAY_PROCESS_QUEUE_NAME = "delay_process_queue";

    /**
     * 死信队列routing_key
     */
    public final static String DELAY_QUEUE_ROUTING_KEY = "delay_queue_key";
    /**
     * 延迟交换机
     */
    public final static String QUEUE_TTL_EXCHANGE_NAME = "queue_ttl_exchange";
    /**
     * 延迟队列名
     */
    public final static String QUEUE_TTL_NAME = "delay_ttl_queue1";

    /**
     * 延迟队列名2
     */
    public final static String QUEUE_TTL_NAME2 = "delay_ttl_queue2";

    /**
     * 延迟队列routing_key
     */
    public final static String QUEUE_TTL_ROUTING_KEY = "delay_ttl_queue_key";
    /**
     * 延迟队列routing_key
     */
    public final static String QUEUE_TTL_ROUTING_KEY2 = "delay_ttl_queue_key2";

    @Autowired
    private QueueConfig queueConfig;
    @Autowired
    private ExchangeConfig exchangeConfig;

    /**
     * 连接工厂
     */
    @Autowired
    private ConnectionFactory connectionFactory;

    /**
     将消息队列1和交换机进行绑定
     */
    @Bean
    public Binding binding_one() {
        return BindingBuilder.bind(queueConfig.firstQueue()).to(exchangeConfig.directExchange()).with(RabbitMqConfig.ROUTINGKEY1);
    }

    /**
     * 将消息队列2和交换机进行绑定
     */
    @Bean
    public Binding binding_two() {
        return BindingBuilder.bind(queueConfig.secondQueue()).to(exchangeConfig.directExchange()).with(RabbitMqConfig.ROUTINGKEY2);
    }

    @Bean
    public Binding binding_topic() {
        return BindingBuilder.bind(queueConfig.topicQueue()).to(exchangeConfig.topicExchange()).with(RabbitMqConfig.ROUTINGKEY3);
    }

    @Bean
    public Binding binding_topic1() {
        return BindingBuilder.bind(queueConfig.topicQueue1()).to(exchangeConfig.topicExchange()).with(RabbitMqConfig.ROUTINGKEY_TOPIC);
    }

    @Bean
    public Binding binding_fanout() {
        return BindingBuilder.bind(queueConfig.fanoutQueue()).to(exchangeConfig.fanoutExchange());
    }

    @Bean
    public Binding binding_fanout_for_third() {
        return BindingBuilder.bind(queueConfig.fanoutQueue1()).to(exchangeConfig.fanoutExchange());
    }


    /**
     * 绑定死信队列
     * @return
     */
    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(queueConfig.delayQueue()).to(exchangeConfig.delayExchange()).with(RabbitMqConfig.DELAY_QUEUE_ROUTING_KEY);
    }

    /**
     * 绑定延迟队列1
     * @return
     */
    @Bean
    public Binding queueTTLBinding() {
        return BindingBuilder.bind(queueConfig.delayTTLQueue()).to(exchangeConfig.perQueueTTLExchange()).with(RabbitMqConfig.QUEUE_TTL_ROUTING_KEY);
    }

    /**
     * 绑定延迟队列2
     * @return
     */
    @Bean
    public Binding queueTTLBinding2() {
        return BindingBuilder.bind(queueConfig.delayTTLQueue2()).to(exchangeConfig.perQueueTTLExchange()).with(RabbitMqConfig.QUEUE_TTL_ROUTING_KEY2);
    }

    /**
     * queue listener  观察 监听模式
     * 当有消息到达时会通知监听在对应的队列上的监听对象
     * @return
     */
    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer_one(){
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(connectionFactory);
        simpleMessageListenerContainer.addQueues(queueConfig.firstQueue());
        simpleMessageListenerContainer.setExposeListenerChannel(true);
        simpleMessageListenerContainer.setMaxConcurrentConsumers(5);
        simpleMessageListenerContainer.setConcurrentConsumers(1);
        simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.MANUAL); //设置确认模式手工确认
        return simpleMessageListenerContainer;
    }

    /**
     * 定义delay_process_queue队列的Listener Container
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    SimpleMessageListenerContainer processContainer(ConnectionFactory connectionFactory, DelayConsumer processReceiver) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(DELAY_PROCESS_QUEUE_NAME); // 监听delay_process_queue
        container.setMessageListener(new MessageListenerAdapter(processReceiver));
        return container;
    }

    /**
     * 定义rabbit template用于数据的接收和发送
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        /**若使用confirm-callback或return-callback，
         * 必须要配置publisherConfirms或publisherReturns为true
         * 每个rabbitTemplate只能有一个confirm-callback和return-callback
         */
        template.setConfirmCallback(msgSendConfirmCallBack());
        //template.setReturnCallback(msgSendReturnCallback());
        /**
         * 使用return-callback时必须设置mandatory为true，或者在配置中设置mandatory-expression的值为true，
         * 可针对每次请求的消息去确定’mandatory’的boolean值，
         * 只能在提供’return -callback’时使用，与mandatory互斥
         */
        //  template.setMandatory(true);
        return template;
    }

    /**
     * 消息确认机制
     * Confirms给客户端一种轻量级的方式，能够跟踪哪些消息被broker处理，
     * 哪些可能因为broker宕掉或者网络失败的情况而重新发布。
     * 确认并且保证消息被送达，提供了两种方式：发布确认和事务。(两者不可同时使用)
     * 在channel为事务时，不可引入确认模式；同样channel为确认模式下，不可使用事务。
     * @return
     */
    @Bean
    public MsgSendConfirmCallBack msgSendConfirmCallBack(){
        return new MsgSendConfirmCallBack();
    }
}
