package com.fankun.web;


import com.fankun.api.PersonService;
import com.fankun.modle.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class PersonConsumerController {
    private final PersonService personService;

    @Autowired
    public PersonConsumerController(PersonService personService) {
        this.personService = personService;
    }




    @PostMapping(value="/person/save")
    public boolean save(@RequestBody Person person) {
        System.out.println("consumer save threadName:"+Thread.currentThread().getName());
        return personService.save(person);
    }

    @GetMapping(value="/person/find/all")
    public Collection<Person> findAll() {
        System.out.println("consumer findAll threadName:"+Thread.currentThread().getName());
        return personService.findAll();
    }
}
