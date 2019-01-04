package com.fankun.producer.controller;

import com.fankun.stream.producer.MessageProducerBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 生产者controller
 */
@RestController
public class KafkaProducerController {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final String topic;

    private final MessageProducerBean messageProducerBean;

    @Autowired
    public KafkaProducerController(KafkaTemplate<String, String> kafkaTemplate,
                                   @Value("${kafka.topic}") String topic,
                                   MessageProducerBean messageProducerBean) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
        this.messageProducerBean = messageProducerBean;
    }

    /**
     * 通过kafkaTemplate发送
     * @param message
     * @return
     */
    @PostMapping("/message/send")
    public Boolean sendMessage(@RequestParam String message){
        System.out.println("send a message to kafka by kafkaTemplate:"+message);
        kafkaTemplate.send(topic, message);//send执行完，不一定发送到kafka
        return true;
    }

    /**
     * 通过messageProducerBean发送
     * @param message
     * @return
     */
    @PostMapping("/message/sendBean")
    public Boolean sendMessageBean(@RequestParam String message){
        System.out.println("send a message to kafka by messageProducerBean:"+message);
        messageProducerBean.send(message);//send执行完，不一定发送到kafka
        return true;
    }
}
