package com.easywritten.armeria;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.client.brave.BraveClient;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.annotation.Get;

import brave.http.HttpTracing;

public class GreetingAnnotatedService {
    private static final Logger logger = LoggerFactory.getLogger(GreetingAnnotatedService.class);

    @Get("/hello")
    public HttpResponse hello() {
        logger.info("armeria-hello");
        return HttpResponse.of("armeria-hello");
    }

    @Get("/hi")
    public HttpResponse hi() {
        logger.info("armeria-hi");
        HttpTracing httpTracing = HttpTracing.create(TracingFactory.create());
        WebClient client = WebClient
                .builder("http://localhost:8081")
                .decorator(BraveClient.newDecorator(httpTracing.clientOf("armeria-server")))
                .build();
        String response = client.get("/hi").aggregate().join().contentUtf8();
        return HttpResponse.of("armeria-hi " + response);
    }

}
