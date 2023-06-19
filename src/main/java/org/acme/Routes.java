package org.acme;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import org.apache.camel.BindToRegistry;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.opentelemetry.OpenTelemetryTracer;

import java.lang.reflect.Method;
import java.util.UUID;

public class Routes extends RouteBuilder {
    @BindToRegistry("myProcessor")
    private MyProcessor processor = new MyProcessor();

    public void configure() throws Exception {
        OpenTelemetryTracer tracer = new OpenTelemetryTracer();
        tracer.init(this.getContext());

        from("timer:tick?period=5s")
                .setBody().constant(UUID.randomUUID().toString())
                .process("myProcessor");
    }

    @BindToRegistry("vertx")
    public Object bindVertxIfRequired() {
        try {
            Class<?> vertx = Class.forName("io.vertx.core.Vertx");
            Method createVertx = vertx.getDeclaredMethod("vertx");
            return createVertx.invoke(null);
        } catch (Exception e) {
            // Ignored
        }
        return null;
    }

    class MyProcessor implements Processor {
        @WithSpan("MyProcessor")
        @Override
        public void process(Exchange exchange) throws Exception {
            System.out.println("=======> Got body: " + exchange.getMessage().getBody());
        }
    }
}
