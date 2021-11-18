package com.example.JmsDemo.model;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class ApiMessageResponse {
    UUID id;
    String content;
    String processed;
}
