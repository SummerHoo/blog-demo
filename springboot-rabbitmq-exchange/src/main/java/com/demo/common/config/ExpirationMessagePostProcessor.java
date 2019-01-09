package com.demo.common.config;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;

/**
 * Created with IntelliJ IDEA.
 * User: zhukai
 * Date: 2019/1/6
 * Time: 14:04
 * Description:
 */
public class ExpirationMessagePostProcessor implements MessagePostProcessor {
    private final Long ttl; // 毫秒
    public ExpirationMessagePostProcessor(Long ttl) {
        this.ttl = ttl;
    }
    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        message.getMessageProperties()
                .setExpiration(ttl.toString()); // 设置per-message的失效时间
        return message;
    }
}
