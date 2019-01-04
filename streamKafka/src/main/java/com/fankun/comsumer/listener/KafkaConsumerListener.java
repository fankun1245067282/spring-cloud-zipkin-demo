package com.fankun.comsumer.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * kafka消费者监听器
 */
@Component
public class KafkaConsumerListener {

//    @KafkaListener(topics="${kafka.topic}")//
//    public void onMessage(String message) {
//        System.out.println("Kafka 消费者监听器，接受消息：" + message);
//    }
//
//    @KafkaListener(topics="${kafka.selfdefined.topic}")//
//    public void onMessageSelfdefined(String message) {
//        System.out.println("1--自定义通道 Kafka 消费者监听器，接受消息：" + message);
//    }
//
//    @KafkaListener(topics="${kafka.selfdefined.topic}")//
//    public void onMessageSelfdefined2(String message) {
//        System.out.println("2--自定义通道 Kafka 消费者监听器，接受消息：" + message);
//    }
}
