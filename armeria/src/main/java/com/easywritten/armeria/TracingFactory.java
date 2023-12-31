package com.easywritten.armeria;

import java.io.IOException;
import java.util.logging.Logger;

import com.linecorp.armeria.common.brave.RequestContextCurrentTraceContext;

import brave.context.slf4j.MDCScopeDecorator;
import brave.Tracing;
import brave.propagation.CurrentTraceContext;
import zipkin2.Span;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.Sender;
import zipkin2.reporter.brave.ZipkinSpanHandler;
import zipkin2.reporter.urlconnection.URLConnectionSender;

final class TracingFactory {

    private static final String serviceName = "hi-armeria";
    private static Tracing tracing;

    /**
     * Controls aspects of tracing such as the name that shows up in the UI
     */
    static Tracing create() {
        if (tracing != null) {
            return tracing;
        }

        CurrentTraceContext currentTraceContext =
                RequestContextCurrentTraceContext.builder()
                                                 .addScopeDecorator(MDCScopeDecorator.get())
                                                 .build();
        tracing = Tracing.newBuilder()
                         .localServiceName(serviceName)
                         .currentTraceContext(currentTraceContext)
                         .addSpanHandler(ZipkinSpanHandler.create(spanReporter(sender())))
                         .build();
        return tracing;
    }

    /**
     * Configuration for how to send spans to Zipkin
     */
    static Sender sender() {
        return URLConnectionSender.create("http://localhost:9411/api/v2/spans");
    }

    /**
     * Configuration for how to buffer spans into messages for Zipkin
     */
    static AsyncReporter<Span> spanReporter(Sender sender) {
        final AsyncReporter<Span> spanReporter = AsyncReporter.create(sender);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            spanReporter.close(); // Make sure spans are reported on shutdown
            try {
                sender.close(); // Release any network resources used to send spans
            } catch (IOException e) {
                Logger.getAnonymousLogger().warning("error closing trace sender: " + e.getMessage());
            }
        }));

        return spanReporter;
    }

    private TracingFactory() {}
}
