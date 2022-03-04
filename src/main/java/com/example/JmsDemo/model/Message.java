package com.example.JmsDemo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Setter;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.UUID;

@JsonDeserialize(builder = Message.MessageBuilder.class)
@Value
@Builder(toBuilder = true)
@Setter
public class Message {

    UUID id;
    String content;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    OffsetDateTime processed;

    @JsonPOJOBuilder(withPrefix = "")
    public static class MessageBuilder {
    }
}
