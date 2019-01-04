package com.fankun;

import org.apache.kafka.common.errors.InvalidGroupIdException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.stream.Stream;

@SpringBootApplication
@EnableAsync
public class StreamKafkaApplication {
    public static void main(String[] args) {
        SpringApplication.run(StreamKafkaApplication.class,args);
//        KafkaAutoConfiguration autoConfiguration;
//        KafkaProperties kafkaProperties;
        InvalidGroupIdException invalidGroupIdException;
    }
}

