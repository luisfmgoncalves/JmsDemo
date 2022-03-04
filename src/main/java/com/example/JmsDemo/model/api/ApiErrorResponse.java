package com.example.JmsDemo.model.api;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class ApiErrorResponse {

    ApiErrorCode errorCode;
    String message;

}
