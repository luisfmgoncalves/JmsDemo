package com.example.JmsDemo.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
public final class ExceptionHandler {
    private ExceptionHandler() {
    }

    public static Mono<ServerResponse> wrapException(Throwable throwable, ServerRequest request) {

        log.error("Exception thrown for request: {}:{}. {}", request.path(), request.methodName(), throwable.getMessage());

        return ServerResponse.status(HttpStatus.I_AM_A_TEAPOT)
                             .contentType(APPLICATION_JSON)
                             .bodyValue("Grab a tea cause this is not working.");
    }
}
