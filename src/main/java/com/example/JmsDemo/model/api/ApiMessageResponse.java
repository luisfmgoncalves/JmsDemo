package com.example.JmsDemo.model.api;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class ApiMessageResponse {

    List<ApiMessage> messages;
}