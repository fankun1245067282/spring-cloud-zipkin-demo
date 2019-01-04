#### 增加@EnableZuulProxy

#### 配置路由规则

基本模式：zuul.routes.${application-name}:/${app-url-prefix}/**



#### 整合ribbon

`Content-Type:application/json;charset=UTF-8`这是客户端的请求类型，要与服务端的接受类型匹配

accept：text/html;这是客户端的接受类型，服务端要返回客户端的接受的类型数据

##streamkafka

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-stream-binder-kafka</artifactId>
</dependency>
```

Alternatively, you can also use the Spring Cloud Stream Kafka Starter.

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-stream-kafka</artifactId>
</dependency>
```

官方说这两种都可以的

```
    compile('org.springframework.cloud:spring-cloud-stream')
    compile('org.springframework.cloud:spring-cloud-stream-binder-kafka')
    compile('org.springframework.kafka:spring-kafka')自动包括在内
```

#### kafka主要用途

消息中间件
流式计算处理
日志



### spring kafka

#### 设计模式

Spring 社区对data(spring-data)数据操作，有一个基本的模式，Template模式

JDBC:JdbcTemplate

Redis:RedisTemplate

Kafka:KafkaTemplate

Jms:JmsTemplate

Rest:RestTemplate

XxxTemplate 一定实现 XxxOperations

KafkaTemplate 实现 KafkaOperations



####Maven依赖

```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```



### Spring Boot Kafka

自动装备器：KafkaAutoConfiguration

其中 kafkaTemplate会自动装配



```java
@Bean
@ConditionalOnMissingBean({KafkaTemplate.class})
public KafkaTemplate<?, ?> kafkaTemplate(ProducerFactory<Object, Object> kafkaProducerFactory, ProducerListener<Object, Object> kafkaProducerListener) {
    KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate(kafkaProducerFactory);
    kafkaTemplate.setProducerListener(kafkaProducerListener);
    kafkaTemplate.setDefaultTopic(this.properties.getTemplate().getDefaultTopic());
    return kafkaTemplate;
}
```



###问题

1、kafka使用场景？

​	高性能的Stream处理

2、kafka如何使用后如何删除？



3、怎么没有看到Broker设置？

​	Broker不需要设置，它是单独启动

4、consumer为什么分组？

​	consumer需要定义不同逻辑分组，（是不是几个可以同时消费一个主题，否则，重启一个只能重新消费）

## Spring Cloud Stream (下)

RabbitMQ： AMQP,JMS 规范

Kafka：相对松散的消息队列协议

企业整合模式 Integration

### Spring Cloud Stream

基本概念



#### Source :   来源  近义词：Producer,Publisher

#### Sink ： 接收器  近义词：Consumer,Subsciber

#### Processor:     对于上流而言是Sink，对于下流而言是Source

Reactive Streams:

​	Publisher

​	Subscriber

​	Processor

消息大致分为两个部分：

消息头：（Headers)

消息体：（Body/Payload）

#### 定义标准的消息发送源头

```

```



#### 问答

@EnableBinding 有什么用

答：@EnableBinding 将`Source`、`Sink`以及`Processor`提升成相应的代理类





## Spring Cloud Sleuth

整合：

###引入maven

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-sleuth</artifactId>
</dependency>
```

### 日志发生的变化

当应用ClassPath存在org.springframework.cloud:spring-cloud-starter-sleuth时候，日志会发生调整

出现这个东西：traceId

[sleuth-server,1fe5f178773b7b5d,1fe5f178773b7b5d,false]

它会调整当前日志系统(slf4j)的`MDC`(Mapped Diagnostic Contexts)

org.springframework.cloud.sleuth.log.Slf4jSpanLogger

```java
public void logContinuedSpan(Span span) {
    MDC.put(Span.SPAN_ID_NAME, Span.idToHex(span.getSpanId()));
    MDC.put(Span.TRACE_ID_NAME, span.traceIdString());
    MDC.put(Span.SPAN_EXPORT_NAME, String.valueOf(span.isExportable()));
    setParentIdIfPresent(span);
    log("Continued span: {}", span);
}
```

整体流程

sleuth会自动装配一个名为TraceFliter的组件（在Spring WebMVC DispatcherServlet之前），它会增加一些slf4j MDC





###Zipkin整合

####创建Spring-Cloud-Zipkin服务器（模块名称）

start.spring.io:zipkin client

####maven导入

```xml
<!--Zipkin 服务器依赖-->
<dependency>
    <groupId>io.zipkin.java</groupId>
    <artifactId>zipkin-server</artifactId>
</dependency>
<!--Zipkin 服务器UI控制器-->
<dependency>
    <groupId>io.zipkin.java</groupId>
    <artifactId>zipkin-autoconfigure-ui</artifactId>
    <!--<scope>runtime</scope>-->
</dependency>
```

#### 添加配置

```properties
##服务名称
spring.application.name=zipkin-server
##服务端口
server.port=23456
##安全管理关闭
management.security.enabled=false
```

##### 激活@EnableZipkinServer

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zipkin.server.EnableZipkinServer;

@SpringBootApplication
@EnableZipkinServer//不建议使用，咋做？？？？
public class ZipkinServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZipkinServerApplication.class,args);
    }
}
```



### HTTP收集（HTTP调用）

###简单整合Spring-Cloud-Sleuth(模块名称)

####maven导入

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-sleuth</artifactId>
</dependency>
<!--新添加依赖-->
<!--Zipkin 客户端依赖-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>
```

#### 添加zipkin配置

```properties
##服务名称
spring.application.name=sleuth-server
##服务端口
server.port=8080

##新增配置##
##Zipkin服务器配置
zipkin.server.host=localhost
zipkin.server.port=23456
###增加 Zipkin服务器地址
spring.zipkin.base-url=http://${zipkin.server.host}:${zipkin.server.port}/
```

####启动zipkinServer,sleuthServer两个服务

测试，打开zipkin服务端进行监控:

http://localhost:23456/zipkin/

访问sleuth模块:



### 整合全部服务（HTTP方式）spring-cloud-sleuth-demo 模块

####程序调用链：

sleuth-server  -->	zuul-server  -->  person-consumer  -->  person-service

#####启动服务顺序：

Zipkin Server     	 端口：23456  启动查看 http://localhost:23456/zipkin/

Eureka Server        端口：8761    启动查看 http://localhost:8761/

Config Server    	 端口：10000  启动查看 http://localhost:10000/zuul/prod  or /zuul-prod.properties

Person Service 	 端口：7070    启动查看 http://localhost:7070/person/find/all

Person Consumer 端口：8080    启动查看 http://localhost:8080/person/find/all

Zuul Server		 端口：9090    启动查看 http://localhost:9090/person-zoo/person/find/all

Sleuth Server         端口：6060   启动查看 http://localhost:6060/hello

http://localhost:6060/to/zuul/person-zoo/person/find/all

要多模块打印日志，才能到zipkin中



#### 改造Sleuth Server

####连接eureka服务器

##### maven导入，增加eureka依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

##### 增加发现eureka server配置(最下面一行)

```properties
##服务名称
spring.application.name=sleuth-server
##服务端口
server.port=6060
##Zipkin服务器配置
zipkin.server.host=localhost
zipkin.server.port=23456
###增加 Zipkin服务器地址
spring.zipkin.base-url=http://${zipkin.server.host}:${zipkin.server.port}/

##Eureka Server服务 Url 用于客户端注册
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
```

#####激活eureka客户端

```java
@SpringBootApplication
@EnableDiscoveryClient
public class SleuthServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SleuthServerApplication.class,args);
    }

    //转发使用，新增的，不要在bean中声明bean,循环依赖。。。
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
```

##### 增加转发的Controller，转发到zuul-server

```java
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
        logger.info("/to/zuul/person-zoo/person/find/all");
        //zuul-server是应用名称
        String url = "http://zuul-server/person/find/all";
        return restTemplate.getForObject(url,Object.class);
    }
}

```

重新启动：sleuth-server ,测试：

访问zuul添加参数

curl -XPOST http://localhost:9090/person-zoo/person/save  -H "content-type:application/json;charset=UTF-8" -d '{"name":"fankun"}'

访问sleuth-server,获取添加的参数

http://localhost:6060/to/zuul/person-zoo/person/find/all



####在zuul-server上报到zipkin服务

#####maven导入

```xml
<!--Zipkin 客户端依赖，使用Zipkin时才增加-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>
```

##### 增加配置

```properties
##Zipkin服务器配置
zipkin.server.host=localhost
zipkin.server.port=23456
###增加 Zipkin服务器地址
spring.zipkin.base-url=http://${zipkin.server.host}:${zipkin.server.port}/
```

不用激活，直接就可以使用

访问后，zipkin出现轨迹。

####在person-consumer,person-service上报到zipkin服务

##### maven导入

```xml
<!--Zipkin 客户端依赖，使用Zipkin时才增加-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>
```

##### 增加配置

```properties
##Zipkin服务器配置
zipkin.server.host=localhost
zipkin.server.port=23456
###增加 Zipkin服务器地址
spring.zipkin.base-url=http://${zipkin.server.host}:${zipkin.server.port}/
```

不用激活，直接就可以使用



##### person-api中增加拦截器，打印日志

```java

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 增加通用日志
 */
public class WebConfig extends WebMvcConfigurerAdapter {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                return false;
            }

            @Override
            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
                logger.info("request URI:{}",request.getRequestURI());
            }

            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

            }
        });
    }
}

```

在person-consumer,person-service启动类上添加

```java
@Import(WebConfig.class)
```

重新启动

### Spring Cloud Stream收集（消息）【之前是http收集】

#### 调整zipkin-server通过stream来收集

#### maven导入

```xml
<!--Zipkin 服务器通过stream跟踪信息-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-sleuth-zipkin-stream</artifactId>
</dependency>
<!--Zipkin 服务器依赖-->
<!--<dependency>-->
    <!--<groupId>io.zipkin.java</groupId>-->
    <!--<artifactId>zipkin-server</artifactId>-->
<!--</dependency>-->
<!--Zipkin 服务器UI控制器-->
<dependency>
    <groupId>io.zipkin.java</groupId>
    <artifactId>zipkin-autoconfigure-ui</artifactId>
    <!--<scope>runtime</scope>-->
</dependency>
<!--使用kafka作为stream服务器-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-stream-binder-kafka</artifactId>
</dependency>
```

####启动zookeeper

####启动kafka

#### 启动当前服务 zipkin-server

####zipkin客户端配置：

#####调整zuul-server

#####maven导入

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-stream-binder-kafka</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-sleuth</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-sleuth-stream</artifactId>
</dependency>
```

