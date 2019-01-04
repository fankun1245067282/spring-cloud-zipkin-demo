package com.fankun.raw.api;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 使用原生的api进行开发
 */
public class KafkaProducerDemo {
    public static void main(String[] args) {
        //初始化配置
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers","localhost:9092");
        properties.setProperty("key.serializer",StringSerializer.class.getName());//xi
        properties.setProperty("value.serializer",JsonSerializer.class.getName());
        //创建 kafkaProducer
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer(properties);
        //发送 kafka 消息:ProducerRecord

        String topic = "test";
        Integer partition = 0;
        Long timestamp = System.currentTimeMillis();
        String key = "message-key";
        String value = "i am a good boy";
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, partition, timestamp, key, value);
        Future<RecordMetadata> metadataFuture = kafkaProducer.send(record);
        try {
            RecordMetadata  recordMetadata = metadataFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
