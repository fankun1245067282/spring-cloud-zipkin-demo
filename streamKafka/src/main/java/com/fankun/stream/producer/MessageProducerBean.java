package com.fankun.stream.producer;

import com.fankun.stream.messaging.MessageSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@EnableBinding({Source.class, MessageSource.class})
public class MessageProducerBean {
    @Autowired
    @Qualifier(Source.OUTPUT)  //Bean 名称
    private MessageChannel messageChannel;
    @Autowired
    private Source source;


    /**
     * 自定义通道
     */
    @Autowired
    private MessageSource messageSource;
    @Autowired
    @Qualifier(MessageSource.NAME)  //Bean 名称
    private MessageChannel selfdefinedMessageChannel;

    /**
     * 发送消息
     * @param message 消息内容
     */
    public  void send(String message){
        //通过消息管道发送消息
//        messageChannel.send(MessageBuilder.withPayload(message).build());
        //两种没有区别,messageChannel,source
        source.output().send(MessageBuilder.withPayload(message).build());


        messageSource.output().send(MessageBuilder.withPayload(message).build());
    }

}
