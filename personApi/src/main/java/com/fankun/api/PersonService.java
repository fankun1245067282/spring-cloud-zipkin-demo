package com.fankun.api;

import com.fankun.modle.Person;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;

@FeignClient("person-service")
public interface PersonService {

    @PostMapping("/person/save")
    boolean save(@RequestBody Person person);

    @GetMapping("/person/find/all")
    Collection<Person> findAll();
}
