package com.demo.mqconsumer;

import com.alibaba.fastjson.JSON;
import com.demo.common.config.RabbitMqConfig;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: zhukai
 * Date: 2019/1/4
 * Time: 10:15
 * Description:
 */
@Component
public class FirstConsumer implements ChannelAwareMessageListener {
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {

        String msg = new String(message.getBody());
        // 每次只接收一个信息
        channel.basicQos(1);
        try {
            //告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了 否则消息服务器以为这条消息没处理掉 后续还会在发
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            if (msg.equals("ss1")){
                channel.basicPublish(message.getMessageProperties().getReceivedExchange(), message.getMessageProperties().getReceivedRoutingKey(),
                        MessageProperties.PERSISTENT_TEXT_PLAIN, "重新放入队列中的数据".getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("FirstConsumer  consumer fail");
            // 丢弃信息
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,false);
        }
        // 处理消息
        System.out.println("FirstConsumer {} handleMessage :"+msg);
    }
}
