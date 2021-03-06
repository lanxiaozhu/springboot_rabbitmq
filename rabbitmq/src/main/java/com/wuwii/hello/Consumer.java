package com.wuwii.hello;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @version 1.0
 * @since <pre>2018/3/15 18:06</pre>
 */
public class Consumer {
    private final static String QUEUE_NAME = "testMain";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接
        ConnectionFactory factory = new ConnectionFactory();
        // 设置 RabbitMQ 的主机名，默认localhost
        factory.setHost("192.168.70.248");
        // 设置端口， 默认 端口5672
        factory.setPort(5672);
        // 设置 Username，默认 guest
        // 设置 Password，默认 guest

        // 创建一个连接
        Connection connection = factory.newConnection();
        // 创建一个通道
        Channel channel = connection.createChannel();
        // 指定一个队列
        // queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments)
        // 参数1 queue ：队列名
        // 参数2 durable ：是否持久化
        // 参数3 exclusive ：仅创建者可以使用的私有队列，断开后自动删除
        // 参数4 autoDelete : 当所有消费客户端连接断开后，是否自动删除队列
        // 参数5 arguments
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        // 创建队列消费者
        com.rabbitmq.client.Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        // basicConsume(String queue, boolean autoAck, Consumer callback)
        // 参数1 queue ：队列名
        // 参数2 autoAck ： 是否自动ACK，消息应答，为true关闭它  false 为关闭自动应答机制
        // 参数3 callback ： 消费者对象的一个接口，用来配置回调
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}
