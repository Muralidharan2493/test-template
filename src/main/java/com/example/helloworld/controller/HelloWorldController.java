package com.example.helloworld.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
// Hello world controller...
@RestController
public class HelloWorldController {

    @GetMapping("/hello")
    public String sendGreetings() {
        return "Hello, World! - Release1";
    }
}
