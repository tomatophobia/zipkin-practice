package com.easywritten.armeria;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.annotation.Get;

public class GreetingAnnotatedService {
    private static final Logger logger = LoggerFactory.getLogger(GreetingAnnotatedService.class);

    @Get("/hi")
    public HttpResponse hi() {
        return HttpResponse.of("hello");
    }

    @Get("/hello")
    public HttpResponse hello() {
        logger.info("hello");
        return HttpResponse.of("hi");
    }

}
