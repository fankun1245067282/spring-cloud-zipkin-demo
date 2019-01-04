package com.fankun.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class DemoController {
    //不必使用static
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final RestTemplate restTemplate;

    @Autowired
    public DemoController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // ""和"/"是有区别的 /seluth/index
    @GetMapping("/hello")
    public String index(){
        String returnValue = "Hello World";
        logger.info("{} index():{}",getClass().getSimpleName(),returnValue);
       return returnValue;
    }


    /**
     * 完整的调用链路
     * sleuth-server
     *    -->zuul-server
     *        -->person-consumer
     *            -->person-service
     * @return
     */
    @GetMapping("/to/zuul/person-zoo/person/find/all")
    public Object toZuul(){
        logger.info("sleuth#toZuul():/to/zuul/person-zoo/person/find/all");
        //zuul-server是应用名称
        String url = "http://zuul-server/person-zoo/person/find/all";
        return restTemplate.getForObject(url,Object.class);
    }
}
