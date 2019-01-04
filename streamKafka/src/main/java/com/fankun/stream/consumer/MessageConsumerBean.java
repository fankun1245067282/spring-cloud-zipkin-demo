package com.fankun.stream.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 消息消费通道
 *
 */
@Component
@EnableBinding({Sink.class})
public class MessageConsumerBean {
    @Autowired
    @Qualifier(Sink.INPUT)  //Bean 名称
    private SubscribableChannel subscribableChannel;

    @Autowired
    private Sink sink;


    /**
     * spring Listen监听启动，然后消息都不从这消费了，这个是异步的原因。。。
     * 同时两个spring Listen，只会有一个listener从通道消费。。。
     */
    //当字段注入完成后的回调
    @PostConstruct
    public void init(){
        System.out.println("==============----------subscribableChannel-------------");
//        subscribableChannel.subscribe(new MessageHandler() {
//            @Override
//            public void handleMessage(Message<?> message) throws MessagingException {
//                System.out.println("sink message:"+message.getPayload());
//            }
//        });
        //异步回调
        sink.input().subscribe(new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                System.out.println("sink message:"+message.getPayload());
            }
        });
    }

    /**
     * 这3种是轮询的一样 integration
     * @param message
     */
    @ServiceActivator(inputChannel = Sink.INPUT)
    public void onMessage(Object message){
        System.out.println("onMessage:"+message);
    }

    @StreamListener(Sink.INPUT)
    public void streamListener(Object message){
        System.out.println("streamListener:"+message);
    }
}
