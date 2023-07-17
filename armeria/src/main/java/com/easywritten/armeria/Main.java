package com.easywritten.armeria;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linecorp.armeria.common.brave.RequestContextCurrentTraceContext;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.brave.BraveService;
import com.linecorp.armeria.server.logging.LoggingService;

import brave.Tracing;
import brave.http.HttpTracing;
import zipkin2.reporter.Sender;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        Tracing tracing = TracingFactory.create("hi-armeria");
        HttpTracing httpTracing = HttpTracing.create(tracing);

        final Server server = Server.builder()
                                    .http(8080)
                                    .decorator(LoggingService.newDecorator())
                                    .decorator(BraveService.newDecorator(httpTracing))
                                    .annotatedService(new GreetingAnnotatedService())
                                    .build();
        server.closeOnJvmShutdown();
        server.start().join();
    }
}
