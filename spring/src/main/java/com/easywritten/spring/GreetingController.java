package com.easywritten.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private Logger logger = LoggerFactory.getLogger(GreetingController.class);

    @GetMapping("/hello")
    public String hello() {
        logger.info("hello");
        return "hi";
    }

    @GetMapping("/hi")
    public String hi() {
        logger.info("hi");
        return "hello";
    }
}
