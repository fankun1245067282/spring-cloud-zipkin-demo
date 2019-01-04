package com.fankun.web;

import com.fankun.api.PersonService;
import com.fankun.modle.Person;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 可以不实现PersonService接口，只要url相同就可以了
 */
@RestController
// implements PersonService
public class PersonServerController {
    private Random rand = new Random();
    private Map<Integer, Person> persons = new ConcurrentHashMap<>();
    private AtomicInteger idGenerator = new AtomicInteger(0);

    @PostMapping(value="person/save")
    @HystrixCommand(fallbackMethod = "saveFault",
            commandProperties={@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="100")})
    public boolean save(@RequestBody Person person) throws InterruptedException {
        System.out.println("provider save threadName:"+Thread.currentThread().getName());
        int n = rand.nextInt(200);
        System.out.println("provider save costs time "+n+" ms");
        Thread.sleep(n);

        Integer id = idGenerator.getAndIncrement();
        person.setId(id);
        persons.put(id, person);
        return true;
    }
    //参数要和上面的保持一致
    public boolean saveFault(Person person){
        System.out.println("provider saveFault is invoked,threadName:"+Thread.currentThread().getName()+"  person:"+person);
        return false;
    }

    @GetMapping(value="/person/find/all")
    @HystrixCommand(fallbackMethod = "findAllFault",
            commandProperties={@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="100")})
    public Collection<Person> findAll() throws InterruptedException {
        System.out.println("provider findAll threadName:"+Thread.currentThread().getName());
        int n = rand.nextInt(200);
        System.out.println("provider findAll costs time "+n+" ms");
        Thread.sleep(n);
        return persons.values();
    }


    public Collection<Person> findAllFault() {
        System.out.println("provider findAllFault  is invoked,threadName:"+Thread.currentThread().getName());
        return Collections.emptyList();
    }
}
