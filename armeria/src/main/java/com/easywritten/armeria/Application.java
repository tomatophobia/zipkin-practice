package com.easywritten.armeria;

import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.brave.BraveService;
import com.linecorp.armeria.server.logging.LoggingService;

import brave.Tracing;
import brave.http.HttpTracing;

public final class Application {

    public static void main(String[] args) throws Exception {
        Tracing tracing = TracingFactory.create();
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
