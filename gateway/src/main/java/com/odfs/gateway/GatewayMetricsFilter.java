package com.ofds.gateway;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class GatewayMetricsFilter implements GlobalFilter, Ordered {

    private final Counter requestCounter;

    public GatewayMetricsFilter(MeterRegistry registry) {
        this.requestCounter = registry.counter("gateway_requests_total");
    }

    @Override
    public Mono<Void> filter(org.springframework.web.server.ServerWebExchange exchange,
                             org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        requestCounter.increment();
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}

