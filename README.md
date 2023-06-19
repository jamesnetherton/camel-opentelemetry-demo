Demonstrates tracing a Camel `Processor` class with OpenTelemetry and the `WithSpan` annotation.

## Running

Before starting the application, start the OpenTelemetry collector and Jaeger UI.

```shell
docker-compose up -d
```

### To run the error scenario

```shell
mvn compile exec:exec
```

### To run the working scenario

This configures `camel-reactive-executor-vertx`.

```shell
mvn compile exec:exec -Dvertx
```

### Browse generated traces

When the application is running, it will generate some output to STDOUT like:

```
=======> Got body: 09a68ba9-964d-4633-a906-0093a25d7b67
```

You can view the traces in the Jaeger UI at http://localhost:16686. From the `Service` selection field choose `camel-opentelemetry-demo`.

In the error scenario there will be separate traces for the timer `tick` endpoint and another for the invocation of `MyProcessor`.

In the working scenario, there will be a trace with 2 spans where `MyProcessor` correctly has its parent span set.

```
- tick
| -- MyProcessor
```
