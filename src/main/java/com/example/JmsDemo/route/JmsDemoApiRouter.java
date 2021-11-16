package com.example.JmsDemo.route;

import com.example.JmsDemo.exception.ExceptionHandler;
import com.example.JmsDemo.service.ApiService;
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
    public RouterFunction<ServerResponse> route(ApiService apiService) {
        return RouterFunctions.route()
                              .path("", builder -> {
                                  builder.GET("/search", apiService::searchMessages).build();
                              })
                              .onError(Throwable.class::isInstance, ExceptionHandler::wrapException)
                              .build();
    }

}
