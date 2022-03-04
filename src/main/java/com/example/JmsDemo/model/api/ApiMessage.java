package com.example.JmsDemo.model.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = ApiMessage.ApiMessageBuilder.class)
public class ApiMessage {

    UUID id;
    String content;
    String processed;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ApiMessageBuilder {
    }
}