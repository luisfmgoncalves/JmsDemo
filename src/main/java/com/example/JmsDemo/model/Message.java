package com.example.JmsDemo.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@JsonDeserialize(builder = Message.MessageBuilder.class)
@Value
@Builder(toBuilder = true)
public class Message {
    UUID id;
    String content;

    @JsonPOJOBuilder(withPrefix = "")
    public static class MessageBuilder {
    }
}
