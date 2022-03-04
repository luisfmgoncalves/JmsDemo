package com.example.JmsDemo.exception;

import com.example.JmsDemo.model.api.ApiErrorCode;
import com.example.JmsDemo.model.api.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.example.JmsDemo.model.api.ApiErrorCode.INTERNAL_ERROR;
import static com.example.JmsDemo.model.api.ApiErrorCode.SERVICE_UNAVAILABLE;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
public final class ExceptionHandler {
    private ExceptionHandler() {
    }

    public static Mono<ServerResponse> wrapException(Throwable throwable, ServerRequest request) {

        log.error("Exception thrown for request: {}:{}. {}", request.path(), request.methodName(), throwable.getMessage(), throwable);

        if(throwable instanceof ElasticsearchException) {
            return buildErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, SERVICE_UNAVAILABLE, throwable.getMessage());
        }

        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_ERROR, throwable.getMessage());
    }

    private static Mono<ServerResponse> buildErrorResponse(HttpStatus status, ApiErrorCode code, String message) {
        ApiErrorResponse response = ApiErrorResponse.builder()
                .errorCode(code)
                .message(message)
                .build();

        return ServerResponse.status(status)
                .contentType(APPLICATION_JSON)
                .bodyValue(response);
    }
}
