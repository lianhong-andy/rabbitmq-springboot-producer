package com.andy.springboot;

import com.andy.springboot.producer.RabbitSender;
import com.andy.springboot.entity.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProducerApplicationTests {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @Test
    public void contextLoads() {
    }

    @Autowired
    private RabbitSender rabbitSender;

    @Test
    public void testSender1() throws InterruptedException {
        Map<String,Object> properties = new HashMap<>();
        properties.put("number","123456");
        properties.put("send_time",simpleDateFormat.format(new Date()));
        String exchange = "exchange-1";

        String confirmCallBackRoutingKey = "springboot.hello";
        String returnCallBackRoutingKey = "spring.hello";

        //测试路由成功的回调
        rabbitSender.send("hello rabbitMQ for Springboot!",properties,exchange,confirmCallBackRoutingKey);

        Thread.sleep(3000);
        System.out.println("===============================================");

        //测试路由不可达的回调
        rabbitSender.send("hello rabbitMQ for Springboot!",properties,exchange,returnCallBackRoutingKey);
    }

    @Test
    public void testSender2() throws Exception {
        String exchange = "exchange-2";
        String confirmCallBackRoutingKey = "springboot.def";
        Order order = new Order("001", "第一个订单");

        //测试路由成功的回调
        rabbitSender.sendOrder(order,exchange,confirmCallBackRoutingKey);

    }


}
