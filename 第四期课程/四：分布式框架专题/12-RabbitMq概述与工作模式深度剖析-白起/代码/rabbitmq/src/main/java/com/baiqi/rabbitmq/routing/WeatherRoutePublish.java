package com.baiqi.rabbitmq.routing;

import com.baiqi.rabbitmq.utils.RabbitConstant;
import com.baiqi.rabbitmq.utils.RabbitUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * created by zhenzhong on 2020/11/29
 * 交换机--》 路由模式，携带路由key，并且只接受对应路由key的信息 天气发布者
 */
public class WeatherRoutePublish {

    public static void main(String[] args) throws IOException, TimeoutException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("k1", "v1");
        map.put("k2", "v2");
        map.put("k3", "v3");
        map.put("k4", "v4");
        map.put("d1", "dv1");
        map.put("d2", "dv2");
        Connection connection = RabbitUtils.getConnection();
        final Channel channel = connection.createChannel();
        final Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<String, String> entry = iterator.next();
            channel.basicPublish(RabbitConstant.EXCHANGE_WEATHER, entry.getKey(), null, entry.getValue().getBytes());
        }
        channel.close();
        connection.close();
    }
}
