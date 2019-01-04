package com.fankun.stream.messaging;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * 自定义通道
 */
public interface MessageSource {
    String NAME = "fankun";
    @Output(NAME)
    MessageChannel output();
    //可以定义多个通道
}
