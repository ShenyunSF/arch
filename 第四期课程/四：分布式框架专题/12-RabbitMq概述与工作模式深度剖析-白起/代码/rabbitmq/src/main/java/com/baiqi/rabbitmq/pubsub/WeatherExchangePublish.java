package com.baiqi.rabbitmq.pubsub;

import com.baiqi.rabbitmq.utils.RabbitConstant;
import com.baiqi.rabbitmq.utils.RabbitUtils;
import com.baiqi.rabbitmq.workqueue.SMS;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * created by zhenzhong on 2020/11/29
 * 交换机--》 发布订阅模式 天气发布者
 */
public class WeatherExchangePublish
{
    public static void main(String[] args) throws IOException, TimeoutException
    {
        Connection connection = RabbitUtils.getConnection();
        final String input     = new Scanner(System.in).next();
        final Channel channel = connection.createChannel();
        channel.basicPublish(RabbitConstant.EXCHANGE_WEATHER,"",null,input.getBytes());
        channel.close();
        connection.close();
    }
}
