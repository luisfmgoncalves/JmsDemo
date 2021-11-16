package com.example.JmsDemo.route;

import com.example.JmsDemo.exception.ExceptionHandler;
import com.example.JmsDemo.handler.JmsDemoApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@EnableWebFlux
public class JmsDemoApiRouter {

    @Bean
    public RouterFunction<ServerResponse> route(JmsDemoApi jmsDemoApi) {
        return RouterFunctions.route()
                              .path("", builder -> {
                                  builder.GET("/search", jmsDemoApi::searchMessages).build();
                              })
                              .onError(Throwable.class::isInstance, ExceptionHandler::wrapException)
                              .build();
    }

}
