package com.example.JmsDemo.route;

import com.example.JmsDemo.exception.ExceptionHandler;
import com.example.JmsDemo.handler.MessageAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.example.JmsDemo.route.RequestParameters.SEARCH_QUERY_PARAM;

@Configuration
@EnableWebFlux
public class JmsDemoApiRouter {

    @Bean
    public RouterFunction<ServerResponse> route(MessageAPI messageApi) {
        return RouterFunctions.route()
                              .path("", builder -> {
                                  builder.GET("/search", RequestPredicates.queryParam(SEARCH_QUERY_PARAM, t -> true), messageApi::searchMessages).build();
                              })
                              .onError(Throwable.class::isInstance, ExceptionHandler::wrapException)
                              .build();
    }

}
