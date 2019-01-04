package com.fankun;


import com.fankun.web.WebConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableDiscoveryClient
@EnableHystrix//激活hystrix
@Import(WebConfig.class)
public class PersonServerProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonServerProviderApplication.class, args);
    }
}
