package com.easywritten.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class GreetingController {

    private final RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(GreetingController.class);

    public GreetingController(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
    }

    @GetMapping("/hello")
    public String hello() {
        logger.info("spring-hello");
        String response = restTemplate.getForObject("http://localhost:8080/hello", String.class);
        return "spring-hello " + response;
    }

    @GetMapping("/hi")
    public String hi() {
        logger.info("spring-hi");
        return "spring-hi";
    }
}
