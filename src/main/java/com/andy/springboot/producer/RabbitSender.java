package com.andy.springboot.producer;

import com.andy.springboot.entity.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/**
 * @author lianhong
 * @description 消息发送者
 * @date 2019/8/21 0021下午 1:25
 */
@Component
public class RabbitSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            System.out.println("correlationData = " + correlationData);
            System.out.println("ack = " + ack);
            System.out.println("cause = " + cause);
            if (!ack) {
                System.err.println("异常处理。。。 ");
            }

            System.out.println("可靠性投递-自动应答，更新数据库");
        }
    };

    final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(org.springframework.amqp.core.Message message, int replyCode, String replyText, String exchange, String routingKey) {
            System.out.println("exchange = " + exchange);
            System.out.println("routingKey = " + routingKey);
            System.out.println("replyCode = " + replyCode);
            System.out.println("replyText = " + replyText);
        }
    };


    public void send (Object message, Map<String,Object> properties,String exchange,String routingKey) {
        MessageHeaders mhs = new MessageHeaders(properties);
        Message msg = MessageBuilder.createMessage(message, mhs);
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        CorrelationData correlationData = new CorrelationData();
        //id+时间戳 全局唯一
        correlationData.setId(String.valueOf(UUID.randomUUID())+System.currentTimeMillis());
        rabbitTemplate.convertAndSend(exchange,routingKey,msg,correlationData);
    }

    //发送消息方法调用: 构建自定义对象消息
    public void sendOrder(Order order, String exchange, String routingKey) {
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString() + System.currentTimeMillis());
        rabbitTemplate.convertAndSend(exchange,routingKey,order,correlationData);
    }

}
