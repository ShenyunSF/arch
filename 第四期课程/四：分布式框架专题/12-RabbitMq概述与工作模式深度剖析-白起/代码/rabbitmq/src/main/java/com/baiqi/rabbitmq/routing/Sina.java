package com.baiqi.rabbitmq.routing;

import com.baiqi.rabbitmq.utils.RabbitConstant;
import com.baiqi.rabbitmq.utils.RabbitUtils;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 消费者新浪; 发布订阅模式
 * created by zhenzhong on 2020/11/29
 */
public class Sina {
    public static void main(String[] args) throws IOException {
        // 获取tcp长连接
        final Connection connection = RabbitUtils.getConnection();
        
        //获取虚拟连接
        final Channel channel = connection.createChannel();
        
        // 申明队列信息
        channel.queueDeclare(RabbitConstant.QUEUE_SINA, false, false, false,
                null
        );
        
        //queueBind用于将独立额和交换机绑定
        // 参数一： 队列名  参数二：交换机名 参数三：路由key，在路由模式时使用
        channel.queueBind(RabbitConstant.QUEUE_SINA, RabbitConstant.EXCHANGE_WEATHER, "");
        //声明每次消费完一条后才去消费下一条
        channel.basicQos(1);
        channel.basicConsume(RabbitConstant.QUEUE_SINA, false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(
                    String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                    byte[] body
            ) throws
                    IOException {
                String jsonSMS = new String(body);
                System.out.println("sina接受天气成功:" + jsonSMS);
                
            }
        });
        
    }
}
