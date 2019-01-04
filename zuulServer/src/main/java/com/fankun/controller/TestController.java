package com.fankun.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test")
    public String findName(){
        System.out.println("/test/findname:return fankun");
        return "this is test a controller";
    }
}
